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

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggerLog4jTest extends AbstractLoggerTest {

	@Override
	protected Logger getLogger() {
		org.apache.log4j.Logger log4j = mock(org.apache.log4j.Logger.class);
		when(log4j.isDebugEnabled()).thenReturn(true);
		when(log4j.isInfoEnabled()).thenReturn(true);

		when(log4j.isEnabledFor(Level.TRACE)).thenReturn(true);
		when(log4j.isEnabledFor(Level.DEBUG)).thenReturn(true);
		when(log4j.isEnabledFor(Level.INFO)).thenReturn(true);
		when(log4j.isEnabledFor(Level.WARN)).thenReturn(true);
		when(log4j.isEnabledFor(Level.ERROR)).thenReturn(true);

		return new LoggerLog4j(log4j);
	}

	@Override
	protected Logger getLoggerWithoutDebug() {
		org.apache.log4j.Logger log4j = mock(org.apache.log4j.Logger.class);
		when(log4j.isDebugEnabled()).thenReturn(false);
		when(log4j.isInfoEnabled()).thenReturn(true);

		when(log4j.isEnabledFor(Level.TRACE)).thenReturn(true);
		when(log4j.isEnabledFor(Level.DEBUG)).thenReturn(false);
		when(log4j.isEnabledFor(Level.INFO)).thenReturn(true);
		when(log4j.isEnabledFor(Level.WARN)).thenReturn(true);
		when(log4j.isEnabledFor(Level.ERROR)).thenReturn(true);

		return new LoggerLog4j(log4j);
	}

	@Override
	protected void verifyInfo(Logger logger, String message, Object... params) throws Exception {
		verifyCall(logger, Level.INFO, message, params);
	}

	@Override
	protected void verifyWarn(Logger logger, String message, Object... params) throws Exception {
		verifyCall(logger, Level.WARN, message, params);
	}

	@Override
	protected void verifyError(Logger logger, String message, Object... params) throws Exception {
		verifyCall(logger, Level.ERROR, message, params);
	}

	@Override
	protected void verifyException(Logger logger, String message, Exception ex) throws Exception {
		org.apache.log4j.Logger log4j = getLog4j(logger);
		verify(log4j).isEnabledFor(Level.ERROR);
		verify(log4j).error(message, ex);
	}

	@Override
	protected void verifyDebug(Logger logger, String message, Object... params) throws Exception {
		verifyCall(logger, Level.DEBUG, message, params);
	}

	@Override
	protected void verifyWithoutDebug(Logger logger, String message, Object... params) throws Exception {
		verify(getLog4j(logger), never()).debug(message);
	}

	@Override
	protected void verifyTrace(Logger logger, String message, Object... params) throws Exception {
		verifyCall(logger, Level.TRACE, message, params);
	}

	private void verifyCall(Logger logger, Level level, String message, Object... params) throws Exception {
		String msg = message;
		if (msg != null && params.length > 0) {
			msg = String.format(msg, params);
		}

		org.apache.log4j.Logger log4j = getLog4j(logger);
		verify(log4j).isEnabledFor(level);
		verify(log4j).log(level, msg);
	}

	private org.apache.log4j.Logger getLog4j(Logger logger) throws Exception {
		return readPrivateField(logger, "log", org.apache.log4j.Logger.class);
	}
}
