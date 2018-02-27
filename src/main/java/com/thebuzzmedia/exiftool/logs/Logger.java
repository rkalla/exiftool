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

/**
 * Logger interface.
 * Appropriate implementation will be used to log exiftool outputs.
 */
public interface Logger {

	/**
	 * Display trace to the console.
	 * Message will be displayed if and only trace level
	 * is enabled.
	 *
	 * @param message Message to display.
	 */
	void trace(String message);

	/**
	 * Display trace to the console.
	 * Message will be displayed if and only trace level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 Message parameter.
	 */
	void trace(String message, Object p1);

	/**
	 * Display trace to the console.
	 * Message will be displayed if and only trace level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 First message parameter.
	 * @param p2 Second message parameter.
	 */
	void trace(String message, Object p1, Object p2);

	/**
	 * Display information to the console.
	 * Message will be displayed if and only information level
	 * is enabled.
	 *
	 * @param message Message to display.
	 */
	void info(String message);

	/**
	 * Display information to the console.
	 * Message will be displayed if and only information level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 First message parameter.
	 */
	void info(String message, Object p1);

	/**
	 * Display information to the console.
	 * Message will be displayed if and only information level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 First message parameter.
	 * @param p2 Second message parameter.
	 */
	void info(String message, Object p1, Object p2);

	/**
	 * Display debug information to the console.
	 * Message will be displayed if and only debug level
	 * is enabled.
	 *
	 * @param message Message to display.
	 */
	void debug(String message);

	/**
	 * Display debug information to the console.
	 * Message will be displayed if and only debug level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 Optional message parameter.
	 */
	void debug(String message, Object p1);

	/**
	 * Display debug information to the console.
	 * Message will be displayed if and only debug level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 Optional (first) message parameter.
	 * @param p2 Optional (second) message parameter.
	 */
	void debug(String message, Object p1, Object p2);

	/**
	 * Display warning information to the console.
	 * Message will be displayed if and only warn level
	 * is enabled.
	 *
	 * @param message Message to display.
	 */
	void warn(String message);

	/**
	 * Display warning information to the console.
	 * Message will be displayed if and only warn level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param ex The exception to log.
	 */
	void warn(String message, Throwable ex);

	/**
	 * Display warning information to the console.
	 * Message will be displayed if and only warn level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 First message parameter.
	 */
	void warn(String message, Object p1);

	/**
	 * Display warning information to the console.
	 * Message will be displayed if and only warn level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 First message parameter.
	 * @param p2 Second message parameter.
	 */
	void warn(String message, Object p1, Object p2);

	/**
	 * Display error information to the console.
	 * Message will be displayed if and only error level
	 * is enabled.
	 *
	 * @param message Message to display.
	 */
	void error(String message);

	/**
	 * Display error information to the console.
	 * Message will be displayed if and only error level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 First message parameter.
	 */
	void error(String message, Object p1);

	/**
	 * Display error information to the console.
	 * Message will be displayed if and only error level
	 * is enabled.
	 *
	 * @param message Message to display.
	 * @param p1 First message parameter.
	 * @param p2 Second message parameter.
	 */
	void error(String message, Object p1, Object p2);

	/**
	 * Log exception stack trace as error Level.
	 *
	 * @param message Error message.
	 * @param ex Thrown exception.
	 */
	void error(String message, Throwable ex);

	/**
	 * Check if debug level is enabled for this logger.
	 *
	 * @return {@code true} if debug level is enabled, {@code false} otherwise.
	 */
	boolean isDebugEnabled();
}
