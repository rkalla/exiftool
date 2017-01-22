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
import com.thebuzzmedia.exiftool.Version;
import com.thebuzzmedia.exiftool.core.StandardFormat;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.tests.FileUtils;
import com.thebuzzmedia.exiftool.tests.junit.OpenedProcessRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Map;

import static com.thebuzzmedia.exiftool.tests.TestConstants.IS_WINDOWS;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractExifToolIT {

	private static final String PATH = path().getAbsolutePath();

	private ExifTool exifTool;

	private ExifTool exifToolStayOpen;

	private ExifTool exifToolPool;

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	@Rule
	public OpenedProcessRule processes = new OpenedProcessRule(PATH);

	@Before
	public void setUp() {
		File path = path();

		exifTool = new ExifToolBuilder()
			.withPath(path.getAbsolutePath())
			.build();

		exifToolStayOpen = new ExifToolBuilder()
			.withPath(path)
			.enableStayOpen()
			.build();

		exifToolPool = new ExifToolBuilder()
				.withPath(path)
				.withPoolSize(2)
				.build();
	}

	@After
	public void tearDown() throws Exception {
		exifTool.close();
		exifToolStayOpen.close();
		exifToolPool.close();
	}

	@Test
	public void testGetVersion() {
		assertThat(exifTool.getVersion()).isEqualTo(new Version("10.16.0"));
		assertThat(exifToolStayOpen.getVersion()).isEqualTo(new Version("10.16.0"));
		assertThat(exifToolPool.getVersion()).isEqualTo(new Version("10.16.0"));
	}

	@Test
	public void testGetImageMeta() throws Exception {
		verifyGetMeta(exifTool);
	}

	@Test
	public void testGetImageMeta_stay_open() throws Exception {
		verifyGetMeta(exifToolStayOpen);
	}

	@Test
	public void testGetImageMeta_Pool() throws Exception {
		verifyGetMeta(exifToolPool);
	}

	@Test
	public void testSetImageMeta() throws Exception {
		verifySetMeta(exifTool);
	}

	@Test
	public void testSetImageMeta_stay_open() throws Exception {
		verifySetMeta(exifToolStayOpen);
	}

	@Test
	public void testSetImageMeta_pool() throws Exception {
		verifySetMeta(exifToolPool);
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
		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.hasSize(expectations.size());

		for (Map.Entry<Tag, String> entry : results.entrySet()) {
			Tag tag = entry.getKey();
			assertThat(expectations)
				.overridingErrorMessage(String.format("Result should contain tag %s", tag))
				.containsKey(tag);

			String result = entry.getValue();
			String expectation = expectations.get(tag);
			assertThat(result)
				.overridingErrorMessage(String.format("Result should contain tag %s with value %s", tag, expectation))
				.isEqualToIgnoringCase(expectation);
		}
	}

	protected abstract String image();

	protected abstract Map<Tag, String> expectations();

	protected abstract Map<Tag, String> updateTags();

	private static File path() {
		String relativePath = "/exiftool-10_16/" + (IS_WINDOWS ? "windows/exiftool.exe" : "unix/exiftool");
		File file = new File(AbstractExifToolIT.class.getResource(relativePath).getFile());

		if (!file.setExecutable(true)) {
			throw new UnsupportedOperationException(String.format("Cannot make %s executable", relativePath));
		}

		if (!file.setReadable(true)) {
			throw new UnsupportedOperationException(String.format("Cannot make %s readable", relativePath));
		}

		return file;
	}
}
