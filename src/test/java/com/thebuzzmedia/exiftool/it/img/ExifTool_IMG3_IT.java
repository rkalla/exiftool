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
public class ExifTool_IMG3_IT extends AbstractExifToolImgIT {

	@Override
	protected String image() {
		return "iphone-4-bacardi.jpeg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		// Results extracted with exiftool 10.07
		return new HashMap<Tag, String>() {
			{
				put(StandardTag.EXPOSURE_TIME, "1/1117");
				put(StandardTag.METERING_MODE, "Multi-segment");
				put(StandardTag.IMAGE_WIDTH, "2592");
				put(StandardTag.IMAGE_HEIGHT, "1936");
				put(StandardTag.GPS_ALTITUDE, "25.4 m Above Sea Level");
				put(StandardTag.GPS_LONGITUDE_REF, "West");
				put(StandardTag.GPS_LATITUDE_REF, "North");
				put(StandardTag.ISO, "80");
				put(StandardTag.FLASH, "Off, Did not fire");
				put(StandardTag.MAKE, "Apple");
				put(StandardTag.GPS_LATITUDE, "47 deg 39' 0.00\" N");
				put(StandardTag.GPS_LONGITUDE, "122 deg 21' 4.80\" W");
				put(StandardTag.SHUTTER_SPEED, "1/1117");
				put(StandardTag.FOCAL_LENGTH, "3.9 mm");
				put(StandardTag.GPS_ALTITUDE_REF, "Above Sea Level");
				put(StandardTag.EXIF_VERSION, "0221");
				put(StandardTag.GPS_TIMESTAMP, "00:58:48");
				put(StandardTag.COLOR_SPACE, "sRGB");
				put(StandardTag.MODEL, "iPhone 4");
				put(StandardTag.SHARPNESS, "Hard");
				put(StandardTag.DATE_TIME_ORIGINAL, "2011:08:17 17:31:20");
				put(StandardTag.ORIENTATION, "Horizontal (normal)");
				put(StandardTag.Y_RESOLUTION, "72");
				put(StandardTag.SENSING_METHOD, "One-chip color area");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.FILE_SIZE, "1679 kB");
				put(StandardTag.BRIGHTNESS, "9.193455245");
				put(StandardTag.WHITE_BALANCE, "Auto");
				put(StandardTag.EXPOSURE_PROGRAM, "Program AE");
				put(StandardTag.SOFTWARE, "5.0");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.APERTURE, "2.8");
				put(StandardTag.FNUMBER, "2.8");
				put(StandardTag.X_RESOLUTION, "72");
				put(StandardTag.CREATE_DATE, "2011:08:17 17:31:20");
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
