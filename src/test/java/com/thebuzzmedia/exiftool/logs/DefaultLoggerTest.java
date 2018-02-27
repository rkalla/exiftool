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

import com.thebuzzmedia.exiftool.tests.junit.SystemOutRule;
import org.junit.Rule;

import static com.thebuzzmedia.exiftool.tests.TestConstants.BR;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultLoggerTest extends AbstractLoggerTest {

	@Rule
	public SystemOutRule systemOutRule = new SystemOutRule();

	@Override
	protected Logger getLogger() {
		return new DefaultLogger(true);
	}

	@Override
	protected Logger getLoggerWithoutDebug() {
		return new DefaultLogger(false);
	}

	@Override
	protected void verifyInfo(Logger logger, String message, Object... params) {
		verifyCall("INFO", message, params);
	}

	@Override
	protected void verifyWarn(Logger logger, String message, Object... params) {
		verifyCall("WARN", message, params);
	}

	@Override
	protected void verifyError(Logger logger, String message, Object... params) {
		verifyCall("ERROR", message, params);
	}

	@Override
	protected void verifyErrorException(Logger logger, String message, Exception ex) {
		verifyException("ERROR", message, ex);
	}

	@Override
	protected void verifyWarnException(Logger logger, String message, Exception ex) {
		verifyException("WARN", message, ex);
	}

	@Override
	protected void verifyDebug(Logger logger, String message, Object... params) {
		verifyCall("DEBUG", message, params);
	}

	@Override
	protected void verifyWithoutDebug(Logger logger, String message, Object... params) {
		assertThat(systemOutRule.getPendingOut()).isNotNull().isEmpty();
	}

	@Override
	protected void verifyTrace(Logger logger, String message, Object... params) {
		verifyCall("TRACE", message, params);
	}

	private void verifyException(String level, String message, Exception ex) {
		String display = systemOutRule.getPendingOut();
		String[] lines = display.split(BR);

		assertThat(lines).isNotEmpty();
		assertThat(lines.length).isGreaterThanOrEqualTo(2);
		assertThat(lines[0]).isNotNull().isNotEmpty().isEqualTo("[" + level + "] [exiftool] " + message);
		assertThat(lines[1]).isNotNull().isNotEmpty().isEqualTo(ex.getClass().getName() + ": " + ex.getMessage());
	}

	private void verifyCall(String level, String message, Object... params) {
		String template = toStringFormatMessage(message);
		String msg = String.format(template, params);
		String expectedMessage = String.format("[%s] [exiftool] %s" + BR, level, msg);
		assertThat(systemOutRule.getPendingOut())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(expectedMessage);
	}

	private String toStringFormatMessage(String message) {
		return message.replace("{}", "%s");
	}
}
