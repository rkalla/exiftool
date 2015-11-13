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

package com.thebuzzmedia.exiftool.process;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest {

	@Test
	public void it_should_create_result() {
		String output = "foo";
		Result result = new Result(0, output);
		assertThat(result.getExitStatus()).isZero();
		assertThat(result.getOutput()).isEqualTo(output);
	}

	@Test
	public void it_should_mark_result_as_success() {
		Result result = new Result(0, "foo");
		assertThat(result.isSuccess()).isTrue();
	}

	@Test
	public void it_should_mark_result_as_failure() {
		Result result = new Result(1, "foo");
		assertThat(result.isSuccess()).isFalse();
	}

	@Test
	public void it_should_implement_equals() {
		Result r1 = new Result(0, "");
		Result r2 = new Result(0, "");
		Result r3 = new Result(0, "");
		Result r4 = new Result(1, "");

		assertThat(r1).isNotEqualTo(r4);
		assertThat(r1).isNotEqualTo(new Object());
		assertThat(r1).isEqualTo(r1);

		assertThat(r1.equals(r2)).isTrue();
		assertThat(r2.equals(r1)).isTrue();

		assertThat(r1.equals(r2)).isTrue();
		assertThat(r2.equals(r3)).isTrue();
		assertThat(r1.equals(r3)).isTrue();
	}

	@Test
	public void it_should_implement_hashCode() {
		Result r1 = new Result(0, "");
		Result r2 = new Result(0, "");
		assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
	}

	@Test
	public void it_should_implement_toString() {
		Result r1 = new Result(0, "foo");
		assertThat(r1.toString()).isEqualTo("[0] foo");
	}
}
