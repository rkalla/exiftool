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
import com.thebuzzmedia.exiftool.core.StandardTag;

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
				put(StandardTag.MAKE, "Sony Ericsson");
				put(StandardTag.COLOR_SPACE, "sRGB");
				put(StandardTag.Y_RESOLUTION, "72");
				put(StandardTag.FLASH, "Unknown (0x6d00)");
				put(StandardTag.ISO, "40");
				put(StandardTag.ORIENTATION, "Horizontal (normal)");
				put(StandardTag.X_RESOLUTION, "72");
				put(StandardTag.SOFTWARE, "12307");
				put(StandardTag.EXIF_VERSION, "0220");
				put(StandardTag.IMAGE_HEIGHT, "1944");
				put(StandardTag.DATE_TIME_ORIGINAL, "2011:08:08 17:09:44");
				put(StandardTag.EXPOSURE_TIME, "1/250");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.IMAGE_WIDTH, "2592");
				put(StandardTag.MODEL, "R800x");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.SHUTTER_SPEED, "1");
				put(StandardTag.FOCAL_LENGTH, "3.6 mm");
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
