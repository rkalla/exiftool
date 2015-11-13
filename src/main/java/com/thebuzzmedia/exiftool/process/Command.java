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

import java.util.LinkedList;
import java.util.List;

import static com.thebuzzmedia.exiftool.commons.PreConditions.notBlank;
import static java.util.Collections.unmodifiableList;

/**
 * Command Line.
 * A command line is defined by:
 * - An executable path (should be absolute or the executable name if it is globally available).
 * - Some arguments: argument order is important and will be preserved.
 */
public class Command {

	/**
	 * Command executable path.
	 */
	private final String executable;

	/**
	 * Command arguments.
	 */
	private final List<String> arguments;

	/**
	 * Create new executable command line.
	 * Command line path should be:
	 * - An absolute path to the executable.
	 * - A name that point to a global executable.
	 *
	 * @param executable Command line path.
	 * @throws java.lang.NullPointerException If executable is null.
	 * @throws java.lang.IllegalArgumentException If executable string is empty or blank.
	 */
	Command(String executable) {
		this.executable = notBlank(executable, "Exiftool path should be defined");
		this.arguments = new LinkedList<String>();
	}

	/**
	 * Add new argument to the command line.
	 *
	 * @param argument Argument.
	 * @throws java.lang.NullPointerException If argument is null.
	 * @throws java.lang.IllegalArgumentException If argument string is empty or blank.
	 */
	public void addArgument(String argument) {
		arguments.add(notBlank(argument, "Exiftool argument must be defined if set"));
	}

	/**
	 * Get executable path / name.
	 *
	 * @return Executable path / name.
	 */
	public String getExecutable() {
		return executable;
	}

	/**
	 * Get list of arguments.
	 * Returned list is an immutable collection.
	 *
	 * @return List of arguments.
	 */
	public List<String> getArguments() {
		return unmodifiableList(arguments);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof Command) {
			Command c = (Command) o;
			return executable.equals(c.executable) &&
				arguments.equals(c.arguments);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return executable.hashCode() + arguments.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(executable);

		for (String argument : arguments) {
			sb.append(" ").append(argument);
		}

		return sb.toString();
	}
}
