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

package com.thebuzzmedia.exiftool.core.handlers;

import com.thebuzzmedia.exiftool.process.OutputHandler;

/**
 * Check if line means it is the end of the stream.
 *
 * End is detected if:
 * - Line is null.
 * - Output is strictly equals to "{ready}".
 *
 * This handler is thread safe, stateless and is implemented as
 * a singleton.
 */
public class StopHandler implements OutputHandler {

	/**
	 * Singleton instance.
	 */
	private static final StopHandler INSTANCE = new StopHandler();

	/**
	 * Get instance.
	 *
	 * @return Singleton instance.
	 */
	public static StopHandler stopHandler() {
		return INSTANCE;
	}

	// Ensure non instantiation.
	private StopHandler() {
	}

	@Override
	public boolean readLine(String line) {
		return line != null && !line.equals("{ready}");
	}
}
