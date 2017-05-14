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

package com.thebuzzmedia.exiftool.core.cache;

import static com.thebuzzmedia.exiftool.tests.TestConstants.EXIF_TOOL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.thebuzzmedia.exiftool.Version;
import com.thebuzzmedia.exiftool.exceptions.ExifToolNotFoundException;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.tests.builders.CommandResultBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class VersionCallableTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Captor
	private ArgumentCaptor<Command> cmdCaptor;

	private String exifTool;

	@Before
	public void setUp() {
		exifTool = "exifTool";
	}

	@Test
	public void it_should_parse_version() throws Exception {
		CommandExecutor executor = mock(CommandExecutor.class);
		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);

		VersionCallable callable = new VersionCallable(exifTool, executor);
		Version version = callable.call();

		verify(executor).execute(cmdCaptor.capture());

		Command cmd = cmdCaptor.getValue();
		assertThat(cmd).isNotNull();
		assertThat(cmd.getArguments())
			.isNotNull()
			.isNotEmpty()
			.hasSize(2)
			.containsExactly(exifTool, "-ver");

		assertThat(version)
			.isNotNull()
			.isEqualTo(new Version("9.36.0"));
	}

	@Test
	public void it_should_parse_version_and_throw_exception_with_failure() throws Exception {
		CommandExecutor executor = mock(CommandExecutor.class);
		CommandResult result = new CommandResultBuilder()
			.success(false)
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);

		VersionCallable callable = new VersionCallable(exifTool, executor);

		thrown.expect(ExifToolNotFoundException.class);
		thrown.expectMessage("Cannot find exiftool from path: exifTool");

		callable.call();
	}

	@Test
	public void it_should_try_to_parse_version_and_throw_ex() throws Exception {
		IOException ex = new IOException();
		CommandExecutor executor = mock(CommandExecutor.class);
		when(executor.execute(any(Command.class))).thenThrow(ex);

		VersionCallable callable = new VersionCallable(exifTool, executor);

		thrown.expect(ExifToolNotFoundException.class);
		thrown.expectCause(is(ex));

		callable.call();
	}

	@Test
	public void it_should_parse_version_and_handle_null_reference() throws Exception {
		String path = EXIF_TOOL.getAbsolutePath();
		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		CommandExecutor executor = mock(CommandExecutor.class);
		when(executor.execute(any(Command.class))).thenReturn(result);

		VersionCallable callable = new VersionCallable(path, executor);

		// Set it to null
		executor = null;

		// Run GC, then sleep for one second to let GC run
		System.gc();
		Thread.sleep(1000);

		Version version = callable.call();

		assertThat(version).isNotNull();
		assertThat(version.getMajor()).isEqualTo(10);
		assertThat(version.getMinor()).isEqualTo(16);
		assertThat(version.getPatch()).isEqualTo(0);
	}
}
