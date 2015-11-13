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

import static com.thebuzzmedia.exiftool.commons.Objects.firstNonNull;

/**
 * Result of command execution.
 * Result is defined by:
 * - An exit code (zero is success).
 * - An output: message logged by process.
 */
public class Result {

	/**
	 * Exit status.
	 */
	private final int exitStatus;

	/**
	 * Command output.
	 */
	private final String output;

	/**
	 * Create result.
	 *
	 * @param exitStatus Exit status.
	 * @param output Command output.
	 */
	Result(int exitStatus, String output) {
		this.exitStatus = exitStatus;
		this.output = firstNonNull(output, "");
	}

	/**
	 * Get exist status.
	 *
	 * @return Exit status.
	 */
	public int getExitStatus() {
		return exitStatus;
	}

	/**
	 * Get command output.
	 *
	 * @return Command output.
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * Check if command line succeeded.
	 *
	 * @return True if result is a success, false otherwise.
	 */
	public boolean isSuccess() {
		return exitStatus == 0;
	}

	@Override
	public int hashCode() {
		return exitStatus + output.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof Result) {
			Result r = (Result) o;
			return exitStatus == r.exitStatus &&
				output.equals(r.output);
		}

		return false;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", exitStatus, output);
	}
}
