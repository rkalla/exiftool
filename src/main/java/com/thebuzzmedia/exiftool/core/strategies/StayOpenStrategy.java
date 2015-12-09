/**
 * Copyright 2011 The Buzz Media, LLC
 * Copyright 2015 Mickael Jeanroy <mickael.jeanroy@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thebuzzmedia.exiftool.core.strategies;

import com.thebuzzmedia.exiftool.ExecutionStrategy;
import com.thebuzzmedia.exiftool.Scheduler;
import com.thebuzzmedia.exiftool.Version;
import com.thebuzzmedia.exiftool.commons.iterables.Mapper;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import com.thebuzzmedia.exiftool.process.command.CommandBuilder;

import java.io.IOException;
import java.util.List;

import static com.thebuzzmedia.exiftool.commons.iterables.Collections.map;

/**
 * Execution strategy that use {@code exiftool} with the {@code stay_open} feature.
 */
public class StayOpenStrategy implements ExecutionStrategy {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(StayOpenStrategy.class);

	/**
	 * Mapper to use to append a line break to each
	 * arguments.
	 */
	private static final ArgumentMapper MAPPER = new ArgumentMapper();

	/**
	 * Minimum version of {@code exiftool} supporting {@code stay_open} feature.
	 */
	private static final Version V8_36 = new Version("8.36");

	/**
	 * Scheduler: will be used to perform automatic cleanup.
	 * If automatic cleanup is disabled (if delay is equal or less than zero),
	 * then it will be set to {@code null}.
	 */
	private final Scheduler scheduler;

	/**
	 * Process opened when the first execution is called.
	 * This process will remain open until a call to {@link #close} is made.
	 */
	private CommandProcess process;

	/**
	 * Create strategy.
	 * Scheduler provided in parameter will be used to clean resources (exiftool process).
	 *
	 * @param scheduler Delay between automatic cleanup.
	 */
	public StayOpenStrategy(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void execute(CommandExecutor executor, String exifTool, List<String> arguments, OutputHandler handler) throws IOException {
		log.debug("Using ExifTool in daemon mode (-stay_open True)...");
		List<String> newArgs = map(arguments, MAPPER);

		synchronized (this) {
			// Start daemon process if it is not already started.
			// If this is our first time calling getImageMeta with a "stayOpen"
			// connection, set up the persistent process and run it so it is
			// ready to receive commands from us.
			if (process == null || process.isClosed()) {
				log.debug("Start exiftool process");
				process = executor.start(CommandBuilder.builder(exifTool)
					.addArgument("-stay_open", "True", "-@", "-")
					.build());
			}

			// Always reset the cleanup task.
			scheduler.stop();
			scheduler.start(new Runnable() {
				@Override
				public void run() {
					safeClose();
				}
			});

			try {
				process.write(newArgs);
				process.flush();
				process.read(handler);
			}
			catch (IOException ex) {
				log.error(ex.getMessage(), ex);
				throw ex;
			}
		}
	}

	@Override
	public synchronized boolean isRunning() {
		return process != null && process.isRunning();
	}

	@Override
	public boolean isSupported(Version version) {
		return V8_36.compareTo(version) <= 0;
	}

	@Override
	public synchronized void close() throws Exception {
		if (process != null) {
			closeScheduler();
			closeProcess();
		}
	}

	// Implement finalizer.
	// This is just a small security: it should not prevent caller
	// to explicitly close the exiftool process (but add this finalizer
	// if someone forget).
	@Override
	protected void finalize() throws Throwable {
		// Just delegate to original finalizer implementation
		super.finalize();

		// Be sure process is closed
		close();
	}

	/**
	 * Close pending cleanup task and stop scheduler.
	 * This scheduler may be re-used if necessary.
	 */
	private synchronized void closeScheduler() {
		// Try to stop cleanup task
		// Note: If task is not stopped, it may be executed later

		try {
			log.debug("Attempting to stop cleanup task");
			scheduler.stop();
			log.debug("Cleanup task successfully stopped");
		}
		catch (Exception ex) {
			// Should not fail everything.
			// Important to log warning at least.
			log.warn("Cleanup task failed to stop");
			log.warn(ex.getMessage(), ex);
		}
	}

	/**
	 * Close ExifTool process.
	 * Process may be re-used if necessary.
	 *
	 * @throws Exception If an error occurs during the close operation.
	 */
	private synchronized void closeProcess() throws Exception {
		try {
			// If ExifTool was used in stayOpen mode but getImageMeta was never
			// called then the streams were never initialized and there is nothing
			// to shut down or destroy, otherwise we need to close down all the
			// resources in use.
			log.debug("Attempting to close ExifTool daemon process, issuing '-stay_open\\nFalse\\n' command...");
			process.close();
			log.debug("ExifTool daemon process successfully closed");
		}
		catch (Exception ex) {
			// Log some warnings.
			log.warn("ExifTool daemon failed to stop");
			log.warn(ex.getMessage(), ex);

			// Re-throw the error, this will let the caller do what he wants with the exception.
			throw ex;
		}
		finally {
			// Dot not forget to set it to null.
			process = null;
		}
	}

	/**
	 * This is exactly the same operation as {@link #close} but catch
	 * all exceptions and log stacktrace.
	 * This method should be used internally to perform a close operation
	 * without catching or propagate exceptions.
	 */
	private synchronized void safeClose() {
		try {
			close();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Argument Mapper.
	 * This mapper should be used to concatenate a line break after
	 * each input.
	 */
	private static class ArgumentMapper implements Mapper<String, String> {

		/**
		 * Line Break.
		 * Used a static variable to not read from system property each time.
		 */
		private static final String BR = System.getProperty("line.separator");

		@Override
		public String map(String input) {
			return input + BR;
		}
	}
}
