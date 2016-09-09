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

import com.thebuzzmedia.exiftool.Version;
import com.thebuzzmedia.exiftool.VersionCache;
import com.thebuzzmedia.exiftool.process.CommandExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static com.thebuzzmedia.exiftool.commons.exceptions.Exceptions.launderThrowable;

/**
 * Default implementation for {@link VersionCache}.
 * Internally, this implementation use a {@link ConcurrentHashMap}.
 */
class DefaultVersionCache implements VersionCache {

	/**
	 * Cache of computed results.
	 * Key is the path to the exiftool executable, value is the computed
	 * version.
	 */
	private final ConcurrentMap<String, Future<Version>> cache;

	/**
	 * Create default cache.
	 */
	DefaultVersionCache() {
		super();
		this.cache = new ConcurrentHashMap<>();
	}

	@Override
	public Version load(final String exifTool, final CommandExecutor executor) {
		// Use while true to retry parsing in case of CancellationException
		boolean interrupted = false;
		Version version = null;

		while (version == null) {
			Future<Version> task = cache.get(exifTool);
			if (task == null) {
				Callable<Version> callable = new VersionCallable(exifTool, executor);
				FutureTask<Version> newTask = new FutureTask<>(callable);
				task = cache.putIfAbsent(exifTool, newTask);
				if (task == null) {
					task = newTask;
					newTask.run();
				}
			}

			try {
				version = task.get();
			}
			catch (CancellationException e) {
				cache.remove(exifTool, task);
				// Do not return anything and retry
			}
			catch (InterruptedException ex) {
				cache.remove(exifTool, task);
				interrupted = true;
				// Do not return anything and retry
			}
			catch (ExecutionException ex) {
				throw launderThrowable(ex.getCause());
			}
		}

		if (interrupted) {
			// Restore interrupt status
			Thread.currentThread().interrupt();
		}

		return version;
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public long size() {
		return cache.size();
	}
}
