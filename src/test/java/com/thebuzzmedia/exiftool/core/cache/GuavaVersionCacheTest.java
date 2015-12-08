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

import com.google.common.cache.Cache;
import com.thebuzzmedia.exiftool.Version;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.tests.builders.CommandResultBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.thebuzzmedia.exiftool.tests.ReflectionUtils.readPrivateField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuavaVersionCacheTest {

	@Mock
	private CommandExecutor executor;

	@Captor
	private ArgumentCaptor<Command> cmdCaptor;

	private String exifTool;

	@Before
	public void setUp() throws Exception {
		exifTool = "exifTool";

		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);
	}

	@Test
	public void it_should_load_cache() throws Exception {
		GuavaVersionCache cache = new GuavaVersionCache();
		Version version = cache.load(exifTool, executor);

		assertThat(version).isEqualTo(new Version("9.36.0"));
		verify(executor).execute(cmdCaptor.capture());
		assertThat(cmdCaptor.getValue().getArguments())
			.hasSize(2)
			.containsExactly(exifTool, "-ver");
	}

	@Test
	public void it_should_load_cache_once() throws Exception {
		GuavaVersionCache cache = new GuavaVersionCache();

		Version v1 = cache.load(exifTool, executor);
		assertThat(v1).isEqualTo(new Version("9.36.0"));
		verify(executor).execute(any(Command.class));

		reset(executor);

		Version v2 = cache.load(exifTool, executor);
		assertThat(v2).isSameAs(v1);
		verify(executor, never()).execute(any(Command.class));
	}

	@Test
	public void it_should_clear_cache() throws Exception {
		GuavaVersionCache cache = new GuavaVersionCache();

		cache.load(exifTool, executor);
		assertThat(readPrivateField(cache, "cache", Cache.class).size())
			.isNotZero()
			.isEqualTo(1);

		cache.clear();
		assertThat(readPrivateField(cache, "cache", Cache.class).size())
			.isZero();
	}
}
