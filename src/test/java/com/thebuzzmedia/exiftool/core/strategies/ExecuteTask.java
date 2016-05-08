/**
 * Copyright 2011 The Buzz Media, LLC
 * Copyright 2016 Mickael Jeanroy <mickael.jeanroy@gmail.com>
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

import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Task that will execute strategy with given parameters when internal
 * lock is released.
 */
class ExecuteTask implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(ExecuteTask.class);

	/**
	 * Internal identifier, used for debug logging.
	 */
	private final int id;

	/**
	 * Internal lock.
	 */
	private final CountDownLatch lock;

	/**
	 * Pool strategy.
	 */
	private final PoolStrategy pool;

	// Arguments of pool.execute method.
	private final CommandExecutor executor;
	private final String exifTool;
	private final List<String> arguments;
	private final OutputHandler handler;

	private IOException thrown;

	ExecuteTask(int id, CountDownLatch lock, PoolStrategy pool, CommandExecutor executor, String exifTool, List<String> arguments, OutputHandler handler) {
		this.id = id;
		this.lock = lock;
		this.pool = pool;
		this.executor = executor;
		this.exifTool = exifTool;
		this.arguments = new ArrayList<String>(arguments);
		this.handler = handler;
	}

	@Override
	public void run() {
		try {
			log.debug("Execute task #{} is waiting for lock", id);
			lock.await();
			log.debug("Execute task #{} is executing task from pool", id);
			pool.execute(executor, exifTool, arguments, handler);
		} catch (IOException ex) {
			log.debug("Execute task #{} throw exception", id);
			this.thrown = ex;
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public IOException getThrown() {
		return thrown;
	}
}
