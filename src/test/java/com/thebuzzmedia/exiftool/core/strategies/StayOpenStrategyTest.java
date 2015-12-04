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

package com.thebuzzmedia.exiftool.core.strategies;

import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.writePrivateField;
import static com.thebuzzmedia.exiftool.tests.TestConstants.BR;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StayOpenStrategyTest {

	private StayOpenStrategy strategy;

	@After
	public void tearDown() throws Exception {
		if (strategy != null) {
			strategy.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void it_should_execute_command() throws Exception {
		String exifTool = "exifTool";
		List<String> args = asList("-S", "-n", "-XArtist", "XComment", "-execute");
		CommandExecutor executor = mock(CommandExecutor.class);
		OutputHandler handler = mock(OutputHandler.class);

		CommandProcess process = mock(CommandProcess.class);
		when(executor.start(any(Command.class))).thenReturn(process);

		strategy = new StayOpenStrategy(10000);
		strategy.execute(executor, exifTool, args, handler);

		ArgumentCaptor<Command> startCmdCaptor = ArgumentCaptor.forClass(Command.class);
		verify(executor).start(startCmdCaptor.capture());

		Command startCmd = startCmdCaptor.getValue();
		assertThat(startCmd.getArguments())
			.isNotNull()
			.isNotEmpty()
			.hasSize(5)
			.containsExactly(
				exifTool, "-stay_open", "True", "-@", "-"
			);

		CommandProcess p = readPrivateField(strategy, "process", CommandProcess.class);
		assertThat(p)
			.isNotNull()
			.isSameAs(process);

		ArgumentCaptor<List> argsCaptor = ArgumentCaptor.forClass(List.class);
		verify(process).write(argsCaptor.capture());

		List<String> processArgs = (List<String>) argsCaptor.getValue();
		assertThat(processArgs)
			.isNotNull()
			.isNotEmpty()
			.are(new Condition<String>() {
				@Override
				public boolean matches(String value) {
					return value.endsWith(BR);
				}
			})
			.isEqualTo(appendBr(args));
	}

	@Test
	public void it_should_not_close_process_if_it_is_not_started() throws Exception {
		strategy = new StayOpenStrategy(10000);
		strategy.close();
	}

	@Test
	public void it_should_close_process_if_it_is_started() throws Exception {
		strategy = new StayOpenStrategy(10000);

		CommandProcess process = mock(CommandProcess.class);
		writePrivateField(strategy, "process", process);

		strategy.close();

		verify(process).close();
	}

	private static List<String> appendBr(List<String> list) {
		List<String> results = new ArrayList<String>(list.size());
		for (String input : list) {
			results.add(input + BR);
		}

		return results;
	}
}
