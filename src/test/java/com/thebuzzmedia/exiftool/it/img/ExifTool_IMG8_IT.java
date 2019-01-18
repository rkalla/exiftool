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
public class ExifTool_IMG8_IT extends AbstractExifToolImgIT {

	@Override
	protected String image() {
		return "panasonic-dmc-gf3-flower.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(StandardTag.METERING_MODE, "Multi-segment");
				put(StandardTag.Y_RESOLUTION, "180");
				put(StandardTag.SHARPNESS, "Normal");
				put(StandardTag.DIGITAL_ZOOM_RATIO, "0");
				put(StandardTag.ROTATION, "Rotate 270 CW");
				put(StandardTag.DATE_TIME_ORIGINAL, "2011:07:28 02:07:11");
				put(StandardTag.MAKE, "Panasonic");
				put(StandardTag.X_RESOLUTION, "180");
				put(StandardTag.ORIENTATION, "Rotate 270 CW");
				put(StandardTag.SOFTWARE, "Ver.1.0");
				put(StandardTag.MODEL, "DMC-GF3");
				put(StandardTag.SATURATION, "Normal");
				put(StandardTag.FLASH, "Off, Did not fire");
				put(StandardTag.EXIF_VERSION, "0230");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.LENS_ID, "LUMIX G VARIO 14-140mm F4.0-5.8");
				put(StandardTag.EXPOSURE_TIME, "1/320");
				put(StandardTag.COLOR_SPACE, "sRGB");
				put(StandardTag.CONTRAST, "Normal");
				put(StandardTag.SENSING_METHOD, "One-chip color area");
				put(StandardTag.FOCAL_LENGTH_35MM, "150 mm");
				put(StandardTag.FNUMBER, "6.3");
				put(StandardTag.IMAGE_HEIGHT, "3000");
				put(StandardTag.EXPOSURE_COMPENSATION, "0");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.WHITE_BALANCE, "Auto");
				put(StandardTag.FOCAL_LENGTH, "75.0 mm");
				put(StandardTag.EXPOSURE_PROGRAM, "Aperture-priority AE");
				put(StandardTag.ISO, "160");
				put(StandardTag.IMAGE_WIDTH, "4000");
				put(StandardTag.FILE_SIZE, "5.5 MB");
				put(StandardTag.CREATE_DATE, "2011:07:28 02:07:11");
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
