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
	protected void verifyInfo(Logger logger, String message, Object... params) throws Exception {
		verifyCall("INFO", message, params);
	}

	@Override
	protected void verifyWarn(Logger logger, String message, Object... params) throws Exception {
		verifyCall("WARN", message, params);
	}

	@Override
	protected void verifyError(Logger logger, String message, Object... params) throws Exception {
		verifyCall("ERROR", message, params);
	}

	@Override
	protected void verifyException(Logger logger, String message, Exception ex) throws Exception {
		String display = systemOutRule.getPendingOut();
		String[] lines = display.split(BR);

		assertThat(lines[0])
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("[ERROR] [exiftool] " + message);

		assertThat(lines[1])
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("[ERROR] [exiftool] " + ex.getClass().getName() + ": " + ex.getMessage());
	}

	@Override
	protected void verifyDebug(Logger logger, String message, Object... params) throws Exception {
		verifyCall("DEBUG", message, params);
	}

	@Override
	protected void verifyWithoutDebug(Logger logger, String message, Object... params) throws Exception {
		assertThat(systemOutRule.getPendingOut())
			.isNotNull()
			.isEmpty();
	}

	@Override
	protected void verifyTrace(Logger logger, String message, Object... params) throws Exception {
		verifyCall("TRACE", message, params);
	}

	private void verifyCall(String level, String message, Object... params) {
		String msg = message == null ? null : String.format(message, params);
		String expectedMessage = String.format("[%s] [exiftool] %s" + BR, level, msg);
		assertThat(systemOutRule.getPendingOut())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(expectedMessage);
	}
}
