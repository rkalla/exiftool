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
public class ExifTool_IMG9_IT extends AbstractExifToolImgIT {

	@Override
	protected String image() {
		return "samsung-vibrant-volcano.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(StandardTag.FLASH, "No flash function");
				put(StandardTag.X_RESOLUTION, "72");
				put(StandardTag.SHARPNESS, "Normal");
				put(StandardTag.SHUTTER_SPEED, "1/1859");
				put(StandardTag.DATE_TIME_ORIGINAL, "2011:08:18 13:57:00");
				put(StandardTag.MODEL, "SGH-T959");
				put(StandardTag.SOFTWARE, "fw 05.15 prm 07.55");
				put(StandardTag.ISO, "50");
				put(StandardTag.IMAGE_HEIGHT, "1536");
				put(StandardTag.EXPOSURE_PROGRAM, "Program AE");
				put(StandardTag.SENSING_METHOD, "One-chip color area");
				put(StandardTag.MAKE, "SAMSUNG");
				put(StandardTag.SATURATION, "Normal");
				put(StandardTag.GPS_ALTITUDE_REF, "Above Sea Level");
				put(StandardTag.Y_RESOLUTION, "72");
				put(StandardTag.GPS_LATITUDE, "0 deg 0' 0.00\" N");
				put(StandardTag.GPS_LONGITUDE, "0 deg 0' 0.00\" E");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.GPS_LATITUDE_REF, "North");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.FOCAL_LENGTH_35MM, "0 mm");
				put(StandardTag.ORIENTATION, "Horizontal (normal)");
				put(StandardTag.CONTRAST, "Normal");
				put(StandardTag.COLOR_SPACE, "sRGB");
				put(StandardTag.FOCAL_LENGTH, "3.8 mm");
				put(StandardTag.IMAGE_WIDTH, "2048");
				put(StandardTag.EXPOSURE_TIME, "1/1859");
				put(StandardTag.GPS_LONGITUDE_REF, "East");
				put(StandardTag.WHITE_BALANCE, "Auto");
				put(StandardTag.METERING_MODE, "Center-weighted average");
				put(StandardTag.EXPOSURE_COMPENSATION, "0");
				put(StandardTag.GPS_ALTITUDE, "0 m Above Sea Level");
				put(StandardTag.APERTURE, "2.6");
				put(StandardTag.FNUMBER, "2.6");
				put(StandardTag.EXIF_VERSION, "0220");
				put(StandardTag.DIGITAL_ZOOM_RATIO, "2.245614035");
				put(StandardTag.FILE_SIZE, "901 kB");
				put(StandardTag.BRIGHTNESS, "9.77");
				put(StandardTag.CREATE_DATE, "2011:08:18 13:57:00");
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
