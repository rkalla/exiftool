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
public class ExifTool_IMG5_IT extends AbstractExifToolImgIT {

	@Override
	protected String image() {
		return "nexus-s-electric-cars.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(StandardTag.EXPOSURE_COMPENSATION, "0");
				put(StandardTag.FOCAL_LENGTH, "3.4 mm");
				put(StandardTag.IMAGE_HEIGHT, "1920");
				put(StandardTag.X_RESOLUTION, "72");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.ISO, "50");
				put(StandardTag.METERING_MODE, "Center-weighted average");
				put(StandardTag.MODEL, "Nexus S");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.IMAGE_WIDTH, "2560");
				put(StandardTag.EXPOSURE_TIME, "1/62");
				put(StandardTag.COLOR_SPACE, "sRGB");
				put(StandardTag.DATE_TIME_ORIGINAL, "2010:12:10 17:07:05");
				put(StandardTag.SHUTTER_SPEED, "1/64");
				put(StandardTag.EXIF_VERSION, "0220");
				put(StandardTag.ORIENTATION, "Horizontal (normal)");
				put(StandardTag.FLASH, "No Flash");
				put(StandardTag.SOFTWARE, "GRH55");
				put(StandardTag.WHITE_BALANCE, "Auto");
				put(StandardTag.EXPOSURE_PROGRAM, "Aperture-priority AE");
				put(StandardTag.MAKE, "google");
				put(StandardTag.APERTURE, "2.8");
				put(StandardTag.FNUMBER, "2.6");

				put(StandardTag.Y_RESOLUTION, "72");
				put(StandardTag.FILE_SIZE, "1475 kB");
				put(StandardTag.BRIGHTNESS, "5");
				put(StandardTag.CREATE_DATE, "2010:12:10 17:07:05");
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
