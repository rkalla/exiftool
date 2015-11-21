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

package com.thebuzzmedia.exiftool.commons;

import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.OutputHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Static Input/Output Utilities.
 */
public final class IOs {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(IOs.class);

	// Ensure non instantiation.
	private IOs() {
	}

	/**
	 * Read input and continue until {@link OutputHandler#readLine(String)} returns false.
	 *
	 * @param is Input stream.
	 * @param handler Result handler.
	 */
	public static void readInputStream(InputStream is, OutputHandler handler) {
		log.trace("Read input stream");

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			boolean hasNext = true;
			while (hasNext) {
				String line = br.readLine();
				hasNext = handler.readLine(line);
				log.trace("  - Line: %s", line);
				log.trace("  - Continue: %s", hasNext);
			}
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		finally {
			try {
				br.close();
			}
			catch (IOException ex) {
				// No worries
			}
		}
	}
}
