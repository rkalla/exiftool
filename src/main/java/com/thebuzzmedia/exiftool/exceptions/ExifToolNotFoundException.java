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

import java.io.IOException;

import com.thebuzzmedia.exiftool.process.CommandResult;

/**
 * Exception thrown when exiftool program is missing.
 */
public class ExifToolNotFoundException extends AbstractExifException {

	/**
	 * ExifTool path defined during command execution.
	 */
	private final String path;

	/**
	 * The result triggered during command execution.
	 */
	private final CommandResult result;

	/**
	 * Create exception.
	 *
	 * @param path ExifTool path defined during command execution.
	 * @param result The result triggered during command execution.
	 */
	public ExifToolNotFoundException(String path, CommandResult result) {
		super(message(path));
		this.path = path;
		this.result = result;
	}

	/**
	 * Create exception.
	 *
	 * @param path ExifTool path defined during command execution.
	 * @param result The result triggered during command execution.
	 */
	public ExifToolNotFoundException(IOException ex, String path, CommandResult result) {
		super(ex);
		this.path = path;
		this.result = null;
	}

	/**
	 * Get ExifTool path defined during command execution.
	 *
	 * @return ExifTool path defined during command execution.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get result triggered during command execution.
	 *
	 * @return The result triggered during command execution.
	 */
	public CommandResult getResult() {
		return result;
	}

	private static String message(String path) {
		return String.format("Cannot find exiftool from path: %s", path);
	}
}
