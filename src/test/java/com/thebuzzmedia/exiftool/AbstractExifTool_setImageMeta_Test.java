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
import com.thebuzzmedia.exiftool.tests.builders.CommandResultBuilder;
import com.thebuzzmedia.exiftool.tests.builders.FileBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import static com.thebuzzmedia.exiftool.tests.MapUtils.newMap;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public abstract class AbstractExifTool_setImageMeta_Test {

	@Rule
	public ExpectedException thrown = none();

	private CommandExecutor executor;

	private ExifTool exifTool;

	@Before
	public void setUp() throws Exception {
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
		exifTool.setImageMeta(null, Format.HUMAN_READABLE, newMap(Tag.APERTURE, "foo", Tag.ARTIST, "bar"));
	}

	@Test
	public void it_should_fail_if_format_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Format cannot be null.");
		exifTool.setImageMeta(mock(File.class), null, newMap(Tag.APERTURE, "foo", Tag.ARTIST, "bar"));
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
		thrown.expectMessage("Unable to read the given image [/tmp/foo.png], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		File image = new FileBuilder("foo.png")
			.exists(false)
			.build();

		exifTool.setImageMeta(image, Format.HUMAN_READABLE, newMap(Tag.APERTURE, "foo", Tag.ARTIST, "bar"));
	}

	@Test
	public void it_should_fail_with_non_writable_file() throws Exception {
		thrown.expect(UnwritableFileException.class);
		thrown.expectMessage("Unable to read the given image [/tmp/foo.png], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		File image = new FileBuilder("foo.png")
			.canWrite(false)
			.build();

		exifTool.setImageMeta(image, Format.HUMAN_READABLE, newMap(Tag.APERTURE, "foo", Tag.ARTIST, "bar"));
	}

	@Test
	public void it_should_set_image_meta_data() throws Exception {
		final File image = new FileBuilder("foo.png").build();
		final Format format = Format.HUMAN_READABLE;
		final Map<Tag, String> tags = newMap(Tag.APERTURE, "foo", Tag.ARTIST, "bar");

		mockExecutor(executor);

		exifTool.setImageMeta(image, format, tags);

		verifyExecution(exifTool, executor, image, format, tags);
	}

	@Test
	public void it_should_set_image_meta_data_in_numeric_format() throws Exception {
		final File image = new FileBuilder("foo.png").build();
		final Map<Tag, String> tags = newMap(Tag.APERTURE, "foo", Tag.ARTIST, "bar");
		final Format format = Format.NUMERIC;

		mockExecutor(executor);

		exifTool.setImageMeta(image, tags);

		verifyExecution(exifTool, executor, image, format, tags);
	}

	/**
	 * Create ExifTool instance.
	 *
	 * @param executor Executor that should be used by exiftool instance.
	 * @return New instance.
	 */
	protected abstract ExifTool createExifTool(CommandExecutor executor) throws Exception;

	/**
	 * Mock execution of {@link com.thebuzzmedia.exiftool.ExifTool#setImageMeta(java.io.File, com.thebuzzmedia.exiftool.Format, java.util.Map)}.
	 *
	 * @param executor Executor to mock.
	 */
	protected abstract void mockExecutor(CommandExecutor executor) throws Exception;

	/**
	 * Check valid execution of {@link com.thebuzzmedia.exiftool.ExifTool#setImageMeta(java.io.File, com.thebuzzmedia.exiftool.Format, java.util.Map)}.
	 *
	 * @param exifTool Tested exiftool wrapper.
	 * @param executor Used executor.
	 * @param format Used format.
	 * @param image Tested image.
	 * @param tags Tested tags.
	 */
	protected abstract void verifyExecution(ExifTool exifTool, CommandExecutor executor, File image, Format format, Map<Tag, String> tags) throws Exception;
}
