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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.thebuzzmedia.exiftool.Version;
import com.thebuzzmedia.exiftool.VersionCache;
import com.thebuzzmedia.exiftool.process.CommandExecutor;

import java.util.concurrent.ExecutionException;

import static com.thebuzzmedia.exiftool.commons.exceptions.Exceptions.launderThrowable;

/**
 * Implementation of {@link VersionCache} using Guava as internal
 * implementation.
 */
class GuavaVersionCache implements VersionCache {

	/**
	 * Guava cache implementation.
	 * Each value will be loaded using a callable task (we cannot use
	 * a default loader implementation).
	 */
	private final Cache<String, Version> cache;

	/**
	 * Create Guava Cache.
	 */
	GuavaVersionCache() {
		this.cache = CacheBuilder.newBuilder()
			.build();
	}

	@Override
	public Version load(String exifTool, CommandExecutor executor) {
		try {
			return cache.get(exifTool, new VersionCallable(exifTool, executor));
		}
		catch (ExecutionException ex) {
			throw launderThrowable(ex.getCause());
		}
	}

	@Override
	public void clear() {
		cache.invalidateAll();
	}

	@Override
	public long size() {
		return cache.size();
	}
}
