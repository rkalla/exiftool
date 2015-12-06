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

package com.thebuzzmedia.exiftool.tests;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * Static Utilities for map.
 */
public final class MapUtils {

	private MapUtils() {
	}

	/**
	 * Create map with two entries.
	 *
	 * @param k1 First key.
	 * @param v1 First value.
	 * @param k2 Second key.
	 * @param v2 Second value.
	 * @param <T> Type of keys.
	 * @param <U> Type of values.
	 * @return New created map.
	 */
	public static <T, U> Map<T, U> newMap(T k1, U v1, T k2, U v2) {
		Map<T, U> map = new LinkedHashMap<T, U>();
		map.put(k1, v1);
		map.put(k2, v2);
		return unmodifiableMap(map);
	}
}
