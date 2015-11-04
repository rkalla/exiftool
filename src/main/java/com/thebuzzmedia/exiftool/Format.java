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

/**
 * Enum used to define the 2 different output formats that {@link Tag}
 * values can be returned in: numeric or human-readable text.
 * <p/>
 * ExifTool, via the <code>-n</code> command line arg, is capable of
 * returning most values in their raw numeric form (e.g.
 * Aperture="2.8010323841") as well as a more human-readable/friendly format
 * (e.g. Aperture="2.8").
 * <p/>
 * While the {@link Tag}s defined on this class do provide a hint at the
 * type of the result (see {@link Tag#getType()}), that hint only applies
 * when the {@link Format#NUMERIC} form of the value is returned.
 * <p/>
 * If the caller finds the human-readable format easier to process,
 * {@link Format#HUMAN_READABLE} can be specified when calling
 * {@link com.thebuzzmedia.exiftool.ExifTool#getImageMeta(java.io.File, Format, Tag...)} and the returned
 * {@link String} values processed manually by the caller.
 * <p/>
 * In order to see the types of values that are returned when
 * {@link Format#HUMAN_READABLE} is used, you can check the comprehensive <a
 * href="http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html">
 * ExifTool Tag Guide</a>.
 * <p/>
 * This makes sense with some values like Aperture that in
 * {@link Format#NUMERIC} format end up returning as 14-decimal-place, high
 * precision values that are near the intended value (e.g.
 * "2.79999992203711" instead of just returning "2.8"). On the other hand,
 * other values (like Orientation) are easier to parse when their numeric
 * value (1-8) is returned instead of a much longer friendly name (e.g.
 * "Mirror horizontal and rotate 270 CW").
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @since 1.1
 */
public enum Format {
	NUMERIC, HUMAN_READABLE;
}
