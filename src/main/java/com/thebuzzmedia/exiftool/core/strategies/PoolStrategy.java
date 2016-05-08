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

import com.thebuzzmedia.exiftool.ExecutionStrategy;
import com.thebuzzmedia.exiftool.Version;
import com.thebuzzmedia.exiftool.exceptions.PoolIOException;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.OutputHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notEmpty;

/**
 * Implementation of {@link ExecutionStrategy} using a pool of
 * strategies.
 *
 * Each time {{@link #execute(CommandExecutor, String, List, OutputHandler)} method
 * is called, an internal strategy from the pool is picked and return to the pool once work
 * is finished.
 *
 * This strategy should be used in a multithreaded environment, when application need to
 * extract exif data from images in parallel.
 */
public class PoolStrategy implements ExecutionStrategy {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(PoolStrategy.class);

	/**
	 * Pool size (i.e number of available slot).
	 */
	private final int poolSize;

	/**
	 * Pool list.
	 */
	private final BlockingQueue<ExecutionStrategy> pool;

	/**
	 * Create the pool.
	 *
	 * @param strategies Internal strategies.
	 * @throws NullPointerException If {@code strategies} is {@code null}.
	 * @throws IllegalArgumentException If {@code strategies} is empty.
	 */
	public PoolStrategy(Collection<ExecutionStrategy> strategies) {
		notEmpty(strategies, "Pool must not be empty");

		this.poolSize = strategies.size();
		this.pool = new LinkedBlockingDeque<ExecutionStrategy>(strategies);
	}

	@Override
	public void execute(CommandExecutor executor, String exifTool, List<String> arguments, OutputHandler handler) throws IOException {
		ExecutionStrategy strategy = null;
		try {
			strategy = this.pool.take();
			strategy.execute(executor, exifTool, arguments, handler);
		}
		catch (InterruptedException ex) {
			log.warn(ex.getMessage());
			Thread.currentThread().interrupt();
		}
		finally {
			if (strategy != null) {
				this.pool.offer(strategy);
			}
		}
	}

	@Override
	public boolean isRunning() {
		return pool.size() < poolSize;
	}

	@Override
	public boolean isSupported(Version version) {
		for (ExecutionStrategy strategy : pool) {
			if (!strategy.isSupported(version)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void close() throws Exception {
		List<ExecutionStrategy> strategies = new ArrayList<ExecutionStrategy>(poolSize);

		// Get all strategies from the pool.
		// We need to be sure that all strategies are available.
		log.debug("Retrieve all pending strategies");
		int added = 0;
		while (added != poolSize) {
			try {
				added += this.pool.drainTo(strategies);
				if (added < poolSize) {
					ExecutionStrategy strategy = this.pool.take();
					strategies.add(strategy);
					added++;
				}
			} catch (InterruptedException ex) {
				log.warn(ex.getMessage());
				Thread.currentThread().interrupt();
			}
		}

		List<Exception> thrownEx = new LinkedList<Exception>();
		int i = 0;
		for (ExecutionStrategy strategy : strategies) {
			try {
				log.debug("Closing strategy #%s", i);
				strategy.close();
			} catch (Exception ex) {
				log.error("Failed to close strategy #%s", i);
				thrownEx.add(ex);
			}
			finally {
				i++;

				// Strategy is now available.
				this.pool.offer(strategy);
			}
		}

		if (thrownEx.size() > 0) {
			throw new PoolIOException("Some strategies in the pool failed to close properly", thrownEx);
		}
	}
}
