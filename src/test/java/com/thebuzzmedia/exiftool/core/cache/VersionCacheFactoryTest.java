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

package com.thebuzzmedia.exiftool.core.cache;

import static org.assertj.core.api.Assertions.assertThat;

import com.thebuzzmedia.exiftool.VersionCache;
import com.thebuzzmedia.exiftool.commons.reflection.DependencyUtils;
import com.thebuzzmedia.exiftool.tests.ReflectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VersionCacheFactoryTest {

	@Before
	public void setUp() {
		assertThat(DependencyUtils.isGuavaAvailable()).isTrue();
	}

	@After
	public void tearDown() throws Exception {
		updateGuavaFlag(true);
	}

	@Test
	public void it_should_load_guava_cache_by_default() throws Exception {
		updateGuavaFlag(true);

		VersionCache cache = VersionCacheFactory.newCache();
		assertThat(cache)
			.isNotNull()
			.isExactlyInstanceOf(GuavaVersionCache.class);
	}

	@Test
	public void it_should_load_default_cache_as_fallback() throws Exception {
		updateGuavaFlag(false);

		VersionCache cache = VersionCacheFactory.newCache();
		assertThat(cache)
			.isNotNull()
			.isExactlyInstanceOf(DefaultVersionCache.class);
	}

	private static void updateGuavaFlag(boolean value) throws Exception {
		ReflectionUtils.writeStaticPrivateField(DependencyUtils.class, "GUAVA_AVAILABLE", value);
	}
}
