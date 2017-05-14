package com.thebuzzmedia.exiftool.tests;

import java.util.List;

import org.mockito.ArgumentMatchers;

/**
 * Static Mockito Utilities.
 * Used in test only.
 */
public final class MockitoUtils {

	// Ensure non instantiation.
	private MockitoUtils() {
	}

	/**
	 * Replacement for deprecated Mockito#anyListOf.
	 *
	 * @param klass The class.
	 * @param <T> Type of elements.
	 * @return List of elements of type T.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> anyListOf(Class<T> klass) {
		return (List<T>) ArgumentMatchers.anyList();
	}
}
