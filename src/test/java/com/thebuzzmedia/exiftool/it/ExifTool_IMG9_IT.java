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

public class ExifTool_IMG9_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "samsung-vibrant-volcano.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.FLASH, "No flash function");
				put(Tag.X_RESOLUTION, "72");
				put(Tag.SHARPNESS, "Normal");
				put(Tag.SHUTTER_SPEED, "1/1859");
				put(Tag.DATE_TIME_ORIGINAL, "2011:08:18 13:57:00");
				put(Tag.MODEL, "SGH-T959");
				put(Tag.SOFTWARE, "fw 05.15 prm 07.55");
				put(Tag.ISO, "50");
				put(Tag.IMAGE_HEIGHT, "1536");
				put(Tag.EXPOSURE_PROGRAM, "Program AE");
				put(Tag.SENSING_METHOD, "One-chip color area");
				put(Tag.MAKE, "SAMSUNG");
				put(Tag.SATURATION, "Normal");
				put(Tag.GPS_ALTITUDE_REF, "Above Sea Level");
				put(Tag.Y_RESOLUTION, "72");
				put(Tag.GPS_LATITUDE, "0 deg 0' 0.00\" N");
				put(Tag.GPS_LONGITUDE, "0 deg 0' 0.00\" E");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.GPS_LATITUDE_REF, "North");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.FOCAL_LENGTH_35MM, "0 mm");
				put(Tag.ORIENTATION, "Horizontal (normal)");
				put(Tag.CONTRAST, "Normal");
				put(Tag.COLOR_SPACE, "sRGB");
				put(Tag.FOCAL_LENGTH, "3.8 mm");
				put(Tag.IMAGE_WIDTH, "2048");
				put(Tag.EXPOSURE_TIME, "1/1859");
				put(Tag.GPS_LONGITUDE_REF, "East");
				put(Tag.WHITE_BALANCE, "Auto");
				put(Tag.METERING_MODE, "Center-weighted average");
				put(Tag.EXPOSURE_COMPENSATION, "0");
				put(Tag.GPS_ALTITUDE, "0 m Above Sea Level");
				put(Tag.APERTURE, "2.6");
				put(Tag.EXIF_VERSION, "0220");
				put(Tag.DIGITAL_ZOOM_RATIO, "2.245614035");
			}
		};
	}
}
