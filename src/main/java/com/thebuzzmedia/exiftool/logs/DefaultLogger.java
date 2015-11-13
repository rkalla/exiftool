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

import java.io.PrintWriter;
import java.io.StringWriter;

import static java.lang.String.format;

/**
 * Default logger implementation.
 * This implementation should be used if and only if no
 * external logging tool is available.
 *
 * This logger will only log to the standard output.
 *
 * Log level will be detected using `exiftool.debug` system property.
 * This system property can be set on startup with:
 * - -Dexiftool.debug=true
 * - or by calling {@link System#setProperty(String, String)} before this class is loaded.
 *
 * Default value is `false`.
 */
public class DefaultLogger implements Logger {

	private static enum Level {
		DEBUG, INFO, WARN, ERROR
	}

	/**
	 * Logger level.
	 */
	private final Level level;

	DefaultLogger(boolean debug) {
		this.level = debug ? Level.DEBUG : Level.INFO;
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
	public boolean isDebugEnabled() {
		return isEnabled(Level.DEBUG);
	}

	@Override
	public void error(CharSequence message, Throwable ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));

		print(Level.ERROR, message);
		print(Level.ERROR, errors.toString());
	}

	private boolean isEnabled(Level level) {
		return this.level.compareTo(level) <= 0;
	}

	private void print(Level level, CharSequence message, Object... params) {
		if (isEnabled(level)) {
			String str = message.toString();
			if (params.length > 0) {
				str = format(str, params);
			}

			System.out.println(format("[" + level + "] [exiftool] " + str));
		}
	}
}
