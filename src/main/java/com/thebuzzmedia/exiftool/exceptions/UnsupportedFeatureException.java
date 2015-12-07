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

import com.thebuzzmedia.exiftool.Version;

import static java.lang.String.format;

/**
 * Class used to define an exception that occurs when the caller attempts to
 * use a feature that the underlying native ExifTool install does
 * not support (i.e. the version isn't new enough).
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @author Mickael Jeanroy (mickael.jeanroy@gmail.com)
 * @since 1.1
 */
@SuppressWarnings("serial")
public class UnsupportedFeatureException extends AbstractExifException {

	/**
	 * Exif Version (this version do not support feature).
	 */
	private final Version version;

	/**
	 * ExifTool path.
	 */
	private final String path;

	public UnsupportedFeatureException(String path, Version version) {
		super(message(path, version));
		this.version = version;
		this.path = path;
	}

	/**
	 * Gets {@link #path}.
	 *
	 * @return {@link #path}.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets {@link #version}.
	 *
	 * @return {@link #version}.
	 */
	public Version getVersion() {
		return version;
	}

	private static String message(String path, Version version) {
		String msg = "" +
			"Use of feature requires version %s or higher of the native ExifTool program. " +
			"The version of ExifTool referenced by the path '%s' is not high enough. " +
			"You can either upgrade the install of ExifTool or avoid using this feature to workaround this exception.";

		return format(msg, version, path);
	}
}
