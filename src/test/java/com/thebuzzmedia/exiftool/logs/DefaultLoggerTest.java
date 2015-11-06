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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultLoggerTest {

	private static final String BR = System.getProperty("line.separator");

	private PrintStream oldOut;

	private ByteArrayOutputStream out;

	@Before
	public void setUp() {
		oldOut = System.out;
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	}

	@After
	public void tearDown() {
		System.out.flush();
		System.setOut(oldOut);
	}

	@Test
	public void it_should_display_info() {
		DefaultLogger logger = new DefaultLogger(true);

		String message = "message";
		logger.info(message);
		System.out.flush();

		assertThat(out.toString())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("[INFO] [exiftool] " + message + BR);
	}

	@Test
	public void it_should_display_warn() {
		DefaultLogger logger = new DefaultLogger(true);

		String message = "message";
		logger.warn(message);
		System.out.flush();

		assertThat(out.toString())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("[WARN] [exiftool] " + message + BR);
	}

	@Test
	public void it_should_display_error() {
		DefaultLogger logger = new DefaultLogger(true);

		String message = "message";
		logger.error(message);
		System.out.flush();

		assertThat(out.toString())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("[ERROR] [exiftool] " + message + BR);
	}

	@Test
	public void it_should_display_debug() {
		DefaultLogger logger = new DefaultLogger(true);

		String message = "message";
		logger.debug(message);
		System.out.flush();

		assertThat(out.toString())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("[DEBUG] [exiftool] " + message + BR);
	}

	@Test
	public void it_should_not_display_debug_if_it_is_disabled() {
		DefaultLogger logger = new DefaultLogger(false);

		String message = "message";
		logger.debug(message);
		System.out.flush();

		assertThat(out.toString())
			.isNotNull()
			.isEmpty();
	}

	@Test
	public void it_should_display_message_with_parameters() {
		DefaultLogger logger = new DefaultLogger(true);

		String message = "message %s";
		logger.debug(message, "foo");
		System.out.flush();

		assertThat(out.toString())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("[DEBUG] [exiftool] message foo" + BR);
	}

	@Test
	public void it_should_check_if_debug_is_enabled() {
		DefaultLogger logger1 = new DefaultLogger(true);
		DefaultLogger logger2 = new DefaultLogger(false);
		assertThat(logger1.isDebugEnabled()).isTrue();
		assertThat(logger2.isDebugEnabled()).isFalse();
	}
}
