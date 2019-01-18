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

package com.thebuzzmedia.exiftool;

/**
 * A {@link Tag} is a value that can be easily extracted from images with
 * an external install of ExifTool.
 *
 * <br>
 *
 * A tag that can be easily parsed to a valid value
 * using the {@link #parse(String)} hint from each {@link Tag}.
 * Using this method allow anyone to convert a string value to the correct
 * data format.
 *
 * <br>
 *
 * Implementations should not make an attempt at converting the value automatically
 * in case the caller decides they would prefer tag values returned in
 * {@link com.thebuzzmedia.exiftool.core.StandardFormat#HUMAN_READABLE} format and
 * to avoid any compatibility issues with future versions of ExifTool if a
 * tag's return value is changed.
 * This approach to leaving returned tag values as strings until
 * the caller decides they want to parse them is a safer and more robust
 * approach.
 *
 * <br>
 *
 * The types provided by each tag are merely a hint based on the
 * <a href="http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/index.html">ExifTool Tag Guide</a>
 * by Phil Harvey; the caller is free to parse or process the returned {@link String} values
 * any way they wish.
 *
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * @author Mickael Jeanroy (mickael.jeanroy@gmail.com)
 * @since 1.1
 */
public interface Tag {

	/**
	 * Used to get the name of the tag (e.g. "Orientation", "ISO", etc.).
	 * This is the value actually used to invoke the tool.
	 *
	 * @return Name of the tag.
	 */
	String getName();
	
	/**
	 * Used to get the display name of the tag, which is the actual name printed
	 * by the tool on stout. 
	 * 
	 * For simple tags this is equivalent to value returned by <code>getName</code>.
	 * 
	 * @since 2.4.0
	 * @return Display name of the tag.
	 */
	String getDisplayName();

	/**
	 * Parse given tag to the correct data format.
	 *
	 * @param value Tag Value.
	 * @param <T> Type of returned value.
	 * @return Data associated with the tag.
	 */
	<T> T parse(String value);
}
