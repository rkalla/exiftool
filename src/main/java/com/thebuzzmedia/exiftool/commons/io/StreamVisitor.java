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

package com.thebuzzmedia.exiftool.commons.io;

/**
 * Visitor used to read lines of {@link java.io.InputStream} during
 * read operation.
 */
public interface StreamVisitor {

	/**
	 * Read line.
	 * Result is a boolean and should indicate if instance of {@link java.io.InputStream}
	 * has a next line to read.
	 *
	 * @param line Line.
	 * @return {@code true} if next line should be read, {@code false} otherwise.
	 */
	boolean readLine(String line);
}
