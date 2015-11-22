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

import org.apache.log4j.Level;

import static java.lang.String.format;

/**
 * Implementation of logger using log4j as
 * internal implementation.
 */
public class LoggerLog4j implements Logger {

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
	public void trace(CharSequence message, Object... params) {
		print(Level.TRACE, message, params);
	}

	@Override
	public void info(CharSequence message, Object... params) {
		print(Level.INFO, message, params);
	}

	@Override
	public void debug(CharSequence message, Object... params) {
		print(Level.DEBUG, message, params);
	}

	@Override
	public void warn(CharSequence message, Object... params) {
		print(Level.WARN, message, params);
	}

	@Override
	public void error(CharSequence message, Object... params) {
		print(Level.ERROR, message, params);
	}

	@Override
	public void error(CharSequence message, Throwable ex) {
		if (log.isEnabledFor(Level.ERROR)) {
			log.error(message, ex);
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	private void print(Level level, CharSequence message, Object... params) {
		if (log.isEnabledFor(level)) {
			String msg = message == null ? null : message.toString();
			if (msg != null && params.length > 0) {
				msg = format(msg, params);
			}

			log.log(level, msg);
		}
	}
}
