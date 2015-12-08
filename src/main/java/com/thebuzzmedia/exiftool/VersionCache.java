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

package com.thebuzzmedia.exiftool;

import com.thebuzzmedia.exiftool.process.CommandExecutor;

/**
 * Parse {@code exiftool} version and put it in a cache.
 */
public interface VersionCache {

	/**
	 * Execute {@code exiftool} using given {@code executor} to get version
	 * of {@code exiftool} executable and put results in a cache.
	 *
	 * @param exifTool Path of {@code exiftool} executable.
	 * @param executor Executor used to execute {@code exiftool} command.
	 * @return Version, {@code null} if version cannot be parsed.
	 */
	Version load(String exifTool, CommandExecutor executor);

	/**
	 * Get current size of cache (a.k.a number of entries).
	 *
	 * @return Cache Size.
	 */
	long size();

	/**
	 * Invalidate all entries.
	 */
	void clear();
}
