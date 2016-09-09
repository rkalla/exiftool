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

package com.thebuzzmedia.exiftool.tests.junit;

import org.junit.rules.ExternalResource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.addAll;

/**
 * Clear system property before each test, and restore value
 * after each test.
 */
public class SystemPropertyRule extends ExternalResource {

	/**
	 * Property names.
	 */
	private final Set<String> props;

	/**
	 * Property values.
	 * Will be initialized before each test and used after
	 * each test to restore property value.
	 */
	private Map<String, String> values;

	/**
	 * Create rule with property name.
	 *
	 * @param prop Property name.
	 */
	public SystemPropertyRule(String prop, String... other) {
		props = new HashSet<>();
		props.add(prop);
		addAll(props, other);
	}

	@Override
	protected void before() throws Throwable {
		values = new HashMap<>();
		for (String prop : props) {
			values.put(prop, System.getProperty(prop));
			System.clearProperty(prop);
		}
	}

	@Override
	protected void after() {
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String prop = entry.getKey();
			String value = entry.getValue();
			if (value == null) {
				System.clearProperty(prop);
			} else {
				System.setProperty(prop, value);
			}
		}
	}
}
