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

import com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.process.executor.CommandExecutors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommandExecutors.class)
public class ExifTool_Constructor_Test {

	@Rule
	public ExpectedException thrown = none();

	private CommandExecutor executor;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(CommandExecutors.class);

		executor = mock(CommandExecutor.class);
		PowerMockito.when(CommandExecutors.newExecutor()).thenReturn(executor);
	}

	@Test
	public void it_should_initialize_exiftool_without_features() {
		CommandResult resultVersion = mock(CommandResult.class);
		when(resultVersion.getOutput()).thenReturn("9.36");
		when(resultVersion.getExitStatus()).thenReturn(0);
		when(resultVersion.isSuccess()).thenReturn(true);
		when(executor.execute(any(Command.class))).thenReturn(resultVersion);

		ExifTool exifTool = new ExifTool();

		assertThat(exifTool.getVersion())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("9.36");

		assertThat(exifTool.isFeatureEnabled(Feature.STAY_OPEN))
			.isFalse();

		ArgumentCaptor<Command> argCommand = ArgumentCaptor.forClass(Command.class);
		verify(executor).execute(argCommand.capture());

		Command command = argCommand.getValue();
		assertThat(command.toString()).isEqualTo("exiftool -ver");
	}

	@Test
	public void it_should_initialize_exiftool_with_stay_open_feature() {
		CommandResult resultVersion = mock(CommandResult.class);
		when(resultVersion.getOutput()).thenReturn("9.36");
		when(resultVersion.getExitStatus()).thenReturn(0);
		when(resultVersion.isSuccess()).thenReturn(true);
		when(executor.execute(any(Command.class))).thenReturn(resultVersion);

		ExifTool exifTool = new ExifTool(Feature.STAY_OPEN);

		assertThat(exifTool.getVersion())
			.isNotNull()
			.isNotEmpty()
			.isEqualTo("9.36");

		assertThat(exifTool.isFeatureEnabled(Feature.STAY_OPEN)).isTrue();

		ArgumentCaptor<Command> argCommand = ArgumentCaptor.forClass(Command.class);
		verify(executor).execute(argCommand.capture());

		Command command = argCommand.getValue();
		assertThat(command.toString()).isEqualTo("exiftool -ver");
	}

	@Test
	public void it_should_not_initialize_exiftool_with_stay_open_feature_if_it_is_not_supported() {
		thrown.expect(UnsupportedFeatureException.class);
		thrown.expectMessage(
			"Use of feature [STAY_OPEN] requires version 8.36 or higher of the native ExifTool program. " +
			"The version of ExifTool referenced by the system property 'exiftool.path' is not high enough. " +
			"You can either upgrade the install of ExifTool or avoid using this feature to workaround this exception."
		);

		CommandResult resultVersion = mock(CommandResult.class);
		when(resultVersion.getOutput()).thenReturn("5.0");
		when(resultVersion.getExitStatus()).thenReturn(0);
		when(resultVersion.isSuccess()).thenReturn(true);
		when(executor.execute(any(Command.class))).thenReturn(resultVersion);

		new ExifTool(Feature.STAY_OPEN);
	}

	@Test
	public void it_should_check_if_feature_is_enabled() {
		CommandResult resultVersion = mock(CommandResult.class);
		when(resultVersion.getOutput()).thenReturn("9.0");
		when(resultVersion.getExitStatus()).thenReturn(0);
		when(resultVersion.isSuccess()).thenReturn(true);
		when(executor.execute(any(Command.class))).thenReturn(resultVersion);

		ExifTool exifTool = new ExifTool(Feature.STAY_OPEN);

		assertThat(exifTool.isFeatureEnabled(Feature.STAY_OPEN)).isTrue();
	}

	@Test
	public void it_should_check_if_feature_is_not_enabled() {
		CommandResult resultVersion = mock(CommandResult.class);
		when(resultVersion.getOutput()).thenReturn("9.0");
		when(resultVersion.getExitStatus()).thenReturn(0);
		when(resultVersion.isSuccess()).thenReturn(true);
		when(executor.execute(any(Command.class))).thenReturn(resultVersion);

		ExifTool exifTool = new ExifTool();

		assertThat(exifTool.isFeatureEnabled(Feature.STAY_OPEN)).isFalse();
	}

	@Test
	public void it_should_fail_check_if_feature_is_not_enabled_with_null() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Feature cannot be null");

		CommandResult resultVersion = mock(CommandResult.class);
		when(resultVersion.getOutput()).thenReturn("9.0");
		when(resultVersion.getExitStatus()).thenReturn(0);
		when(resultVersion.isSuccess()).thenReturn(true);
		when(executor.execute(any(Command.class))).thenReturn(resultVersion);

		ExifTool exifTool = new ExifTool();
		exifTool.isFeatureEnabled(null);
	}
}
