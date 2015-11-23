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

package com.thebuzzmedia.exiftool.mocks;

import com.thebuzzmedia.exiftool.process.OutputHandler;
import com.thebuzzmedia.exiftool.tests.TestConstants;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

public class ReadStringResultAnswer implements Answer<String> {

	private final List<String> lines;

	public ReadStringResultAnswer(String... lines) {
		this.lines = new ArrayList<String>(lines.length);
		addAll(this.lines, lines);
	}

	@Override
	public String answer(InvocationOnMock invocation) throws Throwable {
		Object[] arguments = invocation.getArguments();
		int argIndex = arguments.length == 1 ? 0 : 1;
		OutputHandler handler = (OutputHandler) arguments[argIndex];

		StringBuilder output = new StringBuilder();
		for (String line : lines) {
			handler.readLine(line);
			if (line != null) {
				output.append(line).append(TestConstants.BR);
			}
		}

		return output.toString().trim();
	}
}
