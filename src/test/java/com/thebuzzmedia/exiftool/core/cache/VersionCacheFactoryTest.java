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

import com.thebuzzmedia.exiftool.VersionCache;
import com.thebuzzmedia.exiftool.commons.reflection.ClassUtils;
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
public class VersionCacheFactoryTest {

	@Before
	public void setUp() {
		mockStatic(ClassUtils.class);
	}

	@Test
	public void it_should_load_guava_cache_by_default() {
		when(ClassUtils.isPresent("com.google.common.cache.Cache")).thenReturn(true);

		VersionCache cache = VersionCacheFactory.newCache();
		assertThat(cache)
			.isNotNull()
			.isExactlyInstanceOf(GuavaVersionCache.class);
	}

	@Test
	public void it_should_load_default_cache_as_fallback() {
		when(ClassUtils.isPresent("com.google.common.cache.Cache")).thenReturn(false);

		VersionCache cache = VersionCacheFactory.newCache();
		assertThat(cache)
			.isNotNull()
			.isExactlyInstanceOf(DefaultVersionCache.class);
	}
}
