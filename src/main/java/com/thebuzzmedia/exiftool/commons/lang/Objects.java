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

package com.thebuzzmedia.exiftool.commons.lang;

/**
 * Static Objects Utilities.
 */
public final class Objects {

	// Ensure non instantiation.
	private Objects() {
	}

	/**
	 * Returns the first of two given parameters that is not null.
	 *
	 * @param val1 First value.
	 * @param val2 Second value.
	 * @param others Other value.
	 * @param <T> Type of parameters.
	 * @return First parameter if it is not null, second parameter otherwise.
	 */
	public static <T> T firstNonNull(T val1, T val2, T... others) {
		if (val1 != null) {
			return val1;
		}

		if (val2 != null) {
			return val2;
		}

		for (T current : others) {
			if (current != null) {
				return current;
			}
		}

		return null;
	}

	/**
	 * Compute {@code hashCode} value from all parameters.
	 *
	 * @param values Parameters.
	 * @return Hash Code value.
	 */
	public static int hashCode(Object... values) {
		if (values == null) {
			return 0;
		}

		int result = 1;
		for (Object element : values) {
			result = 31 * result + (element == null ? 0 : element.hashCode());
		}

		return result;
	}

	/**
	 * Check that two values are equals:
	 * - If both are the same instances, return true.
	 * - If both are null, return false.
	 * - If one is null (and not the other one), return false.
	 * - Finally, if both are non null, return the result of {code o1.equals(o2)}.
	 *
	 * @param o1 First parameter to check.
	 * @param o2 Second parameter to check.
	 * @return Result of equality.
	 */
	public static boolean equals(Object o1, Object o2) {
		return o1 == o2 || (o1 != null && o2 != null && o1.equals(o2));
	}
}
