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
public class ExifTool_IMG6_IT extends AbstractExifToolImgIT {

	@Override
	protected String image() {
		return "nikon-d90-audi.jpg";
	}

	@Override
	protected Map<Tag, String> expectations() {
		return new HashMap<Tag, String>() {
			{
				put(StandardTag.METERING_MODE, "Multi-segment");
				put(StandardTag.Y_RESOLUTION, "300");
				put(StandardTag.SHARPNESS, "Normal");
				put(StandardTag.DIGITAL_ZOOM_RATIO, "1");
				put(StandardTag.EXIF_VERSION, "0221");
				put(StandardTag.DATE_TIME_ORIGINAL, "2010:08:21 19:23:36");
				put(StandardTag.MAKE, "NIKON CORPORATION");
				put(StandardTag.X_RESOLUTION, "300");
				put(StandardTag.ORIENTATION, "Horizontal (normal)");
				put(StandardTag.SOFTWARE, "Adobe Photoshop CS2 Windows");
				put(StandardTag.MODEL, "NIKON D90");
				put(StandardTag.SATURATION, "Normal");
				put(StandardTag.FLASH, "No Flash");
				put(StandardTag.SUB_SEC_TIME_ORIGINAL, "00");
				put(StandardTag.MIME_TYPE, "image/jpeg");
				put(StandardTag.FILE_TYPE, "JPEG");
				put(StandardTag.EXPOSURE_TIME, "1/60");
				put(StandardTag.COLOR_SPACE, "sRGB");
				put(StandardTag.CONTRAST, "Normal");
				put(StandardTag.SENSING_METHOD, "One-chip color area");
				put(StandardTag.FOCAL_LENGTH_35MM, "75 mm");
				put(StandardTag.IMAGE_HEIGHT, "2768");
				put(StandardTag.EXPOSURE_COMPENSATION, "0");
				put(StandardTag.WHITE_BALANCE, "Auto");
				put(StandardTag.FOCAL_LENGTH, "50.0 mm");
				put(StandardTag.FNUMBER, "8.0");
				put(StandardTag.EXPOSURE_PROGRAM, "Aperture-priority AE");
				put(StandardTag.ISO, "400");
				put(StandardTag.IMAGE_WIDTH, "3604");
				put(StandardTag.FILE_SIZE, "4.5 MB");
				put(StandardTag.CREATE_DATE, "2010:08:21 19:23:36");
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
