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

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggerSlf4jTest {

	private org.slf4j.Logger slf4j;

	@Before
	public void setUp() {
		slf4j = mock(org.slf4j.Logger.class);

		when(slf4j.isDebugEnabled()).thenReturn(true);
		when(slf4j.isInfoEnabled()).thenReturn(true);
		when(slf4j.isWarnEnabled()).thenReturn(true);
		when(slf4j.isErrorEnabled()).thenReturn(true);
	}

	@Test
	public void it_should_display_info() {
		String message = "message";
		LoggerSlf4j logger = new LoggerSlf4j(slf4j);
		logger.info(message);
		verify(slf4j).info(message, new Object[]{ });
	}

	@Test
	public void it_should_display_warn() {
		String message = "message";
		LoggerSlf4j logger = new LoggerSlf4j(slf4j);
		logger.warn(message);
		verify(slf4j).warn(message, new Object[]{ });
	}

	@Test
	public void it_should_display_error() {
		String message = "message";
		LoggerSlf4j logger = new LoggerSlf4j(slf4j);
		logger.error(message);
		verify(slf4j).error(message, new Object[]{ });
	}

	@Test
	public void it_should_display_debug() {
		String message = "message";
		LoggerSlf4j logger = new LoggerSlf4j(slf4j);
		logger.debug(message);
		verify(slf4j).debug(message, new Object[]{ });
	}

	@Test
	public void it_should_display_message_with_parameters() {
		String message = "message {}";
		String parameter = "foo";

		LoggerSlf4j logger = new LoggerSlf4j(slf4j);
		logger.debug(message, "foo");

		verify(slf4j).debug(message, new Object[]{ parameter });
	}

	@Test
	public void it_should_display_message_with_custom_parameters() {
		String message = "message %s %s";
		String parameter = "foo";

		LoggerSlf4j logger = new LoggerSlf4j(slf4j);
		logger.debug(message, "foo");

		verify(slf4j).debug("message {} {}", new Object[]{ parameter });
	}

	@Test
	public void it_should_check_if_debug_is_enabled() {
		when(slf4j.isDebugEnabled()).thenReturn(true);
		LoggerSlf4j logger1 = new LoggerSlf4j(slf4j);
		assertThat(logger1.isDebugEnabled()).isTrue();
		verify(slf4j).isDebugEnabled();
	}
}
