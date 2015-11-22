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

public class ExifTool_IMG7_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "palm-pre-menu.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.MAKE, "Palm");
				put(Tag.EXPOSURE_PROGRAM, "Program AE");
				put(Tag.X_RESOLUTION, "72");
				put(Tag.Y_RESOLUTION, "72");
				put(Tag.EXPOSURE_TIME, "1/65536000");
				put(Tag.EXIF_VERSION, "0220");
				put(Tag.IMAGE_HEIGHT, "2032");
				put(Tag.ORIENTATION, "Horizontal (normal)");
				put(Tag.GPS_LATITUDE_REF, "North");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.IMAGE_WIDTH, "1520");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.GPS_LATITUDE, "32 deg 48' 4.00\" N");
				put(Tag.FOCAL_LENGTH, "inf mm");
				put(Tag.GPS_LONGITUDE, "117 deg 13' 33.00\" W");
				put(Tag.APERTURE, "1.1");
				put(Tag.MODEL, "Pre");
				put(Tag.GPS_LONGITUDE_REF, "West");
				put(Tag.FLASH, "Auto, Did not fire");
			}
		};
	}
}
