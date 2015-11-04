/**
 * Copyright 2011 The Buzz Media, LLC
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

package com.thebuzzmedia.exiftool;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum used to pre-define a convenient list of tags that can be easily
 * extracted from images using this class with an external install of
 * ExifTool.
 * <p/>
 * Each tag defined also includes a type hint for the parsed value
 * associated with it when the default {@link com.thebuzzmedia.exiftool.Format#NUMERIC} value format
 * is used.
 * <p/>
 * All replies from ExifTool are parsed as {@link String}s and using the
 * type hint from each {@link Tag} can easily be converted to the correct
 * data format by using the provided {@link Tag#parseValue(Tag, String)}
 * method.
 * <p/>
 * This class does not make an attempt at converting the value automatically
 * in case the caller decides they would prefer tag values returned in
 * {@link com.thebuzzmedia.exiftool.Format#HUMAN_READABLE} format and to avoid any compatibility
 * issues with future versions of ExifTool if a tag's return value is
 * changed. This approach to leaving returned tag values as strings until
 * the caller decides they want to parse them is a safer and more robust
 * approach.
 * <p/>
 * The types provided by each tag are merely a hint based on the <a
 * href="http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html"
 * >ExifTool Tag Guide</a> by Phil Harvey; the caller is free to parse or
 * process the returned {@link String} values any way they wish.
 * <h3>Tag Support</h3>
 * ExifTool is capable of parsing almost every tag known to man (1000+), but
 * this class makes an attempt at pre-defining a convenient list of the most
 * common tags for use.
 * <p/>
 * This list was determined by looking at the common metadata tag values
 * written to images by popular mobile devices (iPhone, Android) as well as
 * cameras like simple point and shoots as well as DSLRs. As an additional
 * source of input the list of supported/common EXIF formats that Flickr
 * supports was also reviewed to ensure the most common/useful tags were
 * being covered here.
 * <p/>
 * Please email me or <a
 * href="https://github.com/thebuzzmedia/imgscalr/issues">file an issue</a>
 * if you think this list is missing a commonly used tag that should be
 * added to it.
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @since 1.1
 */
public enum Tag {
	ISO("ISO", Integer.class), APERTURE("ApertureValue", Double.class), WHITE_BALANCE(
			"WhiteBalance", Integer.class), CONTRAST("Contrast",
			Integer.class), SATURATION("Saturation", Integer.class), SHARPNESS(
			"Sharpness", Integer.class), SHUTTER_SPEED("ShutterSpeedValue",
			Double.class), DIGITAL_ZOOM_RATIO("DigitalZoomRatio",
			Double.class), IMAGE_WIDTH("ImageWidth", Integer.class), IMAGE_HEIGHT(
			"ImageHeight", Integer.class), X_RESOLUTION("XResolution",
			Double.class), Y_RESOLUTION("YResolution", Double.class), FLASH(
			"Flash", Integer.class), METERING_MODE("MeteringMode",
			Integer.class), FOCAL_LENGTH("FocalLength", Double.class), FOCAL_LENGTH_35MM(
			"FocalLengthIn35mmFormat", Integer.class), EXPOSURE_TIME(
			"ExposureTime", Double.class), EXPOSURE_COMPENSATION(
			"ExposureCompensation", Double.class), EXPOSURE_PROGRAM(
			"ExposureProgram", Integer.class), ORIENTATION("Orientation",
			Integer.class), COLOR_SPACE("ColorSpace", Integer.class), SENSING_METHOD(
			"SensingMethod", Integer.class), SOFTWARE("Software",
			String.class), MAKE("Make", String.class), MODEL("Model",
			String.class), LENS_MAKE("LensMake", String.class), LENS_MODEL(
			"LensModel", String.class), OWNER_NAME("OwnerName",
			String.class), TITLE("XPTitle", String.class), AUTHOR(
			"XPAuthor", String.class), SUBJECT("XPSubject", String.class), KEYWORDS(
			"XPKeywords", String.class), COMMENT("XPComment", String.class), RATING(
			"Rating", Integer.class), RATING_PERCENT("RatingPercent",
			Integer.class), DATE_TIME_ORIGINAL("DateTimeOriginal",
			String.class), CREATION_DATE("CreationDate", String.class), GPS_LATITUDE(
			"GPSLatitude", Double.class), GPS_LATITUDE_REF(
			"GPSLatitudeRef", String.class), GPS_LONGITUDE("GPSLongitude",
			Double.class), GPS_LONGITUDE_REF("GPSLongitudeRef",
			String.class), GPS_ALTITUDE("GPSAltitude", Double.class), GPS_ALTITUDE_REF(
			"GPSAltitudeRef", Integer.class), GPS_SPEED("GPSSpeed",
			Double.class), GPS_SPEED_REF("GPSSpeedRef", String.class), GPS_PROCESS_METHOD(
			"GPSProcessingMethod", String.class), GPS_BEARING(
			"GPSDestBearing", Double.class), GPS_BEARING_REF(
			"GPSDestBearingRef", String.class), GPS_TIMESTAMP(
			"GPSTimeStamp", String.class), ROTATION("Rotation",Integer.class),
			EXIF_VERSION("ExifVersion",String.class), LENS_ID("LensID",String.class),
			COPYRIGHT("Copyright", String.class), ARTIST("Artist", String.class),
SUB_SEC_TIME_ORIGINAL("SubSecTimeOriginal", Integer.class),
OBJECT_NAME("ObjectName", String.class), CAPTION_ABSTRACT("Caption-Abstract",
String.class), CREATOR("Creator", String.class), IPTC_KEYWORDS("Keywords",
String.class), COPYRIGHT_NOTICE("CopyrightNotice", String.class),
FILE_TYPE("FileType", String.class), AVG_BITRATE("AvgBitrate", String.class),
MIME_TYPE("MIMEType", String.class);

	private static final Map<String, Tag> TAG_LOOKUP_MAP;

	/**
	 * Initializer used to init the <code>static final</code> tag/name
	 * lookup map used by all instances of this class.
	 */
	static {
		Tag[] values = Tag.values();
		TAG_LOOKUP_MAP = new HashMap<String, Tag>(
				values.length * 3);

		for (int i = 0; i < values.length; i++) {
			Tag tag = values[i];
			TAG_LOOKUP_MAP.put(tag.name, tag);
		}
	}

	/**
	 * Used to get the {@link Tag} identified by the given, case-sensitive,
	 * tag name.
	 *
	 * @param name
	 *            The case-sensitive name of the tag that will be searched
	 *            for.
	 *
	 * @return the {@link Tag} identified by the given, case-sensitive, tag
	 *         name or <code>null</code> if one couldn't be found.
	 */
	public static Tag forName(String name) {
		return TAG_LOOKUP_MAP.get(name);
	}

	/**
	 * Convenience method used to convert the given string Tag value
	 * (returned from the external ExifTool process) into the type described
	 * by the associated {@link Tag}.
	 *
	 * @param <T>
	 *            The type of the returned value.
	 * @param tag
	 *            The {@link Tag} whose value this is. The tag's type hint
	 *            will be queried to determine how to convert this string
	 *            value.
	 * @param value
	 *            The {@link String} representation of the tag's value as
	 *            parsed from the image.
	 *
	 * @return the given string value converted to a native Java type (e.g.
	 *         Integer, Double, etc.).
	 *
	 * @throws IllegalArgumentException
	 *             if <code>tag</code> is <code>null</code>.
	 * @throws NumberFormatException
	 *             if any exception occurs while trying to parse the given
	 *             <code>value</code> to any of the supported numeric types
	 *             in Java via calls to the respective <code>parseXXX</code>
	 *             methods defined on all the numeric wrapper classes (e.g.
	 *             {@link Integer#parseInt(String)} ,
	 *             {@link Double#parseDouble(String)} and so on).
	 * @throws ClassCastException
	 *             if the type defined by <code>T</code> is incompatible
	 *             with the type defined by {@link Tag#getType()} returned
	 *             by the <code>tag</code> argument passed in. This class
	 *             performs an implicit/unchecked cast to the type
	 *             <code>T</code> before returning the parsed result of the
	 *             type indicated by {@link Tag#getType()}. If the types do
	 *             not match, a <code>ClassCastException</code> will be
	 *             generated by the VM.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseValue(Tag tag, String value)
			throws IllegalArgumentException, NumberFormatException {
		if (tag == null)
			throw new IllegalArgumentException("tag cannot be null");

		T result = null;

		// Check that there is work to do first.
		if (value != null) {
			Class<?> type = tag.type;

			if (Boolean.class.isAssignableFrom(type))
				result = (T) Boolean.valueOf(value);
			else if (Byte.class.isAssignableFrom(type))
				result = (T) Byte.valueOf(Byte.parseByte(value));
			else if (Integer.class.isAssignableFrom(type))
				result = (T) Integer.valueOf(Integer.parseInt(value));
			else if (Short.class.isAssignableFrom(type))
				result = (T) Short.valueOf(Short.parseShort(value));
			else if (Long.class.isAssignableFrom(type))
				result = (T) Long.valueOf(Long.parseLong(value));
			else if (Float.class.isAssignableFrom(type))
				result = (T) Float.valueOf(Float.parseFloat(value));
			else if (Double.class.isAssignableFrom(type))
				result = (T) Double.valueOf(Double.parseDouble(value));
			else if (Character.class.isAssignableFrom(type))
				result = (T) Character.valueOf(value.charAt(0));
			else if (String.class.isAssignableFrom(type))
				result = (T) value;
		}

		return result;
	}

	/**
	 * Used to get the name of the tag (e.g. "Orientation", "ISO", etc.).
	 *
	 * @return the name of the tag (e.g. "Orientation", "ISO", etc.).
	 */
	public String getName() {
		return name;
	}

	/**
	 * Used to get a hint for the native type of this tag's value as
	 * specified by Phil Harvey's <a href=
	 * "http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html"
	 * >ExifTool Tag Guide</a>.
	 *
	 * @return a hint for the native type of this tag's value.
	 */
	public Class<?> getType() {
		return type;
	}

	private String name;
	private Class<?> type;

	private Tag(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}
}
