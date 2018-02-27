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

package com.thebuzzmedia.exiftool.core.schedulers;

import com.thebuzzmedia.exiftool.Scheduler;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;

import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notNull;
import static com.thebuzzmedia.exiftool.core.schedulers.SchedulerDuration.duration;
import static com.thebuzzmedia.exiftool.core.schedulers.SchedulerDuration.millis;
import static com.thebuzzmedia.exiftool.core.schedulers.SchedulerDuration.seconds;

/**
 * Default implementation for {@code exiftool} {@link com.thebuzzmedia.exiftool.Scheduler}.
 *
 * <br>
 *
 * This scheduler will execute a task in a specified amount of time:
 *
 * <ul>
 *   <li>Time must be specified during construction.</li>
 *   <li>Time unit (milliseconds, seconds, hours, etc.) may be defined during construction.</li>
 * </ul>
 *
 * <strong>Note:</strong> This scheduler will stop pending task when instance is
 * garbage collected. This is just a small security but we cannot guarantee when
 * instance will be garbage collected. For this reason, it should be up to the caller
 * to stop this scheduler when it is not needed anymore.
 */
public class DefaultScheduler implements Scheduler {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(DefaultScheduler.class);

	/**
	 * Delay before task execution.
	 */
	private final SchedulerDuration executionDelay;

	/**
	 * Delay to wait for scheduler termination.
	 */
	private final SchedulerDuration terminationDelay;

	/**
	 * Executor used to defer task execution.
	 * This withExecutor will produce instance of {@link RunnableFuture} tasks.
	 */
	private final ScheduledThreadPoolExecutor executor;

	/**
	 * Create new scheduler.
	 * Default time unit is {@link TimeUnit#MILLISECONDS}.
	 * A default withExecutor will be created.
	 *
	 * @param delay Delay.
	 * @throws IllegalArgumentException If {@code delay} is less than or equal to zero.
	 * @deprecated Use {@link #DefaultScheduler(SchedulerDuration)} instead.
	 */
	@Deprecated
	public DefaultScheduler(long delay) {
		this(millis(delay));
	}

	/**
	 * Create new scheduler.
	 * A default withExecutor will be created.
	 *
	 * @param delay Delay.
	 * @param timeUnit Time Unit.
	 * @throws NullPointerException If {@code timeUnit} is {@code null}.
	 * @throws IllegalArgumentException If {@code delay} is less than or equal to zero.
	 * @deprecated Use {@link #DefaultScheduler(SchedulerDuration)} instead.
	 */
	@Deprecated
	public DefaultScheduler(long delay, TimeUnit timeUnit) {
		this(duration(delay, timeUnit));
	}

	/**
	 * Create new scheduler.
	 * Default time unit is {@link TimeUnit#MILLISECONDS}.
	 * A default withExecutor will be created.
	 *
	 * @param executionDelay The delay between execution.
	 * @throws IllegalArgumentException If {@code delay} is less than or equal to zero.
	 */
	public DefaultScheduler(SchedulerDuration executionDelay) {
		this(executionDelay, seconds(5));
	}

	/**
	 * Create new scheduler.
	 * A default withExecutor will be created.
	 *
	 * @param executionDelay The delay between scheduler execution.
	 * @param terminationDelay The delay to use to wait for termination when scheduler is shutting down.
	 * @throws NullPointerException If {@code timeUnit} is {@code null}.
	 * @throws IllegalArgumentException If {@code delay} is less than or equal to zero.
	 */
	public DefaultScheduler(SchedulerDuration executionDelay, SchedulerDuration terminationDelay) {
		this.executionDelay = notNull(executionDelay, "Execution delay must not be null");
		this.terminationDelay = notNull(terminationDelay, "Termination delay must not be null");

		// Create executor
		this.executor = new ScheduledThreadPoolExecutor(1);
		this.executor.setRemoveOnCancelPolicy(true);
	}

	@Override
	public synchronized void start(Runnable runnable) {
		executor.schedule(runnable, executionDelay.getDelay(), executionDelay.getTimeUnit());
	}

	@Override
	public synchronized void stop() {
		for (Runnable runnable : executor.getQueue()) {
			((RunnableFuture<?>) runnable).cancel(false);
		}

		executor.purge();
	}

	@Override
	public synchronized void shutdown() {
		stop();
		processShutdown();
	}

	private void processShutdown() {
		log.debug("Shutdown scheduler");

		// Disable new tasks from being submitted
		executor.shutdown();

		try {
			log.debug("Wait for scheduler termination");

			// Wait a while for existing tasks to terminate
			if (!executor.awaitTermination(terminationDelay.getDelay(), terminationDelay.getTimeUnit())) {
				// Cancel currently executing tasks
				log.debug("Termination seems to failed, shutdown now");
				executor.shutdownNow();

				// Wait a while for tasks to respond to being cancelled
				if (!executor.awaitTermination(terminationDelay.getDelay(), terminationDelay.getTimeUnit())) {
					log.error("Scheduler did not seem to terminate");
				}
			}
		}
		catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executor.shutdownNow();

			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	// Implement finalizer.
	// This is just a small security to stop scheduled task if
	// instance is garbage collected.
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		shutdown();
	}
}
