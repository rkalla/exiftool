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
import com.thebuzzmedia.exiftool.tests.builders.CommandResultBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readStaticPrivateField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("resource")
@RunWith(MockitoJUnitRunner.class)
public class ExifToolTest {

	@Rule
	public ExpectedException thrown = none();

	@Mock
	private CommandExecutor executor;

	@Mock
	private ExecutionStrategy strategy;

	@Captor
	private ArgumentCaptor<Command> cmdCaptor;

	@Captor
	private ArgumentCaptor<Version> versionCaptor;

	private CommandResult v9_36;

	private String path;

	@Before
	public void setUp() throws Exception {
		path = "exiftool";

		v9_36 = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(v9_36);

		// Clear cache before each test
		VersionCache cache = readStaticPrivateField(ExifTool.class, "cache", VersionCache.class);
		cache.clear();
		assertThat(cache).isNotNull();
		assertThat(cache.size()).isZero();
	}

	@Test
	public void it_should_not_create_exiftool_if_path_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("ExifTool path should not be null");
		new ExifTool(null, executor, strategy);
	}

	@Test
	public void it_should_not_create_exiftool_if_path_is_empty() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("ExifTool path should not be null");
		new ExifTool("", executor, strategy);
	}

	@Test
	public void it_should_not_create_exiftool_if_path_is_blank() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("ExifTool path should not be null");
		new ExifTool("  ", executor, strategy);
	}

	@Test
	public void it_should_not_create_exiftool_if_executor_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Executor should not be null");
		new ExifTool(path, null, strategy);
	}

	@Test
	public void it_should_not_create_exiftool_if_strategy_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Execution strategy should not be null");
		new ExifTool(path, executor, null);
	}

	@Test
	public void it_should_get_version_from_cache() throws Exception {
		VersionCache cache = readStaticPrivateField(ExifTool.class, "cache", VersionCache.class);

		cache.clear();
		assertThat(cache).isNotNull();
		assertThat(cache.size()).isZero();

		when(strategy.isSupported(any(Version.class))).thenReturn(true);

		ExifTool exifTool = new ExifTool(path, executor, strategy);
		assertThat(exifTool.getVersion()).isNotNull();

		cache = readStaticPrivateField(ExifTool.class, "cache", VersionCache.class);
		assertThat(cache).isNotNull();
		assertThat(cache.size()).isEqualTo(1);
	}

	@Test
	public void it_should_check_if_exiftool_is_running() throws Exception {
		when(strategy.isSupported(any(Version.class))).thenReturn(true);
		ExifTool exifTool = new ExifTool(path, executor, strategy);

		when(strategy.isRunning()).thenReturn(false);
		assertThat(exifTool.isRunning()).isFalse();
		verify(strategy).isRunning();

		reset(strategy);

		when(strategy.isRunning()).thenReturn(true);
		assertThat(exifTool.isRunning()).isTrue();
		verify(strategy).isRunning();
	}

	@Test
	public void it_should_close_exiftool() throws Exception {
		when(strategy.isSupported(any(Version.class))).thenReturn(true);
		ExifTool exifTool = new ExifTool(path, executor, strategy);
		exifTool.close();
		verify(strategy).close();
	}

	@Test
	public void it_should_get_exiftool_version() throws Exception {
		when(strategy.isSupported(any(Version.class))).thenReturn(true);
		ExifTool exifTool = new ExifTool(path, executor, strategy);
		Version version = exifTool.getVersion();
		assertThat(version).isEqualTo(new Version("9.36"));
	}

	@Test
	public void it_should_create_exiftool_instance_and_get_version() throws Exception {
		when(strategy.isSupported(any(Version.class))).thenReturn(true);
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

		verify(strategy).isSupported(versionCaptor.capture());

		Version version = versionCaptor.getValue();
		assertThat(version).isNotNull();
		assertThat(version.getMajor()).isEqualTo(9);
		assertThat(version.getMinor()).isEqualTo(36);
		assertThat(version.getPatch()).isEqualTo(0);

		verify(executor).execute(cmdCaptor.capture());
		assertThat(cmdCaptor.getValue()).isNotNull();
		assertThat(cmdCaptor.getValue().getArguments())
			.isNotNull()
			.isNotEmpty()
			.hasSize(2)
			.containsExactly(path, "-ver");
	}

	@Test
	public void it_should_not_create_exiftool_instance_if_strategy_is_not_supported() throws Exception {
		when(strategy.isSupported(any(Version.class))).thenReturn(false);

		thrown.expect(UnsupportedFeatureException.class);
		thrown.expectMessage(
			"Use of feature requires version 9.36.0 or higher of the native ExifTool program. " +
			"The version of ExifTool referenced by the path '" + path + "' is not high enough. " +
			"You can either upgrade the install of ExifTool or avoid using this feature to workaround this exception."
		);

		new ExifTool(path, executor, strategy);
	}
}
