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

package com.thebuzzmedia.exiftool;

import com.thebuzzmedia.exiftool.commons.Mapper;

/**
 * Map all arguments to a valid streaming output.
 * Each argument is terminated with a carriage break.
 * This mapper is thread safe and is implemented as a singleton.
 */
class StreamArgumentMapper implements Mapper<String, String> {

	/**
	 * Singleton instance.
	 */
	private static final StreamArgumentMapper INSTANCE = new StreamArgumentMapper();

	/**
	 * Get instance.
	 *
	 * @return Instance.
	 */
	static StreamArgumentMapper getInstance() {
		return INSTANCE;
	}

	// Ensure non instantiation.
	private StreamArgumentMapper() {
	}

	@Override
	public String map(String input) {
		return input + "\n";
	}
}
