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

package com.thebuzzmedia.exiftool.commons.io;

import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
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
	 * Read input and continue until {@link StreamVisitor#readLine(String)} returns false.
	 *
	 * @param is Input stream.
	 * @param visitor Result handler.
	 */
	public static void readInputStream(InputStream is, StreamVisitor visitor) {
		log.trace("Read input stream");

		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		try {
			boolean hasNext = true;
			while (hasNext) {
				line = br.readLine();
				hasNext = visitor.readLine(line);
				log.trace("  - Line: %s", line);
				log.trace("  - Continue: %s", hasNext);
			}
		}
		catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
		finally {
			// Maybe last line is not null (suppose an handler that should stop on given output).
			// On the opposite, if line is null, then we know that stream should be closed.
			if (line == null) {
				closeQuietly(br);
			}
		}
	}

	/**
	 * Close instance of {@link Closeable} object (stream, reader, writer, etc.).
	 * If an {@link IOException} occurs during the close operation, then it is logged but it
	 * will not fail by throwing another exception.
	 *
	 * @param closeable Closeable instance.
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		}
		catch (IOException ex) {
			// No worries, but log warning at least.
			log.warn(ex.getMessage(), ex);
		}
	}
}
