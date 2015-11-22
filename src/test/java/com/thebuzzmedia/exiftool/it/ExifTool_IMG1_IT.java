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

import com.thebuzzmedia.exiftool.Tag;

import java.util.HashMap;
import java.util.Map;

public class ExifTool_IMG1_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "canon-60d-warrior-dash.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.METERING_MODE, "Evaluative");
				put(Tag.SHUTTER_SPEED, "1/4096");
				put(Tag.EXPOSURE_TIME, "1/4000");
				put(Tag.EXIF_VERSION, "0230");
				put(Tag.COLOR_SPACE, "sRGB");
				put(Tag.AUTHOR, "Test Author");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.COMMENT, "Test comments");
				put(Tag.DATE_TIME_ORIGINAL, "2011:04:30 08:35:00");
				put(Tag.FOCAL_LENGTH, "35.0 mm");
				put(Tag.ISO, "100");
				put(Tag.EXPOSURE_PROGRAM, "Aperture-priority AE");
				put(Tag.RATING, "4");
				put(Tag.LENS_MODEL, "EF16-35mm f/2.8L II USM");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.CREATOR, "Test Author");
				put(Tag.Y_RESOLUTION, "72");
				put(Tag.SUB_SEC_TIME_ORIGINAL, "14");
				put(Tag.ORIENTATION, "Horizontal (normal)");
				put(Tag.LENS_ID, "Canon EF 16-35mm f/2.8L II");
				put(Tag.MAKE, "Canon");
				put(Tag.MODEL, "Canon EOS 60D");
				put(Tag.IMAGE_WIDTH, "5184");
				put(Tag.SUBJECT, "Test Subject");
				put(Tag.WHITE_BALANCE, "Auto");
				put(Tag.ARTIST, "Test Author");
				put(Tag.TITLE, "Test Title");
				put(Tag.KEYWORDS, "tag1 tag2 tag3");
				put(Tag.RATING_PERCENT, "75");
				put(Tag.EXPOSURE_COMPENSATION, "-1/3");
				put(Tag.IMAGE_HEIGHT, "3456");
				put(Tag.CONTRAST, "Normal");
				put(Tag.FLASH, "Off, Did not fire");
				put(Tag.SHARPNESS, "3");
				put(Tag.SATURATION, "Normal");
				put(Tag.X_RESOLUTION, "72");
				put(Tag.APERTURE, "2.8");
			}
		};
	}
}
