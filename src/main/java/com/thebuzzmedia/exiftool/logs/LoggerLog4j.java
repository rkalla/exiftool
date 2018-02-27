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

import org.apache.log4j.Level;

import static com.thebuzzmedia.exiftool.logs.LogUtils.fromSlf4jStyle;

/**
 * Implementation of logger using log4j as
 * internal implementation.
 */
class LoggerLog4j implements Logger {

	/**
	 * Internal Logger.
	 */
	private final org.apache.log4j.Logger log;

	/**
	 * Create logger.
	 * This constructor should be called by {@link com.thebuzzmedia.exiftool.logs.LoggerFactory} only.
	 *
	 * @param log Internal Logger.
	 */
	LoggerLog4j(org.apache.log4j.Logger log) {
		this.log = log;
	}

	@Override
	public void trace(String message) {
		print(Level.TRACE, message);
	}

	@Override
	public void trace(String message, Object p1) {
		print(Level.TRACE, message, p1);
	}

	@Override
	public void trace(String message, Object p1, Object p2) {
		print(Level.TRACE, message, p1, p2);
	}

	@Override
	public void info(String message) {
		print(Level.INFO, message);
	}

	@Override
	public void info(String message, Object p1) {
		print(Level.INFO, message, p1);
	}

	@Override
	public void info(String message, Object p1, Object p2) {
		print(Level.INFO, message, p1, p2);
	}

	@Override
	public void debug(String message) {
		print(Level.DEBUG, message);
	}

	@Override
	public void debug(String message, Object p1) {
		print(Level.DEBUG, message, p1);
	}

	@Override
	public void debug(String message, Object p1, Object p2) {
		print(Level.DEBUG, message, p1, p2);
	}

	@Override
	public void warn(String message) {
		print(Level.WARN, message);
	}

	@Override
	public void warn(String message, Throwable ex) {
		printThrowable(Level.WARN, message, ex);
	}

	@Override
	public void warn(String message, Object p1) {
		print(Level.WARN, message, p1);
	}

	@Override
	public void warn(String message, Object p1, Object p2) {
		print(Level.WARN, message, p1, p2);
	}

	@Override
	public void error(String message) {
		print(Level.ERROR, message);
	}

	@Override
	public void error(String message, Object p1) {
		print(Level.ERROR, message, p1);
	}

	@Override
	public void error(String message, Object p1, Object p2) {
		print(Level.ERROR, message, p1, p2);
	}

	@Override
	public void error(String message, Throwable ex) {
		printThrowable(Level.ERROR, message, ex);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	private void print(Level level, String message) {
		if (log.isEnabledFor(level)) {
			log.log(level, message);
		}
	}

	private void print(Level level, String message, Object p1) {
		if (log.isEnabledFor(level)) {
			log.log(level, String.format(fromSlf4jStyle(message), p1));
		}
	}

	private void print(Level level, String message, Object p1, Object p2) {
		if (log.isEnabledFor(level)) {
			log.log(level, String.format(fromSlf4jStyle(message), p1, p2));
		}
	}

	private void printThrowable(Level level, String message, Throwable ex) {
		if (log.isEnabledFor(level)) {
			log.log(level, message, ex);
		}
	}
}
