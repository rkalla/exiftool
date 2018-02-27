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
class LoggerSlf4j implements Logger {

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
	public void trace(String message) {
		this.log.trace(message);
	}

	@Override
	public void trace(String message, Object p1) {
		this.log.trace(message, p1);
	}

	@Override
	public void trace(String message, Object p1, Object p2) {
		this.log.trace(message, p1, p2);
	}

	@Override
	public void info(String message) {
		this.log.info(message);
	}

	@Override
	public void info(String message, Object p1) {
		this.log.info(message, p1);
	}

	@Override
	public void info(String message, Object p1, Object p2) {
		this.log.info(message, p1, p2);
	}

	@Override
	public void debug(String message) {
		this.log.debug(message);
	}

	@Override
	public void debug(String message, Object p1) {
		this.log.debug(message, p1);
	}

	@Override
	public void debug(String message, Object p1, Object p2) {
		this.log.debug(message, p1, p2);
	}

	@Override
	public void warn(String message) {
		this.log.warn(message);
	}

	@Override
	public void warn(String message, Throwable ex) {
		this.log.warn(message, ex);
	}

	@Override
	public void warn(String message, Object p1) {
		this.log.warn(message, p1);
	}

	@Override
	public void warn(String message, Object p1, Object p2) {
		this.log.warn(message, p1, p2);
	}

	@Override
	public void error(String message) {
		this.log.error(message);
	}

	@Override
	public void error(String message, Object p1) {
		this.log.error(message, p1);
	}

	@Override
	public void error(String message, Object p1, Object p2) {
		this.log.error(message, p1, p2);
	}

	@Override
	public void error(String message, Throwable ex) {
		log.error(message, ex);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}
}
