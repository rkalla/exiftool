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

package com.thebuzzmedia.exiftool.logs;

/**
 * Logger interface.
 * Appropriate implementation will be used to log exiftool outputs.
 */
public interface Logger {

	/**
	 * Display information to the console.
	 * Message will be displayed if and only information level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param params Optional message parameters.
	 */
	void info(CharSequence message, Object... params);

	/**
	 * Display debug information to the console.
	 * Message will be displayed if and only debug level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param params Optional message parameters.
	 */
	void debug(CharSequence message, Object... params);

	/**
	 * Display warning information to the console.
	 * Message will be displayed if and only warn level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param params Optional message parameters.
	 */
	void warn(CharSequence message, Object... params);

	/**
	 * Display error information to the console.
	 * Message will be displayed if and only error level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param params Optional message parameters.
	 */
	void error(CharSequence message, Object... params);

	/**
	 * Log exception stack trace as error Level.
	 *
	 * @param message Error message.
	 * @param ex Thrown exception.
	 */
	void error(CharSequence message, Throwable ex);

	/**
	 * Check if debug level is enabled for this logger.
	 *
	 * @return True if debug level is enabled, false otherwise.
	 */
	boolean isDebugEnabled();
}
