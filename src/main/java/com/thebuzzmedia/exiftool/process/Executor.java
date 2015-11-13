/**
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

package com.thebuzzmedia.exiftool.process;

import com.thebuzzmedia.exiftool.exceptions.ProcessIOException;
import com.thebuzzmedia.exiftool.exceptions.ProcessInterruptionException;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;

import static com.thebuzzmedia.exiftool.commons.IOs.readInputStream;

/**
 * Execute command line and return the result of execution.
 */
public class Executor {

	/**
	 * Class Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(Executor.class);

	// Use static factory
	Executor() {
	}

	/**
	 * Execute command line.
	 *
	 * @param command Command line with its arguments.
	 * @return Execution result.
	 */
	public Result execute(Command command) {
		try {
			LinkedList<String> args = new LinkedList<String>(command.getArguments());
			args.addFirst(command.getExecutable());

			Process proc = new ProcessBuilder(args).start();
			proc.waitFor();

			int exitStatus = proc.exitValue();
			String output = readInputStream(proc.getInputStream());
			return new Result(exitStatus, output);
		}
		catch (InterruptedException ex) {
			log.error(ex.getMessage(), ex);
			throw new ProcessInterruptionException(ex);
		}
		catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw new ProcessIOException(ex);
		}
	}
}
