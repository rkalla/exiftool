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

package com.thebuzzmedia.exiftool.process;

import java.io.IOException;

public interface CommandProcess extends AutoCloseable {

	/**
	 * Read output until a null line is read.
	 *
	 * Since command process will not be closed, a simple string
	 * is returned (an exit status cannot be computed).
	 *
	 * @return Command result.
	 */
	String read();

	/**
	 * Read output until:
	 * - A null line is read.
	 * - Handler returns false when line is read.
	 *
	 * Since command process will not be closed, a simple string
	 * is returned (an exit status cannot be computed).
	 *
	 * @param handler Output handler.
	 * @return Full output.
	 */
	String read(OutputHandler handler);

	/**
	 * Write input string to the current process.
	 * If write operation failed (or is not possible), an instance
	 * of {@link com.thebuzzmedia.exiftool.exceptions.ProcessException} should be thrown.
	 *
	 * @param input Input.
	 * @param others Other inputs.
	 */
	void write(String input, String... others);

	/**
	 * Write set of inputs to the current process.
	 * If write operation failed (or is not possible), an instance
	 * of {@link com.thebuzzmedia.exiftool.exceptions.ProcessException} should be thrown.
	 *
	 * @param inputs Collection of inputs.
	 */
	void write(Iterable<String> inputs);

	/**
	 * Flush pending write operations.
	 */
	void flush() throws IOException;

	/**
	 * Check if current process is still opened.
	 * If this method returns {@code true}, then {@link #isClosed()} should return {@code false}.
	 *
	 * @return True if process is open, false otherwise.
	 */
	boolean isRunning();

	/**
	 * Check if current process has been closed.
	 * If this method returns {@code true}, then {@link #isRunning()} should return {@code false}.
	 *
	 * @return True if process is closed, false otherwise.
	 */
	boolean isClosed();
}
