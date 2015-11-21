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

import com.thebuzzmedia.exiftool.commons.Objects;
import com.thebuzzmedia.exiftool.process.Command;

import java.util.ArrayList;
import java.util.List;

import static com.thebuzzmedia.exiftool.commons.Collections.isEmpty;
import static com.thebuzzmedia.exiftool.commons.Collections.join;
import static com.thebuzzmedia.exiftool.commons.Collections.size;
import static java.util.Collections.unmodifiableList;

/**
 * Default implementation for {@link Command} interface.
 * This implementation should only be used with {@link com.thebuzzmedia.exiftool.process.command.CommandBuilder} builder.
 */
class DefaultCommand implements Command {

	/**
	 * List of arguments:
	 * - First element is the executable.
	 * - Next elements are the executable arguments (optional).
	 *
	 * Once created, this list will be unmodifiable.
	 */
	private final List<String> cmd;

	/**
	 * Create command line.
	 *
	 * @param executable Executable value.
	 * @param arguments List of optional arguments.
	 */
	DefaultCommand(String executable, List<String> arguments) {
		List<String> args = new ArrayList<String>(size(arguments) + 1);

		// Add first argument (should always be executable argument).
		args.add(executable);

		// Add optional arguments.
		if (!isEmpty(arguments)) {
			args.addAll(arguments);
		}

		this.cmd = unmodifiableList(args);
	}

	@Override
	public List<String> getArguments() {
		return cmd;
	}

	@Override
	public String toString() {
		return join(cmd, " ");
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof DefaultCommand) {
			DefaultCommand c = (DefaultCommand) o;
			return Objects.equals(cmd, c.cmd);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(cmd);
	}
}
