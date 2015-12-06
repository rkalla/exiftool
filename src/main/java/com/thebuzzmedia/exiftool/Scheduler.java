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

package com.thebuzzmedia.exiftool;

/**
 * Scheduler interface.
 *
 * Each implementation should provide implementation for:
 * <ul>
 *   <li>{@code start} method: should schedule task in a specified amount of time.</li>
 *   <li>{@code stop} method: should cancel future task.</li>
 * </ul>
 *
 * Note that this interface does not guarantee that scheduled task will
 * effectively be executed.
 */
public interface Scheduler {

	/**
	 * Schedule task.
	 *
	 * <p />
	 *
	 * Task should not run immediately, instead it should run
	 * in a specified amount of time (implementation dependent).
	 *
	 * @param runnable Task to run.
	 */
	void start(Runnable runnable);

	/**
	 * Stop pending task.
	 */
	void stop();
}
