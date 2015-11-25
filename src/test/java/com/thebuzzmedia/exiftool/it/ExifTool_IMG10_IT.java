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

public class ExifTool_IMG10_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "sony-xperia-play-coffee-sign.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.MAKE, "Sony Ericsson");
				put(Tag.COLOR_SPACE, "sRGB");
				put(Tag.Y_RESOLUTION, "72");
				put(Tag.FLASH, "Unknown (0x6d00)");
				put(Tag.ISO, "40");
				put(Tag.ORIENTATION, "Horizontal (normal)");
				put(Tag.X_RESOLUTION, "72");
				put(Tag.SOFTWARE, "12307");
				put(Tag.EXIF_VERSION, "0220");
				put(Tag.IMAGE_HEIGHT, "1944");
				put(Tag.DATE_TIME_ORIGINAL, "2011:08:08 17:09:44");
				put(Tag.EXPOSURE_TIME, "1/250");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.IMAGE_WIDTH, "2592");
				put(Tag.MODEL, "R800x");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.SHUTTER_SPEED, "1");
				put(Tag.FOCAL_LENGTH, "3.6 mm");
			}
		};
	}

	@Override
	protected Map<Tag, String> updateTags() {
		return new HashMap<Tag, String>() {{
			put(Tag.COMMENT, "Hello =World");
			put(Tag.AUTHOR, "mjeanroy");
		}};
	}
}
