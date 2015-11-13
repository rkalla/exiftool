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

package com.thebuzzmedia.exiftool.commons;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

public class PreConditionsTest {

	@Rule
	public ExpectedException thrown = none();

	@Test
	public void it_should_fail_with_npe() {
		String message = "should not be null";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);

		PreConditions.notNull(null, message);
	}

	@Test
	public void it_should_not_fail_with_npe() {
		String val = "foo";
		String foo = PreConditions.notNull(val, "should not be null");
		assertThat(foo)
			.isNotNull()
			.isEqualTo(val);
	}

	@Test
	public void it_should_fail_with_null_string() {
		String message = "should not be empty";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);

		PreConditions.notBlank(null, message);
	}

	@Test
	public void it_should_fail_with_empty_string() {
		String message = "should not be empty";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);

		PreConditions.notBlank("", message);
	}

	@Test
	public void it_should_fail_with_blank_string() {
		String message = "should not be empty";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);

		PreConditions.notBlank("  ", message);
	}

	@Test
	public void it_should_not_fail_with_valid_string() {
		String message = "should not be empty";
		String val = "foo";

		String result = PreConditions.notBlank(val, message);

		assertThat(result)
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(val);
	}
}
