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

public class ExifTool_IMG2_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "htc-glacier-cat-ladder.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.GPS_LONGITUDE, "111 deg 7' 42.12\" W");
				put(Tag.GPS_LATITUDE, "32 deg 22' 28.21\" N");
				put(Tag.FOCAL_LENGTH, "3.5 mm");
				put(Tag.COLOR_SPACE, "sRGB");
				put(Tag.ISO, "274");
				put(Tag.DATE_TIME_ORIGINAL, "2011:08:10 09:50:45");
				put(Tag.EXIF_VERSION, "0220");
				put(Tag.Y_RESOLUTION, "72");
				put(Tag.GPS_LATITUDE_REF, "North");
				put(Tag.GPS_ALTITUDE_REF, "Above Sea Level");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.X_RESOLUTION, "72");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.GPS_ALTITUDE, "613 m Above Sea Level");
				put(Tag.MODEL, "myTouch 4G");
				put(Tag.GPS_PROCESS_METHOD, "GPS");
				put(Tag.IMAGE_HEIGHT, "1456");
				put(Tag.GPS_LONGITUDE_REF, "West");
				put(Tag.GPS_TIMESTAMP, "16:50:45");
				put(Tag.IMAGE_WIDTH, "2592");
				put(Tag.MAKE, "HTC");
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
