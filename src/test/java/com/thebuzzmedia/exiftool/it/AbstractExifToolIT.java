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
import com.thebuzzmedia.exiftool.Feature;
import com.thebuzzmedia.exiftool.Format;
import com.thebuzzmedia.exiftool.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractExifToolIT {

	private ExifTool exifTool;
	private ExifTool exifToolStayOpen;

	@Before
	public void setUp() {
		exifTool = new ExifTool();
		exifToolStayOpen = new ExifTool(Feature.STAY_OPEN);
	}

	@After
	public void tearDown() {
		exifTool.close();
		exifToolStayOpen.close();
	}

	@Test
	public void testImage() throws Exception {
		check(exifTool);
	}

	@Test
	public void testImage_StayOpen() throws Exception {
		check(exifToolStayOpen);
	}

	private void check(ExifTool exifTool) throws Exception {
		File file = new File("src/test/resources/images/" + image());
		Map<Tag, String> results = exifTool.getImageMeta(file, Format.HUMAN_READABLE, Tag.values());
		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(expectations());
	}

	protected abstract String image();

	protected abstract Map<Tag, String> expectations();
}
