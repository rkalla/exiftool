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

package com.thebuzzmedia.exiftool.process.command;

import com.thebuzzmedia.exiftool.process.Command;

import java.util.LinkedList;
import java.util.List;

import static com.thebuzzmedia.exiftool.commons.PreConditions.notBlank;

/**
 * Command builder.
 * This builder should be used to create immutable instance of {@link com.thebuzzmedia.exiftool.process.Command}.
 */
public class CommandBuilder {

	/**
	 * Get new builder.
	 * @param executable Executable value.
	 * @return The new builder.
	 * @throws NullPointerException If executable is null.
	 * @throws IllegalArgumentException If executable is empty or blank.
	 */
	public static CommandBuilder builder(String executable) {
		return new CommandBuilder(executable);
	}

	/**
	 * Command Line Executable.
	 */
	private final String executable;

	/**
	 * Command Line Arguments (will be appended next to the executable value).
	 */
	private final List<String> arguments;

	/**
	 * Create builder.
	 *
	 * @param executable Executable value.
	 * @throws NullPointerException If executable is null.
	 * @throws IllegalArgumentException If executable is empty or blank.
	 */
	private CommandBuilder(String executable) {
		this.executable = notBlank(executable, "Command line executable should be defined");
		this.arguments = new LinkedList<String>();
	}

	/**
	 * Add new argument to the command line.
	 *
	 * @param arg First argument.
	 * @param args Next optional arguments.
	 * @return The builder.
	 * @throws NullPointerException If one of the arguments is null.
	 * @throws IllegalArgumentException If one of the arguments is empty or blank.
	 */
	public CommandBuilder addArgument(String arg, String... args) {
		add(arg);

		if (args.length > 0) {
			for (String a : args) {
				add(a);
			}
		}

		return this;
	}

	/**
	 * Build the command line.
	 *
	 * @return Command Line instance.
	 */
	public Command build() {
		return new DefaultCommand(executable, arguments);
	}

	private void add(String arg) {
		this.arguments.add(notBlank(arg, "Command line argument should be defined if set"));
	}
}
