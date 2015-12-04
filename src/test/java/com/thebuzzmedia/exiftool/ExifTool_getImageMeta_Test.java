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

import com.thebuzzmedia.exiftool.exceptions.UnreadableFileException;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.CommandExecutor;
import com.thebuzzmedia.exiftool.process.CommandResult;
import com.thebuzzmedia.exiftool.process.OutputHandler;
import com.thebuzzmedia.exiftool.tests.builders.CommandResultBuilder;
import com.thebuzzmedia.exiftool.tests.builders.FileBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.thebuzzmedia.exiftool.tests.MapUtils.newMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExifTool_getImageMeta_Test {

	@Rule
	public ExpectedException thrown = none();

	private String path;

	@Mock
	private CommandExecutor executor;

	@Mock
	private ExifToolStrategy strategy;

	@Captor
	private ArgumentCaptor<List<String>> argsCaptor;

	private ExifTool exifTool;

	@Before
	public void setUp() {
		path = "exiftool";

		CommandResult cmd = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(cmd);

		exifTool = new ExifTool(path, executor, strategy);

		reset(executor);
	}

	@Test
	public void it_should_fail_if_image_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Image cannot be null and must be a valid stream of image data.");
		exifTool.getImageMeta(null, Format.HUMAN_READABLE, Tag.values());
	}

	@Test
	public void it_should_fail_if_format_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Format cannot be null.");
		exifTool.getImageMeta(mock(File.class), null, Tag.values());
	}

	@SuppressWarnings("NullArgumentToVariableArgMethod")
	@Test
	public void it_should_fail_if_tags_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Tags cannot be null and must contain 1 or more Tag to query the image for.");
		exifTool.getImageMeta(mock(File.class), Format.HUMAN_READABLE, null);
	}

	@SuppressWarnings("RedundantArrayCreation")
	@Test
	public void it_should_fail_if_tags_is_empty() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Tags cannot be null and must contain 1 or more Tag to query the image for.");
		exifTool.getImageMeta(mock(File.class), Format.HUMAN_READABLE, new Tag[]{ });
	}

	@Test
	public void it_should_fail_with_unknown_file() throws Exception {
		thrown.expect(UnreadableFileException.class);
		thrown.expectMessage("Unable to read the given image [/tmp/foo.png], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		File image = new FileBuilder("foo.png")
			.exists(false)
			.build();

		exifTool.getImageMeta(image, Format.HUMAN_READABLE, Tag.values());
	}

	@Test
	public void it_should_fail_with_non_readable_file() throws Exception {
		thrown.expect(UnreadableFileException.class);
		thrown.expectMessage("Unable to read the given image [/tmp/foo.png], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		File image = new FileBuilder("foo.png")
			.canRead(false)
			.build();

		exifTool.getImageMeta(image, Format.HUMAN_READABLE, Tag.values());
	}

	@Test
	public void it_should_get_image_metadata() throws Exception {
		// Given
		final Format format = Format.HUMAN_READABLE;
		final File image = new FileBuilder("foo.png").build();
		final Map<Tag, String> tags = newMap(
			Tag.ARTIST, "bar",
			Tag.COMMENT, "foo"
		);

		doAnswer(new ReadTagsAnswer(tags, "{ready}"))
			.when(strategy).execute(same(executor), same(path), anyListOf(String.class), any(OutputHandler.class));

		// When
		Map<Tag, String> results = exifTool.getImageMeta(image, format, Tag.ARTIST, Tag.COMMENT);

		// Then
		verify(strategy).execute(same(executor), same(path), argsCaptor.capture(), any(OutputHandler.class));

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.hasSize(tags.size())
			.isEqualTo(tags);
	}

	@Test
	public void it_should_get_image_metadata_in_numeric_format() throws Exception {
		// Given
		final Format format = Format.NUMERIC;
		final File image = new FileBuilder("foo.png").build();
		final Map<Tag, String> tags = newMap(
			Tag.ARTIST, "foo",
			Tag.COMMENT, "bar"
		);

		doAnswer(new ReadTagsAnswer(tags, "{ready}"))
			.when(strategy).execute(same(executor), same(path), anyListOf(String.class), any(OutputHandler.class));

		// When
		Map<Tag, String> results = exifTool.getImageMeta(image, format, Tag.ARTIST, Tag.COMMENT);

		// Then
		verify(strategy).execute(same(executor), same(path), argsCaptor.capture(), any(OutputHandler.class));

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.hasSize(tags.size())
			.isEqualTo(tags);
	}

	private static class ReadTagsAnswer implements Answer {
		private final Map<Tag, String> tags;

		private final String end;

		public ReadTagsAnswer(Map<Tag, String> tags, String end) {
			this.tags = tags;
			this.end = end;
		}

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			OutputHandler handler = (OutputHandler) invocation.getArguments()[3];

			// Read tags
			for (Map.Entry<Tag, String> entry : tags.entrySet()) {
				handler.readLine(entry.getKey().getName() + ": " + entry.getValue());
			}

			// Read last line
			handler.readLine(end);

			return null;
		}
	}
}
