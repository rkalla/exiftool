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

import com.thebuzzmedia.exiftool.commons.reflection.ClassUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClassUtils.class)
public class LoggerFactoryTest {

	private String debug;

	@Before
	public void setUp() {
		debug = System.getProperty("exiftool.debug");

		mockStatic(ClassUtils.class);
		when(ClassUtils.isPresent("org.slf4j.Logger")).thenReturn(true);
		when(ClassUtils.isPresent("org.apache.log4j.Logger")).thenReturn(true);
	}

	@After
	public void tearDown() {
		if (debug != null) {
			System.setProperty("exiftool.debug", debug);
		} else {
			System.clearProperty("exiftool.debug");
		}
	}

	@Test
	public void it_should_get_slf4j_logger() {
		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

		assertThat(logger)
			.isNotNull()
			.isExactlyInstanceOf(LoggerSlf4j.class);
	}

	@Test
	public void it_should_get_log4j_logger() {
		when(ClassUtils.isPresent("org.slf4j.Logger")).thenReturn(false);
		when(ClassUtils.isPresent("org.apache.log4j.Logger")).thenReturn(true);

		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

		assertThat(logger)
			.isNotNull()
			.isExactlyInstanceOf(LoggerLog4j.class);
	}

	@Test
	public void it_should_get_default_logger_with_debug_disabled() {
		System.setProperty("exiftool.debug", "false");

		when(ClassUtils.isPresent("org.slf4j.Logger")).thenReturn(false);
		when(ClassUtils.isPresent("org.apache.log4j.Logger")).thenReturn(false);

		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

		assertThat(logger)
			.isNotNull()
			.isExactlyInstanceOf(DefaultLogger.class);

		assertThat(logger.isDebugEnabled()).isFalse();
	}

	@Test
	public void it_should_get_default_logger_with_debug_enabled() {
		System.setProperty("exiftool.debug", "true");

		when(ClassUtils.isPresent("org.slf4j.Logger")).thenReturn(false);
		when(ClassUtils.isPresent("org.apache.log4j.Logger")).thenReturn(false);

		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

		assertThat(logger)
			.isNotNull()
			.isExactlyInstanceOf(DefaultLogger.class);

		assertThat(logger.isDebugEnabled()).isTrue();
	}
}
