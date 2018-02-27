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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractLoggerTest {

	@Test
	public void it_should_display_info() throws Exception {
		String message = "message";
		Logger logger = getLogger();
		logger.info(message);
		verifyInfo(logger, message);
	}

	@Test
	public void it_should_display_info_with_one_parameter() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {}";
		String p1 = "test";
		logger.info(message, p1);
		verifyInfo(logger, message, p1);
	}

	@Test
	public void it_should_display_info_with_two_parameters() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {} // {}";
		String p1 = "t1";
		String p2 = "t2";
		logger.info(message, p1, p2);
		verifyInfo(logger, message, p1, p2);
	}

	@Test
	public void it_should_display_warn() throws Exception {
		String message = "message";
		Logger logger = getLogger();
		logger.warn(message);
		verifyWarn(logger, message);
	}

	@Test
	public void it_should_display_warn_with_one_parameter() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {}";
		String p1 = "test";
		logger.warn(message, p1);
		verifyWarn(logger, message, p1);
	}

	@Test
	public void it_should_display_warn_with_two_parameters() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {} // {}";
		String p1 = "t1";
		String p2 = "t2";
		logger.warn(message, p1, p2);
		verifyWarn(logger, message, p1, p2);
	}

	@Test
	public void it_should_display_error() throws Exception {
		String message = "message";
		Logger logger = getLogger();
		logger.error(message);
		verifyError(logger, message);
	}

	@Test
	public void it_should_display_error_with_one_parameter() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {}";
		String p1 = "test";
		logger.error(message, p1);
		verifyError(logger, message, p1);
	}

	@Test
	public void it_should_display_error_with_two_parameters() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {} // {}";
		String p1 = "t1";
		String p2 = "t2";
		logger.error(message, p1, p2);
		verifyError(logger, message, p1, p2);
	}

	@Test
	public void it_should_display_trace() throws Exception {
		String message = "message";
		Logger logger = getLogger();
		logger.trace(message);
		verifyTrace(logger, message);
	}

	@Test
	public void it_should_display_trace_with_one_parameter() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {}";
		String p1 = "test";
		logger.trace(message, p1);
		verifyTrace(logger, message, p1);
	}

	@Test
	public void it_should_display_trace_with_two_parameters() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {} // {}";
		String p1 = "t1";
		String p2 = "t2";
		logger.trace(message, p1, p2);
		verifyTrace(logger, message, p1, p2);
	}

	@Test
	public void it_should_display_debug() throws Exception {
		String message = "message";
		Logger logger = getLogger();
		logger.debug(message);
		verifyDebug(logger, message);
	}

	@Test
	public void it_should_display_debug_with_one_parameter() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {}";
		String p1 = "test";
		logger.debug(message, p1);
		verifyDebug(logger, message, p1);
	}

	@Test
	public void it_should_display_debug_with_two_parameters() throws Exception {
		Logger logger = getLogger();
		String message = "A message: {} // {}";
		String p1 = "t1";
		String p2 = "t2";
		logger.debug(message, p1, p2);
		verifyDebug(logger, message, p1, p2);
	}

	@Test
	public void it_should_not_display_debug_if_disabled() throws Exception {
		String message = "message";
		Logger loggerWithoutDebug = getLoggerWithoutDebug();
		loggerWithoutDebug.debug(message);
		verifyWithoutDebug(loggerWithoutDebug, message);
	}

	@Test
	public void it_should_check_if_debug_is_enabled() {
		Logger logger1 = getLogger();
		assertThat(logger1.isDebugEnabled()).isTrue();

		Logger logger2 = getLoggerWithoutDebug();
		assertThat(logger2.isDebugEnabled()).isFalse();
	}

	@Test
	public void it_should_display_error_exception() throws Exception {
		RuntimeException ex = new RuntimeException("Error Message");
		Logger logger = getLogger();
		logger.error(ex.getMessage(), ex);
		verifyErrorException(logger, ex.getMessage(), ex);
	}

	@Test
	public void it_should_display_warn_exception() throws Exception {
		RuntimeException ex = new RuntimeException("Error Message");
		Logger logger = getLogger();
		logger.warn(ex.getMessage(), ex);
		verifyWarnException(logger, ex.getMessage(), ex);
	}

	/**
	 * Create default logger to test.
	 *
	 * @return Logger.
	 */
	protected abstract Logger getLogger();

	/**
	 * Create logger, with debug disabled, to test.
	 *
	 * @return Logger.
	 */
	protected abstract Logger getLoggerWithoutDebug();

	/**
	 * Check info log.
	 *
	 * @param logger Tested logger.
	 * @param message Expected message.
	 * @throws Exception
	 */
	protected abstract void verifyInfo(Logger logger, String message, Object... params) throws Exception;

	/**
	 * Check warn log.
	 *
	 * @param logger Tested logger.
	 * @param message Expected message.
	 * @throws Exception
	 */
	protected abstract void verifyWarn(Logger logger, String message, Object... params) throws Exception;

	/**
	 * Check error log.
	 *
	 * @param logger Tested logger.
	 * @param message Expected message.
	 * @throws Exception
	 */
	protected abstract void verifyError(Logger logger, String message, Object... params) throws Exception;

	/**
	 * Check exception log with ERROR level.
	 *
	 * @param logger Tested logger.
	 * @param message Expected message.
	 * @throws Exception
	 */
	protected abstract void verifyErrorException(Logger logger, String message, Exception ex) throws Exception;

	/**
	 * Check exception log with WARN level.
	 *
	 * @param logger Tested logger.
	 * @param message Expected message.
	 * @throws Exception
	 */
	protected abstract void verifyWarnException(Logger logger, String message, Exception ex) throws Exception;

	/**
	 * Check debug log.
	 *
	 * @param logger Tested logger.
	 * @param message Expected message.
	 * @throws Exception
	 */
	protected abstract void verifyDebug(Logger logger, String message, Object... params) throws Exception;

	/**
	 * Check debug log with debug disabled.
	 *
	 * @param logger Tested logger.
	 * @param message Expected message.
	 * @throws Exception
	 */
	protected abstract void verifyWithoutDebug(Logger logger, String message, Object... params) throws Exception;

	/**
	 * Check trace log.
	 *
	 * @param logger Tested logger.
	 * @param message Expected message.
	 * @throws Exception
	 */
	protected abstract void verifyTrace(Logger logger, String message, Object... params) throws Exception;
}
