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
 * Implementation of logger using slf4j as
 * internal implementation.
 */
public class LoggerSlf4j implements Logger {

	/**
	 * Internal Logger.
	 */
	private final org.slf4j.Logger log;

	/**
	 * Create logger.
	 * This constructor should be called by {@link com.thebuzzmedia.exiftool.logs.LoggerFactory} only.
	 *
	 * @param log Internal Logger.
	 */
	LoggerSlf4j(org.slf4j.Logger log) {
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
		String msg = message == null ? null : message.toString();
		log.error(msg, ex);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	private void print(Level level, CharSequence message, Object... params) {
		if (level.isEnabled(log)) {
			String str = message == null ? null : message.toString();
			if (str != null && params.length > 0) {
				str = str.replaceAll("%s", "{}");
			}

			level.log(log, str, params);
		}
	}

	private static enum Level {
		TRACE {
			@Override
			boolean isEnabled(org.slf4j.Logger log) {
				return log.isTraceEnabled();
			}

			@Override
			void log(org.slf4j.Logger log, String str, Object... params) {
				log.trace(str, params);
			}
		},

		DEBUG {
			@Override
			boolean isEnabled(org.slf4j.Logger log) {
				return log.isDebugEnabled();
			}

			@Override
			void log(org.slf4j.Logger log, String str, Object... params) {
				log.debug(str, params);
			}
		},

		INFO {
			@Override
			boolean isEnabled(org.slf4j.Logger log) {
				return log.isInfoEnabled();
			}

			@Override
			void log(org.slf4j.Logger log, String str, Object... params) {
				log.info(str, params);
			}
		},

		WARN {
			@Override
			boolean isEnabled(org.slf4j.Logger log) {
				return log.isWarnEnabled();
			}

			@Override
			void log(org.slf4j.Logger log, String str, Object... params) {
				log.warn(str, params);
			}
		},

		ERROR {
			@Override
			boolean isEnabled(org.slf4j.Logger log) {
				return log.isErrorEnabled();
			}

			@Override
			void log(org.slf4j.Logger log, String str, Object... params) {
				log.error(str, params);
			}
		};

		abstract boolean isEnabled(org.slf4j.Logger log);

		abstract void log(org.slf4j.Logger log, String str, Object... params);
	}
}
