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

import com.thebuzzmedia.exiftool.ExifToolStrategy;
import com.thebuzzmedia.exiftool.commons.Function;
import com.thebuzzmedia.exiftool.commons.Mapper;
import com.thebuzzmedia.exiftool.exceptions.ExifToolException;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import com.thebuzzmedia.exiftool.process.command.CommandBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.thebuzzmedia.exiftool.commons.Collections.each;
import static com.thebuzzmedia.exiftool.commons.Collections.map;

/**
 * Execution strategy that use {@code exiftool} with the {@code stay_open} feature.
 */
public class StayOpenStrategy implements ExifToolStrategy {

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
	 * Implementation of function used to cancel scheduled future.
	 */
	private static final CancelFunction CANCEL_FN = new CancelFunction();

	/**
	 * Delay between automatic cleanup execution.
	 * If this delay is less than or equal to zero, then the automatic task
	 * will not be executed.
	 */
	private final long delay;

	/**
	 * Scheduler: will be used to perform automatic cleanup.
	 * If automatic cleanup is disabled (if delay is equal or less than zero),
	 * then it will be set to `null`.
	 */
	private final ScheduledThreadPoolExecutor scheduler;

	/**
	 * Process opened when the first execution is called.
	 * This process will remain open until a call to {@link #close} is made.
	 */
	private CommandProcess process;

	/**
	 * Create strategy.
	 *
	 * <p>
	 *
	 * If {@code delay} in parameter is greater than zero, then a task will
	 * be automatically scheduled to clean resources. Otherwise, closing resources
	 * is up to the caller.
	 *
	 * @param delay Delay between automatic cleanup.
	 */
	public StayOpenStrategy(long delay) {
		this.delay = delay;

		if (delay > 0) {
			this.scheduler = new ScheduledThreadPoolExecutor(1);
			this.scheduler.setRemoveOnCancelPolicy(true);
		} else {
			this.scheduler = null;
		}
	}

	@Override
	public void execute(CommandExecutor executor, String exifTool, List<String> arguments, OutputHandler handler) {
		log.debug("Using ExifTool in daemon mode (-stay_open True)...");
		List<String> newArgs = map(arguments, MAPPER);

		// Start deamon process if it is not already started.
		start(executor, exifTool);

		// Always reset the cleanup task.
		resetCleanupTask();

		try {
			process.write(newArgs);
			process.flush();
			process.read(handler);
		}
		catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw new ExifToolException(ex);
		}
	}

	@Override
	public boolean isRunning() {
		return process != null && process.isRunning();
	}

	@Override
	public void close() throws Exception {
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
		// Be sure process is closed
		close();

		// Just delegate to original finalizer implementation
		super.finalize();
	}

	/**
	 * Close pending cleanup task and stop scheduler.
	 * This scheduler may be re-used if necessary.
	 */
	private void closeScheduler() {
		// Try to stop cleanup task
		// **Note:** If task is not stopped, it may be executed later

		try {
			log.debug("Attempting to stop cleanup task");
			stopCleanupTask();
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
	private void closeProcess() throws Exception {
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
	private void safeClose() {
		try {
			close();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Helper method used to make canceling the current task and scheduling a
	 * new one easier.
	 *
	 * It is annoying that we cannot just reset the timer on the task, but that
	 * isn't the way the java.util.Timer class was designed unfortunately.
	 */
	private void resetCleanupTask() {
		// no-op if the timer was never created.
		if (scheduler != null) {
			stopCleanupTask();
			startCleanupTask();
		}
	}

	/**
	 * Start new clean task.
	 *
	 * This task will be scheduled and will run in the
	 * specified delay.
	 *
	 * If no timer has been initialized, then this method
	 * is a no-op.
	 */
	private void startCleanupTask() {
		// no-op if the timer was never created.
		if (scheduler != null) {
			log.debug("Starting cleanup task");

			// Create task
			Runnable task = new Runnable() {
				@Override
				public void run() {
					safeClose();
				}
			};

			// Schedule, using given delay
			scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Stop pending clean task.
	 *
	 * If no timer has been initialized, then this method
	 * is a no-op.
	 */
	private void stopCleanupTask() {
		// no-op if the timer was never created.
		if (scheduler != null) {
			log.debug("Stopping cleanup task");

			// Cancel pending task.
			each(scheduler.getQueue(), CANCEL_FN);

			// Then, purge scheduler.
			// Pure operation remove cancelled tasks from the executor used by this scheduler.
			scheduler.purge();
		}
	}

	private void start(CommandExecutor executor, String exifTool) {
		// If this is our first time calling getImageMeta with a "stayOpen"
		// connection, set up the persistent process and run it so it is
		// ready to receive commands from us.
		if (process == null || process.isClosed()) {
			log.debug("Start exiftool process");
			process = executor.start(CommandBuilder.builder(exifTool)
				.addArgument("-stay_open", "True", "-@", "-")
				.build());
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

	/**
	 * Function that should be used to cancel instance
	 * of {@link java.util.concurrent.RunnableScheduledFuture} objects.
	 *
	 * This implementation assume that given parameter will always be instance of
	 * {@link java.util.concurrent.RunnableScheduledFuture}, since there will be no additional
	 * check before cast.
	 *
	 * Note that this should not be a problem with the scheduler used in this class.
	 */
	private static class CancelFunction implements Function<Runnable> {
		@Override
		public void apply(Runnable input) {
			((RunnableScheduledFuture) input).cancel(false);
		}
	}
}
