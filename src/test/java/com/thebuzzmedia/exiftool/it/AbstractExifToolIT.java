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

package com.thebuzzmedia.exiftool.it;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardFormat;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.exceptions.AbstractExifException;
import com.thebuzzmedia.exiftool.tests.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import static com.thebuzzmedia.exiftool.tests.TestConstants.IS_WINDOWS;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractExifToolIT {

	private ExifTool exifTool;

	private ExifTool exifToolStayOpen;

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	@Before
	public void setUp() {
		File path = path();

		exifTool = new ExifToolBuilder()
			.withPath(path)
			.build();

		exifToolStayOpen = new ExifToolBuilder()
			.withPath(path)
			.enableStayOpen()
			.build();
	}

	@After
	public void tearDown() throws Exception {
		exifTool.close();
		exifToolStayOpen.close();
	}

	@Test
	public void testGetImageMeta() throws Exception {
		verifyGetMeta(exifTool);
	}

	@Test
	public void testGetImageMeta_StayOpen() throws Exception {
		verifyGetMeta(exifToolStayOpen);
	}

	@Test
	public void testSetImageMeta() throws Exception {
		verifySetMeta(exifTool);
	}

	@Test
	public void testSetImageMeta_stay_open() throws Exception {
		verifySetMeta(exifToolStayOpen);
	}

	private void verifyGetMeta(ExifTool exifTool) throws Exception {
		File file = new File("src/test/resources/images/" + image());
		checkMeta(exifTool, file, StandardTag.values(), expectations());
	}

	private void verifySetMeta(ExifTool exifTool) throws Exception {
		File file = new File("src/test/resources/images/" + image());
		File folder = tmp.newFolder("exif");
		File tmpCopy = FileUtils.copy(file, folder);
		Map<Tag, String> meta = updateTags();

		exifTool.setImageMeta(tmpCopy, StandardFormat.HUMAN_READABLE, meta);

		Tag[] tags = new Tag[meta.size()];
		int i = 0;
		for (Tag t : meta.keySet()) {
			tags[i] = t;
			i++;
		}

		checkMeta(exifTool, tmpCopy, tags, meta);
	}

	private void checkMeta(ExifTool exifTool, File image, Tag[] tags, Map<Tag, String> expectations) throws Exception {
		Map<Tag, String> results = exifTool.getImageMeta(image, StandardFormat.HUMAN_READABLE, asList(tags));
		assertThat(new TreeMap<Tag, String>(results))
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(new TreeMap<Tag, String>(expectations));
	}

	protected abstract String image();

	protected abstract Map<Tag, String> expectations();

	protected abstract Map<Tag, String> updateTags();

	private static File path() {
		String relativePath = "/exiftool-10_07/";
		if (IS_WINDOWS) {
			relativePath += "windows/exiftool.exe";
		} else {
			relativePath += "unix/exiftool";
		}

		File file = new File(AbstractExifException.class.getResource(relativePath).getFile());
		file.setExecutable(true);
		file.setReadable(true);
		return file;
	}
}
