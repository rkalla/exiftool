/**
 * Copyright 2011 The Buzz Media, LLC
 * Copyright 2015 Mickael Jeanroy <mickael.jeanroy@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thebuzzmedia.exiftool.tests;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Reflection Utilities used in unit tests.
 */
public final class ReflectionUtils {

	private ReflectionUtils() {
	}

	/**
	 * Read private field on given class instance.
	 *
	 * @param o The class instance (must not be {@code null}).
	 * @param name Name of the field to read.
	 * @param <T> Type of class.
	 * @return The value of given field (may be {@code null}).
	 * @throws NoSuchFieldException   If the field does not exist.
	 * @throws IllegalAccessException If the field is not accessible.
	 */
	public static <T> T readPrivateField(Object o, String name) throws NoSuchFieldException, IllegalAccessException {
		return doRead(o.getClass(), o, name);
	}

	public static <T> T readStaticPrivateField(Class klass, String name) throws NoSuchFieldException, IllegalAccessException {
		return doRead(klass, null, name);
	}

	@SuppressWarnings("unchecked")
	private static <T> T doRead(Class klass, Object instance, String name) throws NoSuchFieldException, IllegalAccessException {
		Field field = klass.getDeclaredField(name);
		field.setAccessible(true);
		return (T) field.get(instance);
	}

	/**
	 * Write value on private field of a given class instance.
	 *
	 * @param o The instance.
	 * @param name Name of the field to write.
	 * @param value The value.
	 * @param <T> Type of class.
	 * @throws NoSuchFieldException   If the field does not exist.
	 * @throws IllegalAccessException If the field is not accessible.
	 */
	public static <T> void writePrivateField(Object o, String name, T value) throws NoSuchFieldException, IllegalAccessException {
		doWrite(o.getClass(), o, name, value);
	}

	/**
	 * Write value on static private field of a given class.
	 *
	 * @param name Name of the field to write.
	 * @param value The value.
	 * @param <T> Type of class.
	 * @throws NoSuchFieldException   If the field does not exist.
	 * @throws IllegalAccessException If the field is not accessible.
	 */
	public static <T, V> void writeStaticPrivateField(Class<T> klass, String name, V value) throws NoSuchFieldException, IllegalAccessException {
		doWrite(klass, null, name, value);
	}

	/**
	 * Write value on static private field of a given class.
	 *
	 * @param name Name of the field to write.
	 * @param value The value.
	 * @param <T> Type of class.
	 * @throws NoSuchFieldException   If the field does not exist.
	 * @throws IllegalAccessException If the field is not accessible.
	 */
	private static <T, V> void doWrite(Class<T> klass, Object instance, String name, V value) throws NoSuchFieldException, IllegalAccessException {
		Field field = klass.getDeclaredField(name);
		field.setAccessible(true);

		// Remove final modifier
		int modifiers = field.getModifiers();
		Field modifierField = field.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(field, modifiers);

		field.set(instance, value);
	}
}
