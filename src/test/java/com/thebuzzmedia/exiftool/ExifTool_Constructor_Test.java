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

package com.thebuzzmedia.exiftool;

import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.tests.builders.CommandResultBuilder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExifTool_Constructor_Test {

	@Test
	public void it_should_create_exiftool_instance_and_get_version() throws Exception {
		String path = "exiftool";
		CommandExecutor executor = mock(CommandExecutor.class);
		ExecutionStrategy strategy = mock(ExecutionStrategy.class);

		CommandResult v9_36 = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(v9_36);

		ExifTool exifTool = new ExifTool(path, executor, strategy);

		assertThat(readPrivateField(exifTool, "path", String.class))
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(path);

		assertThat(readPrivateField(exifTool, "executor", CommandExecutor.class))
			.isNotNull()
			.isEqualTo(executor);

		assertThat(readPrivateField(exifTool, "strategy", ExecutionStrategy.class))
			.isNotNull()
			.isEqualTo(strategy);

		ArgumentCaptor<Command> cmdCaptor = ArgumentCaptor.forClass(Command.class);
		verify(executor).execute(cmdCaptor.capture());

		Command cmd = cmdCaptor.getValue();
		assertThat(cmd).isNotNull();
		assertThat(cmd.getArguments())
			.isNotNull()
			.isNotEmpty()
			.hasSize(2)
			.containsExactly(path, "-ver");
	}
}
