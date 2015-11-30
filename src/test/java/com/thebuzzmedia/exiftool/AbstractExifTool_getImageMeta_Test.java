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
import com.thebuzzmedia.exiftool.tests.builders.CommandResultBuilder;
import com.thebuzzmedia.exiftool.tests.builders.FileBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Map;

import static com.thebuzzmedia.exiftool.tests.MapUtils.newMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public abstract class AbstractExifTool_getImageMeta_Test {

	@Rule
	public ExpectedException thrown = none();

	private CommandExecutor executor;

	private ExifTool exifTool;

	@Before
	public void setUp() {
		executor = mock(CommandExecutor.class);

		CommandResult result = new CommandResultBuilder()
			.output("9.36")
			.build();

		when(executor.execute(any(Command.class))).thenReturn(result);

		exifTool = createExifTool(executor);

		reset(executor);
	}

	@After
	public void tearDown() throws Exception {
		exifTool.close();
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

	@Test
	public void it_should_fail_if_tags_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Tags cannot be null and must contain 1 or more Tag to query the image for.");
		exifTool.getImageMeta(mock(File.class), Format.HUMAN_READABLE, null);
	}

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
		Format format = Format.HUMAN_READABLE;
		File image = new FileBuilder("foo.png").build();
		Map<Tag, String> tags = newMap(
			Tag.ARTIST, "bar",
			Tag.COMMENT, "foo"
		);

		mockExecutor(executor, tags);

		// When
		Map<Tag, String> results = exifTool.getImageMeta(image, format, Tag.ARTIST, Tag.COMMENT);

		// Then
		verifyExecution(exifTool, executor, image, format, tags);

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.hasSize(tags.size())
			.isEqualTo(tags);
	}

	@Test
	public void it_should_get_image_metadata_in_numeric_format() throws Exception {
		// Given
		Format format = Format.NUMERIC;
		File image = new FileBuilder("foo.png").build();
		Map<Tag, String> tags = newMap(
			Tag.ARTIST, "foo",
			Tag.COMMENT, "bar"
		);

		mockExecutor(executor, tags);

		// When
		Map<Tag, String> results = exifTool.getImageMeta(image, format, Tag.ARTIST, Tag.COMMENT);

		// Then
		verifyExecution(exifTool, executor, image, format, tags);

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.hasSize(tags.size())
			.isEqualTo(tags);
	}

	/**
	 * Create exiftool instance to test.
	 *
	 * @param executor Executor that should be used by exiftool instance.
	 * @return Exiftool instance to test.
	 */
	protected abstract ExifTool createExifTool(CommandExecutor executor);

	/**
	 * Mock executor used by {@link com.thebuzzmedia.exiftool.ExifTool#getImageMeta(java.io.File, com.thebuzzmedia.exiftool.Format, com.thebuzzmedia.exiftool.Tag...)}.
	 *
	 * @param executor Mocked executor.
	 * @param tags Tested tags.
	 * @throws Exception
	 */
	protected abstract void mockExecutor(CommandExecutor executor, Map<Tag, String> tags) throws Exception;

	/**
	 * Check valid execution of {@link com.thebuzzmedia.exiftool.ExifTool#getImageMeta(java.io.File, com.thebuzzmedia.exiftool.Format, com.thebuzzmedia.exiftool.Tag...)}.
	 *
	 * @param exifTool Tested exiftool instance.
	 * @param executor Mocked executor.
	 * @param image Tested image.
	 * @param format Used format.
	 * @param results Tested tags.
	 * @throws Exception
	 */
	protected abstract void verifyExecution(ExifTool exifTool, CommandExecutor executor, File image, Format format, Map<Tag, String> results) throws Exception;
}
