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

public class ExifTool_IMG5_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "nexus-s-electric-cars.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.EXPOSURE_COMPENSATION, "0");
				put(Tag.FOCAL_LENGTH, "3.4 mm");
				put(Tag.IMAGE_HEIGHT, "1920");
				put(Tag.X_RESOLUTION, "72");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.ISO, "50");
				put(Tag.METERING_MODE, "Center-weighted average");
				put(Tag.MODEL, "Nexus S");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.IMAGE_WIDTH, "2560");
				put(Tag.EXPOSURE_TIME, "1/62");
				put(Tag.COLOR_SPACE, "sRGB");
				put(Tag.DATE_TIME_ORIGINAL, "2010:12:10 17:07:05");
				put(Tag.SHUTTER_SPEED, "1/64");
				put(Tag.EXIF_VERSION, "0220");
				put(Tag.ORIENTATION, "Horizontal (normal)");
				put(Tag.FLASH, "No Flash");
				put(Tag.SOFTWARE, "GRH55");
				put(Tag.WHITE_BALANCE, "Auto");
				put(Tag.EXPOSURE_PROGRAM, "Aperture-priority AE");
				put(Tag.MAKE, "google");
				put(Tag.APERTURE, "2.8");
				put(Tag.Y_RESOLUTION, "72");
			}
		};
	}
}
