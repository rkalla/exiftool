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

package com.thebuzzmedia.exiftool.exceptions;

import java.io.File;

/**
 * Exception thrown when a file cannot be written.
 * A file cannot be written because:
 * - It does not exist.
 * - It is corrupted and cannot be updated.
 */
@SuppressWarnings("serial")
public class UnwritableFileException extends AbstractExifException {

	/**
	 * Unwritable file.
	 */
	private final File file;

	/**
	 * Create exception.
	 *
	 * @param file Unwritable file.
	 * @param message Error message.
	 */
	public UnwritableFileException(File file, String message) {
		super(message);
		this.file = file;
	}

	public File getFile() {
		return file;
	}
}
