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

package com.thebuzzmedia.exiftool.process;

import java.util.List;

/**
 * Command Line interface.
 * A command line is defined by:
 * - Executable path (or executable name if it is globally available).
 * - List of arguments: may be empty.
 *
 * Each of these should be returned in the {@link #getArguments()} method:
 * - First element is the executable value.
 * - Next elements are the executable arguments.
 */
public interface Command {

	/**
	 * Command arguments.
	 * First item should be the command line executable value.
	 * Next items should be the command line arguments.
	 *
	 * @return Command arguments.
	 */
	List<String> getArguments();
}
