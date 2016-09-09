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
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.process.command.CommandBuilder;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/**
 * Execute {@code exiftool} command to get associated version
 * and return the result.
 */
class VersionCallable implements Callable<Version> {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(VersionCallable.class);

	/**
	 * Path of {@code exiftool} executable.
	 */
	private final String exifTool;

	/**
	 * Executor used to execute {@code exiftool} command.
	 */
	// Use a weak reference to let GC garbage collect executor as soon as possible.
	private final WeakReference<CommandExecutor> executor;

	/**
	 * Create task.
	 *
	 * @param exifTool Path of {@code exiftool} executable.
	 * @param executor Executor used to execute {@code exiftool} command.
	 */
	VersionCallable(String exifTool, CommandExecutor executor) {
		this.exifTool = exifTool;
		this.executor = new WeakReference<>(executor);
	}

	@Override
	public Version call() throws Exception {
		log.debug("Checking exiftool (path: %s) version", exifTool);
		try {
			CommandExecutor commandExecutor = executor.get();
			if (commandExecutor == null) {
				// This should never happen, since this function will be called very quickly after
				// exiftool creation.
				log.warn("Command executor has already been garbage collected, cannot parse version");
				return null;
			}

			CommandResult result = commandExecutor.execute(CommandBuilder.builder(exifTool)
				.addArgument("-ver")
				.build());

			return result.isSuccess() ? new Version(result.getOutput()) : null;
		}
		catch (IOException ex) {
			// Do not fail now.
			log.warn(ex.getMessage(), ex);
			return null;
		}
	}
}
