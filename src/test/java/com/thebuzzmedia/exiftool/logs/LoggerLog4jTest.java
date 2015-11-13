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
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggerLog4jTest {

	private org.apache.log4j.Logger log4j;

	@Before
	public void setUp() {
		log4j = mock(org.apache.log4j.Logger.class);
		when(log4j.isEnabledFor(any(Level.class))).thenReturn(true);
	}

	@Test
	public void it_should_display_info() {
		String message = "message";
		LoggerLog4j logger = new LoggerLog4j(log4j);
		logger.info(message);
		verify(log4j).log(Level.INFO, message);
	}

	@Test
	public void it_should_display_warn() {
		String message = "message";
		LoggerLog4j logger = new LoggerLog4j(log4j);
		logger.warn(message);
		verify(log4j).log(Level.WARN, message);
	}

	@Test
	public void it_should_display_error() {
		String message = "message";
		LoggerLog4j logger = new LoggerLog4j(log4j);
		logger.error(message);
		verify(log4j).log(Level.ERROR, message);
	}

	@Test
	public void it_should_display_exception() {
		RuntimeException ex = new RuntimeException("Error Message");
		LoggerLog4j logger = new LoggerLog4j(log4j);
		logger.error(ex.getMessage(), ex);
		verify(log4j).error(ex.getMessage(), ex);
	}

	@Test
	public void it_should_display_debug() {
		String message = "message";
		LoggerLog4j logger = new LoggerLog4j(log4j);
		logger.debug(message);
		verify(log4j).log(Level.DEBUG, message);
	}

	@Test
	public void it_should_not_display_debug_if_is_disabled() {
		when(log4j.isEnabledFor(Level.DEBUG)).thenReturn(false);

		String message = "message";
		LoggerLog4j logger = new LoggerLog4j(log4j);
		logger.debug(message);
		verify(log4j, never()).log(any(Level.class), anyString());
	}

	@Test
	public void it_should_display_message_with_parameters() {
		String message = "message %s";
		String parameter = "foo";

		LoggerLog4j logger = new LoggerLog4j(log4j);
		logger.debug(message, parameter);

		verify(log4j).log(Level.DEBUG, "message foo");
	}

	@Test
	public void it_should_check_if_debug_is_enabled() {
		when(log4j.isDebugEnabled()).thenReturn(true);
		LoggerLog4j logger1 = new LoggerLog4j(log4j);
		assertThat(logger1.isDebugEnabled()).isTrue();
		verify(log4j).isDebugEnabled();
	}
}
