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

package com.thebuzzmedia.exiftool.commons.iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Static Collection Utilities.
 */
public final class Collections {

	// Ensure non instantiation.
	private Collections() {
	}

	/**
	 * Check if collection is empty (null or empty).
	 *
	 * @param values Collection of values.
	 * @param <T> Type of element in collection.
	 * @return True if collection is empty (null or empty), false otherwise.
	 */
	public static <T> boolean isEmpty(Collection<T> values) {
		return values == null || values.isEmpty();
	}

	/**
	 * Get size of collection.
	 * If collection is empty (null or empty), zero will be returned.
	 *
	 * @param values Collection of values.
	 * @param <T> Type of element in collection.
	 * @return Size of collection.
	 */
	public static <T> int size(Collection<T> values) {
		return isEmpty(values) ? 0 : values.size();
	}

	/**
	 * Join elements of collection to a final string.
	 * If collection is empty (null or empty), an empty string is returned.
	 *
	 * @param values Collection of values.
	 * @param separator Separator, use to separate each elements.
	 * @param <T> Type of element in collection.
	 * @return Final string.
	 */
	public static <T> String join(Collection<T> values, String separator) {
		if (isEmpty(values)) {
			return "";
		}

		Iterator<T> it = values.iterator();
		StringBuilder sb = new StringBuilder();
		sb.append(it.next());
		while (it.hasNext()) {
			sb.append(separator).append(it.next());
		}

		return sb.toString();
	}

	/**
	 * Map list of inputs to a new list of outputs.
	 *
	 * @param inputs Input list.
	 * @param mapper Mapper used to transform inputs.
	 * @param <T> Type of input.
	 * @param <U> Type of output.
	 * @return New list of outputs.
	 */
	public static <T, U> List<U> map(List<T> inputs, Mapper<T, U> mapper) {
		List<U> outputs = new ArrayList<U>(inputs.size());
		for (T input : inputs) {
			U output = mapper.map(input);
			outputs.add(output);
		}

		return outputs;
	}

	/**
	 * Index list of inputs by result of mapper function.
	 *
	 * @param inputs List of inputs.
	 * @param indexer Mapper function.
	 * @param <T> Type of input.
	 * @param <U> Type of index keys.
	 * @return Map of key (result of mapper function) with values (inputs).
	 */
	public static <T, U> Map<U, T> indexBy(Iterable<T> inputs, Mapper<T, U> indexer) {
		Map<U, T> index = new HashMap<U, T>();
		for (T input : inputs) {
			U output = indexer.map(input);
			index.put(output, input);
		}

		return index;
	}
}
