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

package com.thebuzzmedia.exiftool.process.executor;

import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.CommandExecutor;

/**
 * Executor Factory.
 */
public final class CommandExecutors {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(CommandExecutors.class);

	// Ensure non instantiation.
	private CommandExecutors() {
	}

	/**
	 * Create a fresh new withExecutor.
	 *
	 * @return Executor.
	 */
	public static CommandExecutor newExecutor() {
		log.debug("Create new default command withExecutor");
		return new DefaultCommandExecutor();
	}
}
