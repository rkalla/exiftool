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

@SuppressWarnings("serial")
public class ExifTool_IMG11_IT extends AbstractExifToolImgIT {

	@Override
	protected String image() {
		return "canon-60d warrior-dash.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(StandardTag.METERING_MODE, "Evaluative");
				put(StandardTag.SHUTTER_SPEED, "1/4096");
				put(StandardTag.EXPOSURE_TIME, "1/4000");
				put(StandardTag.EXIF_VERSION, "0230");
				put(StandardTag.COLOR_SPACE, "sRGB");
				put(StandardTag.AUTHOR, "Test Author");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.COMMENT, "Test comments");
				put(StandardTag.DATE_TIME_ORIGINAL, "2011:04:30 08:35:00");
				put(StandardTag.FOCAL_LENGTH, "35.0 mm");
				put(StandardTag.ISO, "100");
				put(StandardTag.EXPOSURE_PROGRAM, "Aperture-priority AE");
				put(StandardTag.RATING, "4");
				put(StandardTag.LENS_MODEL, "EF16-35mm f/2.8L II USM");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.FILE_SIZE, "4.7 MB");
				put(StandardTag.CREATOR, "Test Author");
				put(StandardTag.Y_RESOLUTION, "72");
				put(StandardTag.SUB_SEC_TIME_ORIGINAL, "14");
				put(StandardTag.ORIENTATION, "Horizontal (normal)");
				put(StandardTag.LENS_ID, "Canon EF 16-35mm f/2.8L II");
				put(StandardTag.MAKE, "Canon");
				put(StandardTag.MODEL, "Canon EOS 60D");
				put(StandardTag.IMAGE_WIDTH, "5184");
				put(StandardTag.SUBJECT, "Test Subject");
				put(StandardTag.WHITE_BALANCE, "Auto");
				put(StandardTag.ARTIST, "Test Author");
				put(StandardTag.TITLE, "Test Title");
				put(StandardTag.KEYWORDS, "tag1 tag2 tag3");
				put(StandardTag.RATING_PERCENT, "75");
				put(StandardTag.EXPOSURE_COMPENSATION, "-1/3");
				put(StandardTag.IMAGE_HEIGHT, "3456");
				put(StandardTag.CONTRAST, "Normal");
				put(StandardTag.FLASH, "Off, Did not fire");
				put(StandardTag.SHARPNESS, "3");
				put(StandardTag.SATURATION, "Normal");
				put(StandardTag.X_RESOLUTION, "72");
				put(StandardTag.APERTURE, "2.8");
				put(StandardTag.FNUMBER, "2.8");
				put(StandardTag.OWNER_NAME, "");
				put(StandardTag.COPYRIGHT, "");
				put(StandardTag.CREATE_DATE, "2011:04:30 08:35:00");
			}
		};
	}

	@Override
	protected Map<Tag, String> updateTags() {
		return new HashMap<Tag, String>() {{
			put(StandardTag.COMMENT, "Hello =World");
			put(StandardTag.AUTHOR, "mjeanroy");
		}};
	}
}
