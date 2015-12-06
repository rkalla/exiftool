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

package com.thebuzzmedia.exiftool.tests.builders;

import com.thebuzzmedia.exiftool.process.CommandResult;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Build to create mock instance of {@link com.thebuzzmedia.exiftool.process.CommandResult} class.
 */
public class CommandResultBuilder {

	/**
	 * Exit Value.
	 * Default is `0`.
	 */
	private final int exitStatus;

	/**
	 * Command output.
	 * Default is an empty string.
	 */
	private String output;

	/**
	 * Success flag.
	 * Default is true if exit status is zero, false otherwise.
	 */
	private boolean success;

	/**
	 * Create builder with zero as default value for {@link #exitStatus}.
	 */
	public CommandResultBuilder() {
		this(0);
	}

	/**
	 * Create builder with value for {@link #exitStatus}.
	 *
	 * @param exitStatus Value for {@link #exitStatus}.
	 */
	public CommandResultBuilder(int exitStatus) {
		this.exitStatus = exitStatus;
		this.success = exitStatus == 0;
		this.output = "";
	}

	/**
	 * Update output value.
	 *
	 * @param output New output.
	 * @return Current builder.
	 */
	public CommandResultBuilder output(String output) {
		this.output = output;
		return this;
	}

	/**
	 * Update success flag.
	 *
	 * @param success New success flag.
	 * @return Current builder.
	 */
	public CommandResultBuilder success(boolean success) {
		this.success = success;
		return this;
	}

	/**
	 * Build object.
	 * **NOTE:** Returned object is a mock (created with mockito).
	 *
	 * @return Created instance.
	 */
	public CommandResult build() {
		CommandResult result = mock(CommandResult.class);
		when(result.getExitStatus()).thenReturn(exitStatus);
		when(result.isSuccess()).thenReturn(success);
		when(result.isFailure()).thenReturn(!success);
		when(result.getOutput()).thenReturn(output);
		return result;
	}
}
