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

package com.thebuzzmedia.exiftool.core.strategies;

import com.thebuzzmedia.exiftool.ExecutionStrategy;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import com.thebuzzmedia.exiftool.process.command.CommandBuilder;

import java.util.List;

/**
 * This is the default strategy.
 *
 * <p />
 *
 * For each execution, a one-shot process is:
 * <ul>
 *   <li>Created using withExecutor.</li>
 *   <li>Used to execute command line.</li>
 *   <li>Closed at the end of the execution.</li>
 * </ul>
 */
public class DefaultStrategy implements ExecutionStrategy {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(DefaultStrategy.class);

	/**
	 * Create strategy.
	 */
	public DefaultStrategy() {
	}

	@Override
	public void execute(CommandExecutor executor, String exifTool, List<String> arguments, OutputHandler handler) {
		log.debug("Using ExifTool in non-daemon mode (-stay_open False)...");

		Command cmd = CommandBuilder.builder(exifTool)
			.addAll(arguments)
			.build();

		executor.execute(cmd, handler);
	}

	@Override
	public boolean isRunning() {
		// Just return false.
		return false;
	}

	@Override
	public void close() {
		// Nothing to do here
	}
}
