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

package com.thebuzzmedia.exiftool.process;

import java.util.List;

/**
 * Command Line interface.
 *
 * <p />
 *
 * A command line is defined by:
 * <ul>
 *   <li>Executable path (or executable name if it is globally available).</li>
 *   <li>List of arguments: may be empty.</li>
 * </ul>
 *
 * Each of these should be returned in the {@link #getArguments()} method:
 * <ul>
 *   <li>First element is the executable value.</li>
 *   <li>Next elements are the executable arguments.</li>
 * </ul>
 */
public interface Command {

	/**
	 * Command arguments:
	 * <ul>
	 *   <li>First item should be the command line executable value.</li>
	 *   <li>Next items should be the command line arguments.</li>
	 * </ul>
	 *
	 * @return Command arguments.
	 */
	List<String> getArguments();
}
