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

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

		int nbParams = params.length;
		if (nbParams == 0) {
			verify(slf4j).info(message);
		} else if (nbParams == 1) {
			verify(slf4j).info(message, params[0]);
		} else if (nbParams == 2) {
			verify(slf4j).info(message, params[0], params[1]);
		} else {
			throw new AssertionError("Invalid number of parameters");
		}
	}

	@Override
	protected void verifyWarn(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);

		int nbParams = params.length;
		if (nbParams == 0) {
			verify(slf4j).warn(message);
		} else if (nbParams == 1) {
			verify(slf4j).warn(message, params[0]);
		} else if (nbParams == 2) {
			verify(slf4j).warn(message, params[0], params[1]);
		} else {
			throw new AssertionError("Invalid number of parameters");
		}
	}

	@Override
	protected void verifyError(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);

		int nbParams = params.length;
		if (nbParams == 0) {
			verify(slf4j).error(message);
		} else if (nbParams == 1) {
			verify(slf4j).error(message, params[0]);
		} else if (nbParams == 2) {
			verify(slf4j).error(message, params[0], params[1]);
		} else {
			throw new AssertionError("Invalid number of parameters");
		}
	}

	@Override
	protected void verifyErrorException(Logger logger, String message, Exception ex) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j).error(message, ex);
	}

	@Override
	protected void verifyWarnException(Logger logger, String message, Exception ex) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j).warn(message, ex);
	}

	@Override
	protected void verifyDebug(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);

		int nbParams = params.length;
		if (nbParams == 0) {
			verify(slf4j).debug(message);
		} else if (nbParams == 1) {
			verify(slf4j).debug(message, params[0]);
		} else if (nbParams == 2) {
			verify(slf4j).debug(message, params[0], params[1]);
		} else {
			throw new AssertionError("Invalid number of parameters");
		}
	}

	@Override
	protected void verifyWithoutDebug(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);
		verify(slf4j, never()).debug(anyString(), any(Object[].class));
	}

	@Override
	protected void verifyTrace(Logger logger, String message, Object... params) throws Exception {
		org.slf4j.Logger slf4j = getSlf4j(logger);

		int nbParams = params.length;
		if (nbParams == 0) {
			verify(slf4j).trace(message);
		} else if (nbParams == 1) {
			verify(slf4j).trace(message, params[0]);
		} else if (nbParams == 2) {
			verify(slf4j).trace(message, params[0], params[1]);
		} else {
			throw new AssertionError("Invalid number of parameters");
		}
	}

	private static org.slf4j.Logger getSlf4j(Logger logger) throws Exception {
		return readPrivateField(logger, "log");
	}

	private static String toSlf4jMessage(String msg) {
		return msg != null ? msg.replace("%s", "{}") : msg;
	}
}
