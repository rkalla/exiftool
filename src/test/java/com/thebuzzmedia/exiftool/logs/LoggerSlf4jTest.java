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

import java.lang.reflect.Field;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggerSlf4jTest extends AbstractLoggerTest {

	@Override
	protected Logger getLogger() {
		org.slf4j.Logger slf4j = mock(org.slf4j.Logger.class);

		when(slf4j.isTraceEnabled()).thenReturn(true);
		when(slf4j.isDebugEnabled()).thenReturn(true);
		when(slf4j.isInfoEnabled()).thenReturn(true);
		when(slf4j.isWarnEnabled()).thenReturn(true);
		when(slf4j.isErrorEnabled()).thenReturn(true);

		return new LoggerSlf4j(slf4j);
	}

	@Override
	protected Logger getLoggerWithoutDebug() {
		org.slf4j.Logger slf4j = mock(org.slf4j.Logger.class);

		when(slf4j.isTraceEnabled()).thenReturn(false);
		when(slf4j.isDebugEnabled()).thenReturn(false);
		when(slf4j.isInfoEnabled()).thenReturn(true);
		when(slf4j.isWarnEnabled()).thenReturn(true);
		when(slf4j.isErrorEnabled()).thenReturn(true);

		return new LoggerSlf4j(slf4j);
	}

	@Override
	protected void verifyInfo(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j).info(toSlf4jMessage(message), params);
	}

	@Override
	protected void verifyWarn(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j).warn(toSlf4jMessage(message), params);
	}

	@Override
	protected void verifyError(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j).error(toSlf4jMessage(message), params);
	}

	@Override
	protected void verifyException(Logger logger, String message, Exception ex) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j).error(message, ex);
	}

	@Override
	protected void verifyDebug(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j).debug(toSlf4jMessage(message), params);
	}

	@Override
	protected void verifyWithoutDebug(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j, never()).debug(anyString(), any(Object[].class));
	}

	@Override
	protected void verifyTrace(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j).trace(toSlf4jMessage(message), params);
	}

	private org.slf4j.Logger getSlf4j(Logger logger) throws Exception {
		Field field = logger.getClass().getDeclaredField("log");
		field.setAccessible(true);
		return (org.slf4j.Logger) field.get(logger);
	}

	private String toSlf4jMessage(String msg) {
		return msg != null ? msg.replace("%s", "{}") : msg;
	}
}
