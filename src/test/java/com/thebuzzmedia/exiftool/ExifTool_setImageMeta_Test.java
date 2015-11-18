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

import com.thebuzzmedia.exiftool.exceptions.UnwritableFileException;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.process.executor.CommandExecutors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommandExecutors.class)
public class ExifTool_setImageMeta_Test {

	@Rule
	public ExpectedException thrown = none();

	private ExifTool exifTool;

	private CommandExecutor executor;

	private Map<Tag, String> tags;

	@Before
	public void setUp() {
		executor = mock(CommandExecutor.class);

		PowerMockito.mockStatic(CommandExecutors.class);
		PowerMockito.when(CommandExecutors.newExecutor()).thenReturn(executor);

		CommandResult resultVersion = mock(CommandResult.class);
		when(resultVersion.getOutput()).thenReturn("9.36");
		when(resultVersion.getExitStatus()).thenReturn(0);
		when(resultVersion.isSuccess()).thenReturn(true);
		when(executor.execute(any(Command.class))).thenReturn(resultVersion);

		exifTool = new ExifTool();
		assertThat(exifTool.getVersion()).isEqualTo("9.36");

		reset(executor);

		tags = new HashMap<Tag, String>();
		tags.put(Tag.APERTURE, "foo");
		tags.put(Tag.ARTIST, "bar");
	}

	@Test
	public void it_should_fail_if_image_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Image cannot be null and must be a valid stream of image data.");

		exifTool.setImageMeta(null, Format.HUMAN_READABLE, tags);
	}

	@Test
	public void it_should_fail_if_format_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Format cannot be null.");

		exifTool.setImageMeta(mock(File.class), null, tags);
	}

	@Test
	public void it_should_fail_if_tags_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Tags cannot be null and must contain 1 or more Tag to query the image for.");

		exifTool.setImageMeta(mock(File.class), Format.HUMAN_READABLE, null);
	}

	@Test
	public void it_should_fail_if_tags_is_empty() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Tags cannot be null and must contain 1 or more Tag to query the image for.");

		exifTool.setImageMeta(mock(File.class), Format.HUMAN_READABLE, Collections.<Tag, String>emptyMap());
	}

	@Test
	public void it_should_fail_with_unknown_file() throws Exception {
		thrown.expect(UnwritableFileException.class);
		thrown.expectMessage("Unable to read the given image [/foo.png], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		File image = mock(File.class);
		when(image.getPath()).thenReturn("/foo.png");
		when(image.exists()).thenReturn(false);
		when(image.canWrite()).thenReturn(true);
		when(image.toString()).thenCallRealMethod();

		exifTool.setImageMeta(image, Format.HUMAN_READABLE, tags);
	}

	@Test
	public void it_should_fail_with_non_writable_file() throws Exception {
		thrown.expect(UnwritableFileException.class);
		thrown.expectMessage("Unable to read the given image [/foo.png], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		File image = mock(File.class);
		when(image.getPath()).thenReturn("/foo.png");
		when(image.exists()).thenReturn(true);
		when(image.canWrite()).thenReturn(false);
		when(image.toString()).thenCallRealMethod();

		exifTool.setImageMeta(image, Format.HUMAN_READABLE, tags);
	}
}
