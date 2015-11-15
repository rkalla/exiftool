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

package com.thebuzzmedia.exiftool.exceptions;

import com.thebuzzmedia.exiftool.Feature;

import static java.lang.String.format;

/**
 * Class used to define an exception that occurs when the caller attempts to
 * use a {@link com.thebuzzmedia.exiftool.Feature} that the underlying native ExifTool install does
 * not support (i.e. the version isn't new enough).
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @since 1.1
 */
public class UnsupportedFeatureException extends AbstractExifException {

	/**
	 * Unsupported Feature.
	 */
	private final Feature feature;

	/**
	 * Exif Version (this version do not support feature).
	 */
	private final String exifVersion;

	public UnsupportedFeatureException(String exifVersion, Feature feature) {
		super(message(feature));
		this.exifVersion = exifVersion;
		this.feature = feature;
	}

	public String getExifVersion() {
		return exifVersion;
	}

	public Feature getFeature() {
		return feature;
	}

	private static String message(Feature feature) {
		String msg = "" +
			"Use of feature [%s] requires version %s or higher of the native ExifTool program. " +
			"The version of ExifTool referenced by the system property 'exiftool.path' is not high enough. " +
			"You can either upgrade the install of ExifTool or avoid using this feature to workaround this exception.";

		return format(msg, feature, feature.getVersion());
	}
}
