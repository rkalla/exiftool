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

package com.thebuzzmedia.exiftool.commons;

/**
 * Static Objects Utilities.
 */
public final class Objects {

	// Ensure non instantiation.
	private Objects() {
	}

	/**
	 * Returns the first of two given parameters that is not null.
	 *
	 * @param val1 First value.
	 * @param val2 Second value.
	 * @param <T> Type of parameters.
	 * @return First parameter if it is not null, second parameter otherwise.
	 */
	public static <T> T firstNonNull(T val1, T val2) {
		return val1 != null ? val1 : val2;
	}
}
