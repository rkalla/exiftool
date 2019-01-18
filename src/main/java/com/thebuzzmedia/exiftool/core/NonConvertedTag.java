package com.thebuzzmedia.exiftool.core;

import com.thebuzzmedia.exiftool.Tag;

/**
 * Utility class used to generate tags which are not print converted. This is
 * done in Exiftool by suffixing <code>#</code> to the tag and it has the same
 * effect of <code>-n</code> but applied on a per-tag basis.
 * 
 * The class wraps another tag and manages its different query name. By design
 * <code>NonConvertedTag.of(Tag.ANY)</code> is not equal to <code>Tag.ANY</code>
 * since it's possible to query two different formats of the same tag.
 * 
 * @author Jack (jack@pixbits.com)
 */

public class NonConvertedTag implements Tag {
	private final Tag original;

	public NonConvertedTag(Tag original) {
		this.original = original;
	}

	@Override
	public String getName() {
		return original.getName() + "#";
	}

	@Override
	public String getDisplayName() {
		return original.getName();
	}

	@Override
	public <T> T parse(String value) {
		return original.parse(value);
	}

	@Override
	public String toString() {
		return original.toString();
	}

	@Override
	public int hashCode() {
		return original.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof NonConvertedTag && ((NonConvertedTag) other).original.equals(original);
	}

	public static Tag of(final Tag original) {
		return new NonConvertedTag(original);
	};
}
