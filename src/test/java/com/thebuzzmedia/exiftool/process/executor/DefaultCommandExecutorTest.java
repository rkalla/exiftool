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

package com.thebuzzmedia.exiftool.process.executor;

import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandProcess;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import org.junit.Test;

import java.io.File;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultCommandExecutorTest {

	@Test
	public void it_should_execute_command_line() {
		File script = new File(getClass().getResource("/processes/success.sh").getFile());
		Command command = createUnixCommand(script.getAbsolutePath());

		CommandExecutor executor = new DefaultCommandExecutor();
		CommandResult result = executor.execute(command);

		assertThat(result).isNotNull();
		assertThat(result.getExitStatus()).isZero();
		assertThat(result.getOutput()).isEqualTo("Hello World");
	}

	@Test
	public void it_should_execute_command_line_with_handler() {
		File script = new File(getClass().getResource("/processes/success.sh").getFile());
		Command command = createUnixCommand(script.getAbsolutePath());
		OutputHandler handler = mock(OutputHandler.class);

		CommandExecutor executor = new DefaultCommandExecutor();
		CommandResult result = executor.execute(command, handler);

		verify(handler).readLine("Hello World");

		assertThat(result).isNotNull();
		assertThat(result.getExitStatus()).isZero();
		assertThat(result.getOutput()).isEqualTo("Hello World");
	}

	@Test
	public void it_should_start_command_line() {
		File script = new File(getClass().getResource("/processes/success.sh").getFile());
		Command command = createUnixCommand(script.getAbsolutePath());

		CommandExecutor executor = new DefaultCommandExecutor();
		CommandProcess process = executor.start(command);

		assertThat(process).isNotNull();

		String output = process.read();
		assertThat(output)
			.isNotNull()
			.isEqualTo("Hello World");
	}

	private Command createUnixCommand(String script) {
		Command command = mock(Command.class);
		when(command.getArguments()).thenReturn(asList(
			"/bin/sh",
			script
		));

		return command;
	}
}
