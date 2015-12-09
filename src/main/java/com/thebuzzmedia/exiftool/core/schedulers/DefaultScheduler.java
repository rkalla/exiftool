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

import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.isPositive;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notNull;

/**
 * Default implementation for {@code exiftool} {@link com.thebuzzmedia.exiftool.Scheduler}.
 *
 * <p/>
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
	 * Delay before task execution.
	 */
	private final long delay;

	/**
	 * Time unit for the specified {@link #delay}.
	 * Default is {@link TimeUnit#MILLISECONDS}.
	 */
	private final TimeUnit timeUnit;

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
	 */
	public DefaultScheduler(long delay) {
		this(delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * Create new scheduler.
	 * A default withExecutor will be created.
	 *
	 * @param delay Delay.
	 * @param timeUnit Time Unit.
	 * @throws NullPointerException If {@code timeUnit} is {@code null}.
	 * @throws IllegalArgumentException If {@code delay} is less than or equal to zero.
	 */
	public DefaultScheduler(long delay, TimeUnit timeUnit) {
		this.delay = isPositive(delay, "Delay should be a strictly positive value");
		this.timeUnit = notNull(timeUnit, "Time Unit should not be null");

		// Create executor
		this.executor = new ScheduledThreadPoolExecutor(1);
		this.executor.setRemoveOnCancelPolicy(true);
	}

	@Override
	public synchronized void start(Runnable runnable) {
		executor.schedule(runnable, delay, timeUnit);
	}

	@Override
	public synchronized void stop() {
		for (Runnable runnable : executor.getQueue()) {
			((RunnableFuture<?>) runnable).cancel(false);
		}

		executor.purge();
	}

	// Implement finalizer.
	// This is just a small security to stop scheduled task if
	// instance is garbage collected.
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		stop();
		executor.shutdownNow();
	}
}
