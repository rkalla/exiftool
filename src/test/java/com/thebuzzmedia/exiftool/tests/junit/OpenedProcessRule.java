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

package com.thebuzzmedia.exiftool.tests.junit;

import org.junit.rules.ExternalResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import static com.thebuzzmedia.exiftool.tests.TestConstants.IS_WINDOWS;
import static org.junit.Assert.fail;

/**
 * Rule to check that no process has been left open after test.
 */
public class OpenedProcessRule extends ExternalResource {

	/**
	 * Process name to check.
	 */
	private final String processName;

	/**
	 * Opened process before tests: used to ignore these process after test has
	 * running.
	 */
	private Set<String> openedBefore;

	/**
	 * Create rule.
	 *
	 * @param processName Process name.
	 */
	public OpenedProcessRule(String processName) {
		this.processName = processName;
	}

	@Override
	protected void before() throws Throwable {
		super.before();
		this.openedBefore = getProcesses();
	}

	@Override
	protected void after() {
		super.after();
		try {
			Set<String> process = getProcesses();
			process.removeAll(openedBefore);
			if (!process.isEmpty()) {
				fail(String.format("Expect opened process during test to have been closed (opened process: %s)", process));
			}
		}
		catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	private Set<String> getProcesses() throws Exception {
		final String command;

		if (IS_WINDOWS) {
			command = System.getenv("windir") + "\\system32\\" + "tasklist.exe";
		}
		else {
			command = "ps -e";
		}

		Process p = Runtime.getRuntime().exec(command);
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;

		Set<String> openedProcesses = new HashSet<String>();
		while ((line = input.readLine()) != null) {
			if (line.contains(processName)) {
				openedProcesses.add(line);
			}
		}

		input.close();

		return openedProcesses;
	}
}
