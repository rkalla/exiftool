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

import com.thebuzzmedia.exiftool.commons.lang.Objects;

import java.util.concurrent.TimeUnit;

import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.isPositive;
import static com.thebuzzmedia.exiftool.commons.lang.PreConditions.notNull;

/**
 * A duration, represented by a delay and a time unit.
 */
public class SchedulerDuration {

	/**
	 * Create new duration in seconds.
	 *
	 * @param delay The delay, in seconds.
	 * @return The duration.
	 */
	public static SchedulerDuration seconds(long delay) {
		return new SchedulerDuration(delay, TimeUnit.SECONDS);
	}

	/**
	 * Create new duration in millis.
	 *
	 * @param delay The delay, in millis.
	 * @return The duration.
	 */
	public static SchedulerDuration millis(long delay) {
		return new SchedulerDuration(delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * Create new duration in millis.
	 *
	 * @param delay The delay, in millis.
	 * @param timeUnit The time unit.
	 * @return The duration.
	 */
	public static SchedulerDuration duration(long delay, TimeUnit timeUnit) {
		return new SchedulerDuration(delay, timeUnit);
	}

	/**
	 * The duration delay.
	 */
	private final long delay;

	/**
	 * The duration time unit.
	 */
	private final TimeUnit timeUnit;

	/**
	 * Create new duration.
	 *
	 * @param delay Duration delay.
	 * @param timeUnit Duration unit.
	 */
	private SchedulerDuration(long delay, TimeUnit timeUnit) {
		this.delay = isPositive(delay, "Delay should be a strictly positive value");
		this.timeUnit = notNull(timeUnit, "Time Unit should not be null");
	}

	/**
	 * Get the duration delay.
	 *
	 * @return The delay.
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * Get the duration time unit.
	 *
	 * @return The time unit.
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()
				+ "{ "
				+   "delay: " + delay + ", "
				+   "timeUnit: " + timeUnit
				+ "}";

	}
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof SchedulerDuration) {
			SchedulerDuration d = (SchedulerDuration) o;
			return delay == d.delay && timeUnit == d.timeUnit;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(delay, timeUnit);
	}
}
