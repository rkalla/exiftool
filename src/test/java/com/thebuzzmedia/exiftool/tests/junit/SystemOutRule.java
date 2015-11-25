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

package com.thebuzzmedia.exiftool.tests.junit;

import org.junit.rules.ExternalResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Catch System.out logging and store in a buffer.
 */
public class SystemOutRule extends ExternalResource {

	/**
	 * Original out stream.
	 * Will be initialized before each tests.
	 * Will be restored after each tests.
	 */
	private PrintStream originalOut;

	/**
	 * Custom out stream.
	 * Will be initialized before each tests.
	 * Will be flushed after each tests.
	 */
	private ByteArrayOutputStream out;

	@Override
	public void before() {
		originalOut = System.out;
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	}

	@Override
	public void after() {
		try {
			out.flush();
		}
		catch (IOException ex) {
			// No worries
		}

		// Restore original out stream
		System.setOut(originalOut);
	}

	public String getPendingOut() {
		if (out == null) {
			return null;
		}

		String pending = out.toString();
		out.reset();
		return pending;
	}
}
