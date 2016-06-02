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

package com.thebuzzmedia.exiftool.core;

import com.thebuzzmedia.exiftool.Tag;

/**
 * Enum used to pre-define a convenient list of tags that can be easily
 * extracted from images using this class with an external install of
 * ExifTool.
 *
 * Each tag defined also includes a type hint for the parsed value
 * associated with it when the default {@link com.thebuzzmedia.exiftool.core.StandardFormat#NUMERIC}
 * value format is used.
 *
 * <br>
 *
 * The types provided by each tag are merely a hint based on the
 * <a href="http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html">ExifTool Tag Guide</a>
 * by Phil Harvey; the caller is free to parse or process the returned {@link String} values any way they wish.
 *
 * This list was determined by looking at the common metadata tag values
 * written to images by popular mobile devices (iPhone, Android) as well as
 * cameras like simple point and shoots as well as DSLRs. As an additional
 * source of input the list of supported/common EXIF formats that Flickr
 * supports was also reviewed to ensure the most common/useful tags were
 * being covered here.
 *
 * <br>
 *
 * Please email me or file an issue if you think this list is missing a commonly
 * used tag that should be added to it.
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @author Mickael Jeanroy (mickael.jeanroy@gmail.com)
 * @since 1.1
 */
public enum StandardTag implements Tag {

	ISO("ISO", Type.INTEGER),
	APERTURE("ApertureValue", Type.DOUBLE),
	WHITE_BALANCE("WhiteBalance", Type.INTEGER),
	CONTRAST("Contrast", Type.INTEGER),
	SATURATION("Saturation", Type.INTEGER),
	SHARPNESS("Sharpness", Type.INTEGER),
	SHUTTER_SPEED("ShutterSpeedValue", Type.DOUBLE),
	DIGITAL_ZOOM_RATIO("DigitalZoomRatio", Type.DOUBLE),
	IMAGE_WIDTH("ImageWidth", Type.INTEGER),
	IMAGE_HEIGHT("ImageHeight", Type.INTEGER),
	X_RESOLUTION("XResolution", Type.DOUBLE),
	Y_RESOLUTION("YResolution", Type.DOUBLE),
	FLASH("Flash", Type.INTEGER),
	METERING_MODE("MeteringMode", Type.INTEGER),
	FOCAL_LENGTH("FocalLength", Type.DOUBLE),
	FOCAL_LENGTH_35MM("FocalLengthIn35mmFormat", Type.INTEGER),
	EXPOSURE_TIME("ExposureTime", Type.DOUBLE),
	EXPOSURE_COMPENSATION("ExposureCompensation", Type.DOUBLE),
	EXPOSURE_PROGRAM("ExposureProgram", Type.INTEGER),
	ORIENTATION("Orientation", Type.INTEGER),
	COLOR_SPACE("ColorSpace", Type.INTEGER),
	SENSING_METHOD("SensingMethod", Type.INTEGER),
	SOFTWARE("Software", Type.STRING),
	MAKE("Make", Type.STRING),
	MODEL("Model", Type.STRING),
	LENS_MAKE("LensMake", Type.STRING),
	LENS_MODEL("LensModel", Type.STRING),
	OWNER_NAME("OwnerName", Type.STRING),
	TITLE("XPTitle", Type.STRING),
	AUTHOR("XPAuthor", Type.STRING),
	SUBJECT("XPSubject", Type.STRING),
	KEYWORDS("XPKeywords", Type.STRING),
	COMMENT("XPComment", Type.STRING),
	RATING("Rating", Type.INTEGER),
	RATING_PERCENT("RatingPercent", Type.INTEGER),
	DATE_TIME_ORIGINAL("DateTimeOriginal", Type.STRING),
	CREATION_DATE("CreationDate", Type.STRING),
	GPS_LATITUDE("GPSLatitude", Type.DOUBLE),
	GPS_LATITUDE_REF("GPSLatitudeRef", Type.STRING),
	GPS_LONGITUDE("GPSLongitude", Type.DOUBLE),
	GPS_LONGITUDE_REF("GPSLongitudeRef", Type.STRING),
	GPS_ALTITUDE("GPSAltitude", Type.DOUBLE),
	GPS_ALTITUDE_REF("GPSAltitudeRef", Type.INTEGER),
	GPS_SPEED("GPSSpeed", Type.DOUBLE),
	GPS_SPEED_REF("GPSSpeedRef", Type.STRING),
	GPS_PROCESS_METHOD("GPSProcessingMethod", Type.STRING),
	GPS_BEARING("GPSDestBearing", Type.DOUBLE),
	GPS_BEARING_REF("GPSDestBearingRef", Type.STRING),
	GPS_TIMESTAMP("GPSTimeStamp", Type.STRING),
	ROTATION("Rotation", Type.INTEGER),
	EXIF_VERSION("ExifVersion", Type.STRING),
	LENS_ID("LensID", Type.STRING),
	COPYRIGHT("Copyright", Type.STRING),
	ARTIST("Artist", Type.STRING),
	SUB_SEC_TIME_ORIGINAL("SubSecTimeOriginal", Type.INTEGER),
	OBJECT_NAME("ObjectName", Type.STRING),
	CAPTION_ABSTRACT("Caption-Abstract", Type.STRING),
	CREATOR("Creator", Type.STRING),
	IPTC_KEYWORDS("Keywords", Type.ARRAY),
	COPYRIGHT_NOTICE("CopyrightNotice", Type.STRING),
	FILE_TYPE("FileType", Type.STRING),
	AVG_BITRATE("AvgBitrate", Type.STRING),
	MIME_TYPE("MIMEType", Type.STRING);

	/**
	 * Used to get the name of the tag (e.g. "Orientation", "ISO", etc.).
	 */
	private final String name;

	/**
	 * Used to get a hint for the native type of this tag's value as
	 * specified by Phil Harvey's <a href="http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html">ExifTool Tag Guide</a>.
	 */
	private final Type type;

	private StandardTag(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public <T> T parse(String value) {
		return type.parse(value);
	}

	@SuppressWarnings("unchecked")
	private static enum Type {
		INTEGER {
			@Override
			public <T> T parse(String value) {
				return (T) Integer.valueOf(Integer.parseInt(value));
			}
		},
		DOUBLE {
			@Override
			public <T> T parse(String value) {
				return (T) Double.valueOf(Double.parseDouble(value));
			}
		},
		STRING {
			@Override
			public <T> T parse(String value) {
				return (T) value;
			}
		},
        ARRAY {
            @Override
            public <T> T parse(String value) { return (T) value.split("\\|>☃"); }
        };

		public abstract <T> T parse(String value);
	}
}
