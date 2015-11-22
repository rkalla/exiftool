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

public class ExifTool_IMG6_IT extends AbstractExifToolIT {

	@Override
	protected String image() {
		return "nikon-d90-audi.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(Tag.METERING_MODE, "Multi-segment");
				put(Tag.Y_RESOLUTION, "300");
				put(Tag.SHARPNESS, "Normal");
				put(Tag.DIGITAL_ZOOM_RATIO, "1");
				put(Tag.EXIF_VERSION, "0221");
				put(Tag.DATE_TIME_ORIGINAL, "2010:08:21 19:23:36");
				put(Tag.MAKE, "NIKON CORPORATION");
				put(Tag.X_RESOLUTION, "300");
				put(Tag.ORIENTATION, "Horizontal (normal)");
				put(Tag.SOFTWARE, "Adobe Photoshop CS2 Windows");
				put(Tag.MODEL, "NIKON D90");
				put(Tag.SATURATION, "Normal");
				put(Tag.FLASH, "No Flash");
				put(Tag.SUB_SEC_TIME_ORIGINAL, "00");
				put(Tag.MIME_TYPE, "image/jpeg");
				put(Tag.FILE_TYPE, "JPEG");
				put(Tag.EXPOSURE_TIME, "1/60");
				put(Tag.COLOR_SPACE, "sRGB");
				put(Tag.CONTRAST, "Normal");
				put(Tag.SENSING_METHOD, "One-chip color area");
				put(Tag.FOCAL_LENGTH_35MM, "75 mm");
				put(Tag.IMAGE_HEIGHT, "2768");
				put(Tag.EXPOSURE_COMPENSATION, "0");
				put(Tag.WHITE_BALANCE, "Auto");
				put(Tag.FOCAL_LENGTH, "50.0 mm");
				put(Tag.EXPOSURE_PROGRAM, "Aperture-priority AE");
				put(Tag.ISO, "400");
				put(Tag.IMAGE_WIDTH, "3604");
			}
		};
	}
}
