/**
 * Copyright 2011 The Buzz Media, LLC
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
 * Enum used to define the different kinds of features in the native
 * ExifTool executable that this class can help you take advantage of.
 * <p/>
 * These flags are different from {@link Tag}s in that a "feature" is
 * determined to be a special functionality of the underlying ExifTool
 * executable that requires a different code-path in this class to take
 * advantage of; for example, <code>-stay_open True</code> support.
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @since 1.1
 */
public enum Feature {
	/**
	 * Enum used to specify that you wish to launch the underlying ExifTool
	 * process with <code>-stay_open True</code> support turned on that this
	 * class can then take advantage of.
	 * <p/>
	 * Required ExifTool version is <code>8.36</code> or higher.
	 */
	STAY_OPEN("8.36");

	/**
	 * Used to get the version of ExifTool required by this feature in order
	 * to work.
	 *
	 * @return the version of ExifTool required by this feature in order to
	 *         work.
	 */
	public String getVersion() {
		return version;
	}

	private String version;

	private Feature(String version) {
		this.version = version;
	}
}
