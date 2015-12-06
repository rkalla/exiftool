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

package com.thebuzzmedia.exiftool;

import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.OutputHandler;

import java.util.List;

/**
 * ExifTool execution strategy.
 *
 * <p />
 *
 * For instance:
 * <ul>
 *   <li>Execution using a one-shot process.</li>
 *   <li>Execution using {@code stay_open} flag: this strategy means that a process is started and re-used for next executions.</li>
 * </ul>
 *
 * Each implementation will define the main logic for reading and
 * writing metadata (this is the main purpose for the {@link #execute} method.
 *
 * <p />
 *
 * Implementation should also define a close method: this method
 * will be used to stop remaining process and clean previous execution.
 * Calling {@link #close} method should not prevent instances to be used
 * for a next execution.
 */
public interface ExecutionStrategy extends AutoCloseable {

	/**
	 * Execute exiftool command.
	 *
	 * @param executor ExifTool withExecutor.
	 * @param exifTool ExifTool withPath.
	 * @param arguments Command line arguments.
	 * @param handler Handler to read command output.
	 */
	void execute(CommandExecutor executor, String exifTool, List<String> arguments, OutputHandler handler);

	/**
	 * Check if exiftool process is currently running.
	 * This method is important especially if `stay_open` flag has been enabled.
	 *
	 * @return True if exiftool process is currently open, false otherwise.
	 */
	boolean isRunning();

	/**
	 * This method should be used to:
	 * - Close remaining process (if any).
	 * - Clean previous executions.
	 *
	 * For instance, with the `stay_open` flag, this method should:
	 * - Close opened process.
	 * - Stop task used to automatically close process.
	 *
	 * Once closed, ExifTool should still be able to use this strategy
	 * if a call to {@link #execute} is made.
	 */
	void close() throws Exception;
}
