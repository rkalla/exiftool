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

public class ExifTool_IMG4_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "iphone-4-cyanide.JPG";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.METERING_MODE, "Multi-segment");
				put(Tag.Y_RESOLUTION, "72");
				put(Tag.SHARPNESS, "Hard");
				put(Tag.SHUTTER_SPEED, "1/15");
				put(Tag.GPS_LONGITUDE, "122 deg 21' 0.60\" W");
				put(Tag.GPS_LATITUDE_REF, "North");
				put(Tag.DATE_TIME_ORIGINAL, "2011:08:17 19:35:54");
				put(Tag.X_RESOLUTION, "72");
				put(Tag.SENSING_METHOD, "One-chip color area");
				put(Tag.MODEL, "iPhone 4");
				put(Tag.GPS_LATITUDE, "47 deg 38' 56.40\" N");
				put(Tag.WHITE_BALANCE, "Auto");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.FLASH, "Off, Did not fire");
				put(Tag.GPS_LONGITUDE_REF, "West");
				put(Tag.GPS_ALTITUDE_REF, "Above Sea Level");
				put(Tag.GPS_ALTITUDE, "19.9 m Above Sea Level");
				put(Tag.EXPOSURE_PROGRAM, "Program AE");
				put(Tag.SOFTWARE, "5.0");
				put(Tag.APERTURE, "2.8");
				put(Tag.MAKE, "Apple");
				put(Tag.EXPOSURE_TIME, "1/15");
				put(Tag.IMAGE_HEIGHT, "1936");
				put(Tag.ORIENTATION, "Rotate 90 CW");
				put(Tag.GPS_TIMESTAMP, "04:01:42");
				put(Tag.ISO, "125");
				put(Tag.FOCAL_LENGTH, "3.9 mm");
				put(Tag.COLOR_SPACE, "sRGB");
				put(Tag.EXIF_VERSION, "0221");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.IMAGE_WIDTH, "2592");
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
