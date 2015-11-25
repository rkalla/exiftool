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

package com.thebuzzmedia.exiftool.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class FileUtils {

	private FileUtils() {
	}

	/**
	 * Copy file to a destination directory.
	 *
	 * @param src File to copy.
	 * @param dstFolder Destination.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File copy(File src, File dstFolder) throws IOException {
		File dst = new File(dstFolder, src.getName());
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(dst);

		int buf;
		while ((buf = fis.read()) >= 0) {
			fos.write(buf);
		}

		fis.close();
		fos.close();

		return dst;
	}
}
