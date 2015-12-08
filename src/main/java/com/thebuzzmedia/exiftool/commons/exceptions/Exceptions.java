package com.thebuzzmedia.exiftool.commons.exceptions;

/**
 * Created by mickael on 12/8/15.
 */
public class Exceptions {

	/**
	 * Coerce an unchecked Throwable to a RuntimeException:
	 *
	 * <ul>
	 *   <li>If the Throwable is an Error, throw it.</li>
	 *   <li>If it is a {@link RuntimeException} return it.</li>
	 *   <li>Otherwise, throw {@link IllegalStateException}.</li>
	 * </ul>
	 */
	public static RuntimeException launderThrowable(Throwable t) {
		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		}
		else if (t instanceof Error) {
			throw (Error) t;
		}
		else {
			throw new IllegalStateException("Not unchecked", t);
		}
	}
}
