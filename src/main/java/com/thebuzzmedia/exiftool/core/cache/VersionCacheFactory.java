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

public final class VersionCacheFactory {

	private VersionCacheFactory() {
	}

	/**
	 * Create new cache for exiftool version.
	 *
	 * @return New instance of {@link VersionCache}.
	 */
	public static VersionCache newCache() {
		// Try Guava first
		if (ClassUtils.isPresent("com.google.common.cache.Cache")) {
			return new GuavaVersionCache();
		}

		return new DefaultVersionCache();
	}
}
