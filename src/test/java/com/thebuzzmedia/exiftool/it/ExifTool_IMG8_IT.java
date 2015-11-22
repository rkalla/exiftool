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

public class ExifTool_IMG8_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "panasonic-dmc-gf3-flower.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.METERING_MODE, "Multi-segment");
				put(Tag.Y_RESOLUTION, "180");
				put(Tag.SHARPNESS, "Normal");
				put(Tag.DIGITAL_ZOOM_RATIO, "0");
				put(Tag.ROTATION, "Rotate 270 CW");
				put(Tag.DATE_TIME_ORIGINAL, "2011:07:28 02:07:11");
				put(Tag.MAKE, "Panasonic");
				put(Tag.X_RESOLUTION, "180");
				put(Tag.ORIENTATION, "Rotate 270 CW");
				put(Tag.SOFTWARE, "Ver.1.0");
				put(Tag.MODEL, "DMC-GF3");
				put(Tag.SATURATION, "Normal");
				put(Tag.FLASH, "Off, Did not fire");
				put(Tag.EXIF_VERSION, "0230");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.LENS_ID, "LUMIX G VARIO 14-140mm F4.0-5.8");
				put(Tag.EXPOSURE_TIME, "1/320");
				put(Tag.COLOR_SPACE, "sRGB");
				put(Tag.CONTRAST, "Normal");
				put(Tag.SENSING_METHOD, "One-chip color area");
				put(Tag.FOCAL_LENGTH_35MM, "150 mm");
				put(Tag.IMAGE_HEIGHT, "3000");
				put(Tag.EXPOSURE_COMPENSATION, "0");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.WHITE_BALANCE, "Auto");
				put(Tag.FOCAL_LENGTH, "75.0 mm");
				put(Tag.EXPOSURE_PROGRAM, "Aperture-priority AE");
				put(Tag.ISO, "160");
				put(Tag.IMAGE_WIDTH, "4000");
			}
		};
	}
}
