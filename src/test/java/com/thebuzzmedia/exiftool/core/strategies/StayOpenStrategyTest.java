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

import com.thebuzzmedia.exiftool.Scheduler;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.writePrivateField;
import static com.thebuzzmedia.exiftool.tests.TestConstants.BR;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StayOpenStrategyTest {

	@Mock
	private Scheduler scheduler;

	@Mock
	private CommandProcess process;

	@Mock
	private CommandExecutor executor;

	@Mock
	private OutputHandler outputHandler;

	@Captor
	private ArgumentCaptor<List<String>> argsCaptor;

	@Captor
	private ArgumentCaptor<Command> cmdCaptor;

	private String exifTool;

	private List<String> args;

	private StayOpenStrategy strategy;

	@Before
	public void setUp() throws Exception {
		exifTool = "exiftool";
		strategy = new StayOpenStrategy(scheduler);

		// Mock withExecutor
		when(executor.start(any(Command.class))).thenReturn(process);

		// Execution arguments
		args = asList("-S", "-n", "-XArtist", "XComment", "-execute");
	}

	@Test
	public void it_should_create_stay_open_strategy() throws Exception {
		assertThat(readPrivateField(strategy, "scheduler", Scheduler.class)).isSameAs(scheduler);
		assertThat(readPrivateField(strategy, "process", CommandProcess.class)).isNull();
	}

	@Test
	public void it_should_execute_command() throws Exception {
		strategy.execute(executor, exifTool, args, outputHandler);

		InOrder inOrder = inOrder(scheduler, executor, process);
		inOrder.verify(executor).start(cmdCaptor.capture());
		inOrder.verify(scheduler).stop();
		inOrder.verify(scheduler).start(any(Runnable.class));
		inOrder.verify(process).write(argsCaptor.capture());
		inOrder.verify(process).flush();
		inOrder.verify(process).read(any(OutputHandler.class));

		assertThat(readPrivateField(strategy, "process", CommandProcess.class))
			.isNotNull()
			.isSameAs(process);

		verifyStartProcess();
		verifyExecutionArguments();
	}

	@Test
	public void it_should_not_start_process_twice_if_it_is_running() throws Exception {
		// Mock Process
		writePrivateField(strategy, "process", process);
		when(process.isClosed()).thenReturn(false);

		strategy.execute(executor, exifTool, args, outputHandler);

		// Executor should not have been started
		verify(executor, never()).start(any(Command.class));

		InOrder inOrder = inOrder(scheduler, process);
		inOrder.verify(process).isClosed();
		inOrder.verify(scheduler).stop();
		inOrder.verify(scheduler).start(any(Runnable.class));
		inOrder.verify(process).write(argsCaptor.capture());
		inOrder.verify(process).flush();
		inOrder.verify(process).read(any(OutputHandler.class));

		verifyExecutionArguments();
	}

	@Test
	public void it_should_restart_process_twice_if_it_is_not_running() throws Exception {
		// Mock Process
		writePrivateField(strategy, "process", process);
		when(process.isClosed()).thenReturn(true);

		strategy.execute(executor, exifTool, args, outputHandler);

		InOrder inOrder = inOrder(scheduler, process, executor);
		inOrder.verify(process).isClosed();
		inOrder.verify(executor).start(cmdCaptor.capture());
		inOrder.verify(scheduler).stop();
		inOrder.verify(scheduler).start(any(Runnable.class));
		inOrder.verify(process).write(argsCaptor.capture());
		inOrder.verify(process).flush();
		inOrder.verify(process).read(any(OutputHandler.class));

		verifyStartProcess();
		verifyExecutionArguments();
	}

	@Test
	public void it_should_not_close_process_if_it_is_not_started() throws Exception {
		StayOpenStrategy strategy = new StayOpenStrategy(scheduler);
		strategy.close();
		verify(scheduler, never()).stop();
	}

	@Test
	public void it_should_close_process_if_it_is_started() throws Exception {
		StayOpenStrategy strategy = new StayOpenStrategy(scheduler);
		writePrivateField(strategy, "process", process);
		strategy.close();

		InOrder inOrder = inOrder(scheduler, process);
		inOrder.verify(scheduler).stop();
		inOrder.verify(process).write("-stay_open\nFalse\n");
		inOrder.verify(process).flush();
		inOrder.verify(process).close();

		verifyNoMoreInteractions(process);
		verifyNoMoreInteractions(scheduler);
	}

	@Test
	public void it_should_check_if_process_is_running() throws Exception {
		StayOpenStrategy strategy = new StayOpenStrategy(scheduler);
		assertThat(strategy.isRunning()).isFalse();

		writePrivateField(strategy, "process", process);

		when(process.isRunning()).thenReturn(true);
		assertThat(strategy.isRunning()).isTrue();

		when(process.isRunning()).thenReturn(false);
		assertThat(strategy.isRunning()).isFalse();
	}

	private void verifyStartProcess() {
		Command startCmd = cmdCaptor.getValue();
		assertThat(startCmd.getArguments())
			.isNotNull()
			.isNotEmpty()
			.hasSize(5)
			.containsExactly(
				exifTool, "-stay_open", "True", "-@", "-"
			);
	}

	private void verifyExecutionArguments() {
		List<String> processArgs = argsCaptor.getValue();
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

	private List<String> appendBr(List<String> list) {
		List<String> results = new ArrayList<String>(list.size());
		for (String input : list) {
			results.add(input + BR);
		}

		return results;
	}
}
