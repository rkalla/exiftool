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

package com.thebuzzmedia.exiftool.it.builder;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;

import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import com.thebuzzmedia.exiftool.process.executor.DefaultCommandResult;

public class ExifToolCustomExecutorIT extends AbstractExifToolIT {

	@Override
	ExifToolBuilder create() {
		return new ExifToolBuilder().withExecutor(new CommonsExecutor());
	}

	private static class CommonsExecutor implements CommandExecutor {

		private CommonsExecutor() {
		}

		@Override
		public CommandResult execute(Command command) throws IOException {
			CommandLine commandLine = CommandLine.parse(command.toString());
			CommandOutputStream out = new CommandOutputStream();
			PumpStreamHandler psh = new PumpStreamHandler(out);
			DefaultExecutor executor = executor();
			executor.setStreamHandler(psh);

			final int exitValue = executor.execute(commandLine);
			final String output = out.getOutput();
			return new DefaultCommandResult(exitValue, output);
		}

		@Override
		public CommandResult execute(Command command, OutputHandler handler) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public CommandProcess start(Command command) throws IOException {
			throw new UnsupportedOperationException();
		}

		private DefaultExecutor executor() {
			return new DefaultExecutor();
		}
	}

	private static class CommandOutputStream extends LogOutputStream {
		private final StringBuilder sb;

		private CommandOutputStream() {
			this.sb = new StringBuilder();
		}

		@Override
		protected void processLine(String line, int level) {
			sb.append(line).append(System.getProperty("line.separator"));
		}

		public String getOutput() {
			return sb.toString().trim();
		}
	}
}
