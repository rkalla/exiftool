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

package com.thebuzzmedia.exiftool.tests;

import java.io.File;

import com.thebuzzmedia.exiftool.it.img.AbstractExifToolImgIT;

public final class TestConstants {

	private TestConstants() {
	}

	public static final String BR = System.getProperty("line.separator");

	public static final boolean IS_WINDOWS;

	public static final File EXIF_TOOL;

	static {
		final String osName = System.getProperty("os.name").toLowerCase();
		final boolean isWindows = osName.contains("windows");
		final String relativePath = "/exiftool-10_16/" + (isWindows ? "windows/exiftool.exe" : "unix/exiftool");

		final File file = new File(AbstractExifToolImgIT.class.getResource(relativePath).getFile());
		file.setExecutable(true);
		file.setReadable(true);

		IS_WINDOWS = isWindows;
		EXIF_TOOL = file;
	}
}
