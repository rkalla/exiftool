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

package com.thebuzzmedia.exiftool.it.img;

import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;

import java.util.HashMap;
import java.util.Map;

import static com.thebuzzmedia.exiftool.tests.TestConstants.IS_WINDOWS;

@SuppressWarnings("serial")
public class ExifTool_IMG7_IT extends AbstractExifToolImgIT {

	@Override
	protected String image() {
		return "palm-pre-menu.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		// Results extracted with exiftool 10.07.
		return new HashMap<Tag, String>() {
			{
				put(StandardTag.MAKE, "Palm");
				put(StandardTag.EXPOSURE_PROGRAM, "Program AE");
				put(StandardTag.X_RESOLUTION, "72");
				put(StandardTag.Y_RESOLUTION, "72");
				put(StandardTag.EXPOSURE_TIME, "1/65536000");
				put(StandardTag.EXIF_VERSION, "0220");
				put(StandardTag.IMAGE_HEIGHT, "2032");
				put(StandardTag.ORIENTATION, "Horizontal (normal)");
				put(StandardTag.GPS_LATITUDE_REF, "North");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.IMAGE_WIDTH, "1520");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.GPS_LATITUDE, "32 deg 48' 4.00\" N");
				put(StandardTag.FNUMBER, "2.4");

				// Weird, but there is specific results for windows / unix
				put(StandardTag.FOCAL_LENGTH, IS_WINDOWS ? "0.0 mm" : "inf mm");

				put(StandardTag.GPS_LONGITUDE, "117 deg 13' 33.00\" W");
				put(StandardTag.APERTURE, "1.1");
				put(StandardTag.MODEL, "Pre");
				put(StandardTag.GPS_LONGITUDE_REF, "West");
				put(StandardTag.FLASH, "Auto, Did not fire");
				put(StandardTag.FILE_SIZE, "1017 kB");
			}
		};
	}

	@Override
	protected Map<Tag, String> updateTags() {
		return new HashMap<Tag, String>() {{
			put(StandardTag.FLASH, "Auto, Did not fire");
		}};
	}
}
