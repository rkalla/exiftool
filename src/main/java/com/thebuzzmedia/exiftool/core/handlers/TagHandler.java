/**
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

package com.thebuzzmedia.exiftool.core.handlers;

import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.logs.Logger;
import com.thebuzzmedia.exiftool.logs.LoggerFactory;
import com.thebuzzmedia.exiftool.process.OutputHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.thebuzzmedia.exiftool.core.handlers.StopHandler.stopHandler;
import static java.util.Collections.unmodifiableMap;

/**
 * Read tags line by line.
 *
 * This class is not thread-safe and should be used to
 * read exiftool output from one thread (should not be shared across
 * several threads).
 */
public class TagHandler implements OutputHandler {

	/**
	 * Class logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(TagHandler.class);

	/**
	 * Compiled {@link Pattern} of ": " used to split compact output from
	 * ExifTool evenly into name/value pairs.
	 */
	private static final Pattern TAG_VALUE_PATTERN = Pattern.compile(": ");

	/**
	 * Map of tags found.
	 * Each tags will be added one by one during line processing.
	 */
	private final Map<Tag, String> tags;

	/**
	 * Create handler.
	 */
	public TagHandler() {
		tags = new HashMap<Tag, String>();
	}

	/**
	 * Create handler and initialize size of tags.
	 * If size is not enough, it will be resized automatically.
	 *
	 * @param size Expected size of tags to parse.
	 */
	public TagHandler(int size) {
		tags = new HashMap<Tag, String>(size);
	}

	@Override
	public boolean readLine(String line) {
		// If line is null, then this is the end.
		// If line is strictly equals to "{ready}", then it means that stay_open feature
		// is enabled and this is the end of the output.
		if (!stopHandler().readLine(line)) {
			return false;
		}

		// Now, we are sure we can process line.
		String[] pair = TAG_VALUE_PATTERN.split(line);
		if (pair != null && pair.length == 2) {
			// Determine the tag represented by this value.
			Tag tag = Tag.forName(pair[0]);

			// Store the tag and the associated value in the result map only
			// if we were able to map the name back to a Tag instance. If
			// not, then this is an unknown/unexpected tag return value and
			// we skip it since we cannot translate it back to one of our
			// supported tags.
			if (tag != null) {
				tags.put(tag, pair[1]);
				log.debug("Read Tag [name=%s, value=%s]", tag, pair[1]);
			} else {
				log.debug("Unable to read Tag: %s", line);
			}
		}

		return true;
	}

	public Map<Tag, String> getTags() {
		return unmodifiableMap(tags);
	}

	public int size() {
		return tags.size();
	}
}
