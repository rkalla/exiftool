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
import com.thebuzzmedia.exiftool.process.executor.CommandExecutors;
import com.thebuzzmedia.exiftool.tests.builders.CommandResultBuilder;
import com.thebuzzmedia.exiftool.tests.junit.SystemPropertyRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CommandExecutors.class })
public class ExifToolBuilderTest {

	@Rule
	public SystemPropertyRule exifToolProp = new SystemPropertyRule("exiftool.path");

	private ExifToolBuilder builder;

	@Before
	public void setUp() {
		builder = new ExifToolBuilder();
		mockStatic(CommandExecutors.class);
	}

	@Test
	public void it_should_update_path() throws Exception {
		String path = "/foo";
		ExifToolBuilder res = builder.path(path);

		assertThat(res).isSameAs(builder);
		assertThat(readPrivateField(builder, "path", String.class))
			.isNotNull()
			.isEqualTo(path);
	}

	@Test
	public void it_should_update_executor() throws Exception {
		CommandExecutor executor = mock(CommandExecutor.class);
		ExifToolBuilder res = builder.executor(executor);

		assertThat(res).isSameAs(builder);
		assertThat(readPrivateField(builder, "executor", CommandExecutor.class))
			.isNotNull()
			.isEqualTo(executor);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void it_should_enable_or_disable_stay_open_feature() throws Exception {
		ExifToolBuilder r1 = builder.enableStayOpen();
		assertThat(r1).isSameAs(builder);

		Set<Feature> f1 = readPrivateField(builder, "features", Set.class);
		assertThat(f1)
			.isNotNull()
			.contains(Feature.STAY_OPEN);

		ExifToolBuilder r2 = builder.disableStayOpen();
		assertThat(r2).isSameAs(builder);

		Set<Feature> f2 = readPrivateField(builder, "features", Set.class);
		assertThat(f2)
			.isNotNull()
			.doesNotContain(Feature.STAY_OPEN);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void it_should_create_exiftool_with_custom_props() throws Exception {
		CommandExecutor executor = mock(CommandExecutor.class);
		String path = "/foo";

		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);

		ExifTool exifTool = builder
			.path(path)
			.executor(executor)
			.enableStayOpen()
			.build();

		assertThat(readPrivateField(exifTool, "path", String.class))
			.isNotNull()
			.isEqualTo(path);

		assertThat(readPrivateField(exifTool, "executor", CommandExecutor.class))
			.isNotNull()
			.isEqualTo(executor);

		Set<Feature> features = readPrivateField(exifTool, "features", Set.class);
		assertThat(features)
			.isNotNull()
			.isNotEmpty()
			.contains(Feature.STAY_OPEN);
	}

	@Test
	public void it_should_create_exiftool_and_get_path_from_system_property() throws Exception {
		String path = "/foo";
		System.setProperty("exiftool.path", path);

		CommandExecutor executor = mock(CommandExecutor.class);
		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);

		ExifTool exifTool = builder
			.executor(executor)
			.build();

		assertThat(readPrivateField(exifTool, "path", String.class))
			.isNotNull()
			.isEqualTo(path);
	}

	@Test
	public void it_should_create_exiftool_and_get_from_default() throws Exception {
		CommandExecutor executor = mock(CommandExecutor.class);
		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);

		ExifTool exifTool = builder
			.executor(executor)
			.build();

		assertThat(readPrivateField(exifTool, "path", String.class))
			.isNotNull()
			.isEqualTo("exiftool");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void it_should_not_enable_stay_open_by_default() throws Exception {
		CommandExecutor executor = mock(CommandExecutor.class);
		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);

		ExifTool exifTool = builder
			.executor(executor)
			.build();

		Set<Feature> features = readPrivateField(exifTool, "features", Set.class);
		assertThat(features)
			.isNotNull()
			.isEmpty();
	}

	@Test
	public void it_should_create_with_default_executor() throws Exception {
		CommandExecutor executor = mock(CommandExecutor.class);
		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);

		PowerMockito.when(CommandExecutors.newExecutor()).thenReturn(executor);

		ExifTool exifTool = builder
			.executor(executor)
			.build();

		assertThat(readPrivateField(exifTool, "executor", CommandExecutor.class))
			.isNotNull()
			.isEqualTo(executor);
	}
}
