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

import java.util.List;

/**
 * Interface used to define different output formats.
 * For instance, default implementations defined by {@link com.thebuzzmedia.exiftool.core.StandardFormat}
 * values can be returned in: numeric or human-readable text.
 *
 * <p/>
 *
 * ExifTool, via the {@code -n} command line arg, is capable of
 * returning most values in their raw numeric form (e.g.
 * Aperture="2.8010323841") as well as a more human-readable/friendly format
 * (e.g. Aperture="2.8").
 *
 * <p/>
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @author Mickael Jeanroy (mickael.jeanroy@gmail.com)
 * @since 1.1
 */
public interface Format {

	/**
	 * List of arguments to pass to {@code exiftool} command to return
	 * associated format.
	 * This method should not return {@code null}, but an empty list if no arguments
	 * should be returned.
	 *
	 * @return List of arguments.
	 */
	List<String> getArgs();
}
