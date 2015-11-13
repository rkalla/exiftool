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
 * Static PreConditions Utilities.
 */
public final class PreConditions {

	// Ensure non instantiation.
	private PreConditions() {
	}

	/**
	 * Ensures that an object reference passed as a parameter to the calling method is not null.
	 *
	 * @param val Value to check.
	 * @param message Message passed to NullPointerException.
	 * @param <T> Type of parameter.
	 * @return Original value if it is not null.
	 * @throws java.lang.NullPointerException If {@code val} is null.
	 */
	public static <T> T notNull(T val, String message) {
		if (val == null) {
			throw new NullPointerException(message);
		}

		return val;
	}

	/**
	 * Ensures that a string is:
	 * - Not null.
	 * - Not empty.
	 * - Not blank (i.e contains at least one character other than space).
	 *
	 * @param val Value to check.
	 * @param message Message passed to thrown exception.
	 * @return Original value if it is not null.
	 * @throws java.lang.NullPointerException If {@code val} is null.
	 * @throws java.lang.IllegalArgumentException If {@code val} is empty or blank.
	 */
	public static String notBlank(String val, String message) {
		notNull(val, message);

		if (val.length() == 0 || val.trim().length() == 0) {
			throw new IllegalArgumentException(message);
		}

		return val;
	}
}
