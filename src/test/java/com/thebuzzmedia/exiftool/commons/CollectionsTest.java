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

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CollectionsTest {

	@Test
	public void it_should_check_if_collection_is_empty() {
		assertThat(Collections.isEmpty(null)).isTrue();
		assertThat(Collections.isEmpty(java.util.Collections.emptyList())).isTrue();
		assertThat(Collections.isEmpty(asList(1, 2, 3))).isFalse();
	}

	@Test
	public void it_should_get_size_of_collection() {
		assertThat(Collections.size(null)).isZero();
		assertThat(Collections.size(java.util.Collections.emptyList())).isZero();
		assertThat(Collections.size(asList(1, 2, 3))).isEqualTo(3);
	}

	@Test
	public void it_should_join_collection() {
		assertThat(Collections.join(null, " ")).isEqualTo("");
		assertThat(Collections.join(java.util.Collections.emptyList(), " ")).isEqualTo("");
		assertThat(Collections.join(asList("foo"), " ")).isEqualTo("foo");
		assertThat(Collections.join(asList("foo", "bar"), " ")).isEqualTo("foo bar");
	}
}
