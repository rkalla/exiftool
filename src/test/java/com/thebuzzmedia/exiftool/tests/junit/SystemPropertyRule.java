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

package com.thebuzzmedia.exiftool.tests.junit;

import org.junit.rules.ExternalResource;

/**
 * Clear system property before each test, and restore value
 * after each test.
 */
public class SystemPropertyRule extends ExternalResource {

	/**
	 * Property name.
	 */
	private final String prop;

	/**
	 * Property value.
	 * Will be initialized before each test and used after
	 * each test to restore property value.
	 */
	private String value;

	/**
	 * Create rule with property name.
	 *
	 * @param prop Property name.
	 */
	public SystemPropertyRule(String prop) {
		this.prop = prop;
	}

	@Override
	protected void before() throws Throwable {
		value = System.getProperty(prop);
		System.clearProperty(prop);
	}

	@Override
	protected void after() {
		if (value == null) {
			System.clearProperty(prop);
		} else {
			System.setProperty(prop, value);
		}
	}
}
