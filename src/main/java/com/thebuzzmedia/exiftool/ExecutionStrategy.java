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

import java.io.IOException;
import java.util.List;

/**
 * ExifTool execution strategy.
 *
 * <br>
 *
 * For instance:
 * <ul>
 *   <li>Execution using a one-shot process.</li>
 *   <li>
 *     Execution using {@code stay_open} flag: this strategy means that a
 *     process is started and re-used for next executions.
 *   </li>
 * </ul>
 *
 * Each implementation will define the main logic for reading and
 * writing metadata (this is the main purpose for the {@link #execute} method.
 *
 * <br>
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
	 * @throws IOException If an error occurred during execution.
	 */
	void execute(CommandExecutor executor, String exifTool, List<String> arguments, OutputHandler handler) throws IOException;

	/**
	 * Check if exiftool process is currently running.
	 * This method is important especially if {@code stay_open} flag has been enabled.
	 *
	 * @return {@code true} if {@code exiftool} process is currently open, {@code false} otherwise.
	 */
	boolean isRunning();

	/**
	 * Check if this strategy should is supported with this specific version.
	 *
	 * @param version ExifTool Version.
	 * @return {@code true} if this strategy may be used safely with this specific version, {@code false} otherwise.
	 */
	boolean isSupported(Version version);

	/**
	 * This method should be used to:
	 * <ul>
	 *   <li>Close remaining process (if any).</li>
	 *   <li>Clean previous executions.</li>
	 * </ul>
	 *
	 * For instance, with the {@code stay_open} flag, this method should:
	 * <ul>
	 *   <li>Close opened process.</li>
	 *   <li>Stop task used to automatically close process.</li>
	 * </ul>
	 *
	 * Once closed, ExifTool should still be able to use this strategy
	 * if a call to {@link #execute} is made.
	 *
	 * @throws Exception If an error occurred while stopping exiftool client.
	 */
	void close() throws Exception;

	/**
	 * Shutdown the strategy.
	 *
	 * @throws Exception If an error occurred while stopping exiftool client.
	 */
	void shutdown() throws Exception;
}
