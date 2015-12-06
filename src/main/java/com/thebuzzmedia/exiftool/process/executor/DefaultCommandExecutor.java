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

package com.thebuzzmedia.exiftool.process.executor;

import com.thebuzzmedia.exiftool.exceptions.ProcessException;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.process.OutputHandler;

import java.io.IOException;
import java.util.List;

import static com.thebuzzmedia.exiftool.commons.io.IOs.closeQuietly;
import static com.thebuzzmedia.exiftool.commons.io.IOs.readInputStream;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notNull;

/**
 * Default Executor.
 */
class DefaultCommandExecutor implements CommandExecutor {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(DefaultCommandExecutor.class);

	/**
	 * Create default withExecutor.
	 */
	DefaultCommandExecutor() {
	}

	@Override
	public CommandResult execute(Command command) {
		return readProcessOutput(command, null);
	}

	@Override
	public CommandResult execute(Command command, OutputHandler handler) {
		return readProcessOutput(command, notNull(handler, "Handler should not be null"));
	}

	@Override
	public DefaultCommandProcess start(Command command) {
		final Process proc = createProcess(command);
		return new DefaultCommandProcess(proc.getInputStream(), proc.getOutputStream(), proc.getErrorStream());
	}

	private CommandResult readProcessOutput(Command cmd, OutputHandler h) {
		final Process proc = createProcess(cmd);
		final ResultHandler h1 = new ResultHandler();
		final OutputHandler handler = h == null ? h1 : new CompositeHandler(h, h1);

		readInputStream(proc.getInputStream(), handler);

		// Wait for end of process
		try {
			proc.waitFor();
			return new DefaultCommandResult(proc.exitValue(), h1.getOutput());
		}
		catch (InterruptedException ex) {
			log.error(ex.getMessage(), ex);
			throw new ProcessException(ex);
		}
		finally {
			// Close streams.
			closeQuietly(proc.getInputStream());
			closeQuietly(proc.getOutputStream());
			closeQuietly(proc.getErrorStream());
		}
	}

	private Process createProcess(Command command) {
		try {
			List<String> args = command.getArguments();
			return new ProcessBuilder(args).start();
		}
		catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw new ProcessException(ex);
		}
	}
}
