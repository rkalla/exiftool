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

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.writeStaticPrivateField;
import static org.assertj.core.api.Assertions.assertThat;

import com.thebuzzmedia.exiftool.commons.reflection.DependencyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoggerFactoryTest {

	private String debug;

	@Before
	public void setUp() {
		debug = System.getProperty("exiftool.debug");

		assertThat(DependencyUtils.isSlf4jAvailable()).isTrue();
		assertThat(DependencyUtils.isLog4jAvailable()).isTrue();
	}

	@After
	public void tearDown() throws Exception {
		updateExifToolDebugProperty(debug);
		updateFlagSlf4j(true);
		updateFlagLog4j(true);
	}

	@Test
	public void it_should_get_slf4j_logger() throws Exception {
		updateFlagLog4j(true);
		updateFlagSlf4j(true);

		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

		assertThat(logger)
			.isNotNull()
			.isExactlyInstanceOf(LoggerSlf4j.class);
	}

	@Test
	public void it_should_get_log4j_logger() throws Exception {
		updateFlagSlf4j(false);
		updateFlagLog4j(true);

		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

		assertThat(logger)
			.isNotNull()
			.isExactlyInstanceOf(LoggerLog4j.class);
	}

	@Test
	public void it_should_get_default_logger_with_debug_disabled() throws Exception {
		updateExifToolDebugProperty("false");
		updateFlagLog4j(false);
		updateFlagSlf4j(false);

		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

		assertThat(logger)
			.isNotNull()
			.isExactlyInstanceOf(DefaultLogger.class);

		assertThat(logger.isDebugEnabled()).isFalse();
	}

	@Test
	public void it_should_get_default_logger_with_debug_enabled() throws Exception {
		updateExifToolDebugProperty("true");
		updateFlagLog4j(false);
		updateFlagSlf4j(false);

		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

		assertThat(logger)
			.isNotNull()
			.isExactlyInstanceOf(DefaultLogger.class);

		assertThat(logger.isDebugEnabled()).isTrue();
	}

	private static void updateExifToolDebugProperty(String debug) {
		if (debug != null) {
			System.setProperty("exiftool.debug", debug);
		} else {
			System.clearProperty("exiftool.debug");
		}
	}

	private static void updateFlagSlf4j(boolean value) throws Exception {
		writeStaticPrivateField(DependencyUtils.class, "SLF4J_AVAILABLE", value);
	}

	private static void updateFlagLog4j(boolean value) throws Exception {
		writeStaticPrivateField(DependencyUtils.class, "LOG4J_AVAILABLE", value);
	}
}
