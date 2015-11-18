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

package com.thebuzzmedia.exiftool.process.handlers;

import com.thebuzzmedia.exiftool.process.OutputHandler;

/**
 * Simple command handler that just read output line by line
 * and append each one in a {@link StringBuilder} instance.
 * When current line is null, handler will return false.
 *
 * Note that this handler is not thread safe and should be
 * synchronized if needed.
 */
public class ResultHandler implements OutputHandler {

	/**
	 * Line separator.
	 * This constant is system dependent and should be read from
	 * system environment.
	 */
	private static final String BR = System.getProperty("line.separator");

	/**
	 * Current output.
	 */
	private final StringBuilder output;

	/**
	 * Create new handler.
	 */
	public ResultHandler() {
		this.output = new StringBuilder();
	}

	@Override
	public boolean readLine(String line) {
		if (line != null) {
			if (output.length() > 0) {
				output.append(BR);
			}

			output.append(line);
		}

		return line != null;
	}

	/**
	 * Get full output.
	 *
	 * @return Command line output.
	 */
	public String getOutput() {
		return output.toString();
	}
}
