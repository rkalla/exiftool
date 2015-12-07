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

import java.util.Timer;
import java.util.TimerTask;

import static com.thebuzzmedia.exiftool.commons.lang.Objects.firstNonNull;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.isPositive;

/**
 * Scheduler using {@link java.util.Timer} as internal task scheduler.
 *
 * <p />
 *
 * This scheduler should be used only for compatibility reason (this was the very first kind of scheduler
 * available), instead instance of {@link com.thebuzzmedia.exiftool.core.schedulers.DefaultScheduler} should
 * be used.
 */
public class TimerScheduler implements Scheduler {

	/**
	 * Name of timer thread.
	 */
	private final String name;

	/**
	 * Schedule delay.
	 * This delay must be strictly positive.
	 */
	private final long delay;

	/**
	 * Timer Scheduler.
	 */
	private final Timer timer;

	/**
	 * Pending task.
	 * This task should be cancel with {@link #stop()} method before any
	 * call to {@link #start(Runnable)} method.
	 */
	private TimerTask pendingTask;

	/**
	 * Create scheduler.
	 * @param name Thread name.
	 * @param delay Delay before task execution.
	 * @throws IllegalArgumentException If {@code delay} is not strictly positive.
	 */
	public TimerScheduler(String name, long delay) {
		this.name = firstNonNull(name, "ExifTool Cleanup Timer");
		this.delay = isPositive(delay, "Delay must be strictly positive");
		this.timer = new Timer(this.name, true);
	}

	@Override
	public synchronized void start(Runnable runnable) {
		pendingTask = new CleanupTask(runnable);
		timer.schedule(pendingTask, delay);
	}

	@Override
	public synchronized void stop() {
		if (pendingTask != null) {
			pendingTask.cancel();
			timer.purge();
			pendingTask = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		stop();
		timer.cancel();
	}

	private static class CleanupTask extends TimerTask {
		private final Runnable runnable;

		private CleanupTask(Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void run() {
			runnable.run();
		}
	}
}
