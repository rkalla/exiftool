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

package com.thebuzzmedia.exiftool.logs;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Static Logger Utilities.
 */
final class LogUtils {

	// Ensure non instantiation.
	private LogUtils() {
	}

	/**
	 * Turn SLF4J String to a string that can be used to format messages using {@link String#format(String, Object...)} method.
	 *
	 * @param message Input message.
	 * @return Output message.
	*/
	static String fromSlf4jStyle(String message) {
		return message.replace("{}", "%s");
	}

	/**
	 * Get stacktrace of a given exception as a String.
	 *
	 * @param ex The exception.
	 * @return The associated stacktrace.
	 */
	static String getStackTrace(Throwable ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}
