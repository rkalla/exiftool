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

/**
 * Result of command execution.
 * Result is defined by:
 * - An exit status.
 * - Output.
 * - Status: success or failure.
 *
 * Most of the time, success & failure may be guess using exit status, but
 * this may more complex.
 */
public interface CommandResult {

	/**
	 * Exit status.
	 *
	 * @return Exit status.
	 */
	int getExitStatus();

	/**
	 * Check if command result is a success.
	 * When command is a success, then it must not be a failure.
	 *
	 * @return True if command is a success, false otherwise.
	 */
	boolean isSuccess();

	/**
	 * Check if command result is a failure.
	 * When command is a failure, then it must not be a success.
	 *
	 * @return True if command is a failure, false otherwise.
	 */
	boolean isFailure();

	/**
	 * Command output.
	 *
	 * @return Output.
	 */
	String getOutput();
}
