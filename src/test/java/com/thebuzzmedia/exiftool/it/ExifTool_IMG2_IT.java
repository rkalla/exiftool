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

@SuppressWarnings("serial")
public class ExifTool_IMG2_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "htc-glacier-cat-ladder.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(StandardTag.GPS_LONGITUDE, "111 deg 7' 42.12\" W");
				put(StandardTag.GPS_LATITUDE, "32 deg 22' 28.21\" N");
				put(StandardTag.FOCAL_LENGTH, "3.5 mm");
				put(StandardTag.COLOR_SPACE, "sRGB");
				put(StandardTag.ISO, "274");
				put(StandardTag.DATE_TIME_ORIGINAL, "2011:08:10 09:50:45");
				put(StandardTag.EXIF_VERSION, "0220");
				put(StandardTag.Y_RESOLUTION, "72");
				put(StandardTag.GPS_LATITUDE_REF, "North");
				put(StandardTag.GPS_ALTITUDE_REF, "Above Sea Level");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.FILE_SIZE, "841 kB");
				put(StandardTag.X_RESOLUTION, "72");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.GPS_ALTITUDE, "613 m Above Sea Level");
				put(StandardTag.MODEL, "myTouch 4G");
				put(StandardTag.GPS_PROCESS_METHOD, "GPS");
				put(StandardTag.IMAGE_HEIGHT, "1456");
				put(StandardTag.GPS_LONGITUDE_REF, "West");
				put(StandardTag.GPS_TIMESTAMP, "16:50:45");
				put(StandardTag.IMAGE_WIDTH, "2592");
				put(StandardTag.MAKE, "HTC");
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
