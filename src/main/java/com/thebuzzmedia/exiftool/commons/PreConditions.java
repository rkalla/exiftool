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

import com.thebuzzmedia.exiftool.exceptions.UnreadableFileException;
import com.thebuzzmedia.exiftool.exceptions.UnwritableFileException;

import java.io.File;
import java.util.Map;

import static java.lang.String.format;

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
	 * @param params Message parameters (formatted with {@link String#format(String, Object...)}).
	 * @param <T> Type of parameter.
	 * @return Original value if it is not null.
	 * @throws java.lang.NullPointerException If {@code val} is null.
	 */
	public static <T> T notNull(T val, String message, Object... params) {
		if (val == null) {
			String msg = params.length > 0 ? format(message, params) : message;
			throw new NullPointerException(msg);
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
	 * @param params Message parameters (formatted with {@link String#format(String, Object...)}).
	 * @return Original value if it is not null.
	 * @throws java.lang.NullPointerException If {@code val} is null.
	 * @throws java.lang.IllegalArgumentException If {@code val} is empty or blank.
	 */
	public static String notBlank(String val, String message, Object... params) {
		notNull(val, message, params);

		if (val.length() == 0 || val.trim().length() == 0) {
			String msg = params.length > 0 ? format(message, params) : message;
			throw new IllegalArgumentException(msg);
		}

		return val;
	}

	/**
	 * Ensures that array is:
	 * - Not null.
	 * - Not empty.
	 *
	 * @param val Value to check.
	 * @param message Message passed to thrown exception.
	 * @param params Message parameters (formatted with {@link String#format(String, Object...)}).
	 * @return Original value if it is not null.
	 * @throws java.lang.NullPointerException If {@code val} is null.
	 * @throws java.lang.IllegalArgumentException If {@code val} is empty.
	 */
	public static <T> T[] notEmpty(T[] val, String message, Object... params) {
		notNull(val, message, params);

		if (val.length == 0) {
			String msg = params.length > 0 ? format(message, params) : message;
			throw new IllegalArgumentException(msg);
		}

		return val;
	}

	/**
	 * Ensures that map is:
	 * - Not null.
	 * - Not empty.
	 *
	 * @param val Value to check.
	 * @param message Message passed to thrown exception.
	 * @param params Message parameters (formatted with {@link String#format(String, Object...)}).
	 * @return Original value if it is not null.
	 * @throws java.lang.NullPointerException If {@code val} is null.
	 * @throws java.lang.IllegalArgumentException If {@code val} is empty.
	 */
	public static <T, U> Map<T, U> notEmpty(Map<T, U> val, String message, Object... params) {
		notNull(val, message, params);

		if (val.size() == 0) {
			String msg = params.length > 0 ? format(message, params) : message;
			throw new IllegalArgumentException(msg);
		}

		return val;
	}

	/**
	 * Check that a given file exist and is readable.
	 *
	 * @param file File to check.
	 * @param message Error message.
	 * @param params Message parameters (formatted with {@link String#format(String, Object...)}).
	 * @return Original file.
	 * @throws NullPointerException If {@code file} is null.
	 * @throws UnreadableFileException If {@code file} does not exist.
	 * @throws UnreadableFileException If {@code file} cannot be read.
	 */
	public static File isReadable(File file, String message, Object... params) {
		notNull(file, message, params);

		if (!file.exists() || !file.canRead()) {
			String msg = params.length > 0 ? format(message, params) : message;
			throw new UnreadableFileException(file, msg);
		}

		return file;
	}

	/**
	 * Check that a given file exist and is writable.
	 *
	 * @param file File to check.
	 * @param message Error message.
	 * @param params Message parameters (formatted with {@link String#format(String, Object...)}).
	 * @return Original file.
	 * @throws NullPointerException If {@code file} is null.
	 * @throws UnreadableFileException If {@code file} does not exist.
	 * @throws UnreadableFileException If {@code file} cannot be updated.
	 */
	public static File isWritable(File file, String message, Object... params) {
		notNull(file, message, params);

		if (!file.exists() || !file.canWrite()) {
			String msg = params.length > 0 ? format(message, params) : message;
			throw new UnwritableFileException(file, msg);
		}

		return file;
	}
}
