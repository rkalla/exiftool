package com.thebuzzmedia.exiftool;

import com.thebuzzmedia.exiftool.core.StandardFormat;
import com.thebuzzmedia.exiftool.core.StandardTag;

import java.io.File;

import static java.util.Arrays.asList;

public class Example {
	public static final int ITERS = 100;
	public static final File[] IMAGES = new File("src/test/resources/images")
			.listFiles();

	public static void main(String[] args) throws Exception {
//		System.setProperty("imgscalr.ext.exiftool.withPath",
//				"D:\\Tools\\exiftool.exe");
//		System.setProperty("imgscalr.ext.exiftool.debug", "false");

		ExifTool tool = new ExifToolBuilder()
			.enableStayOpen()
			.build();

		for (File f : IMAGES) {
			System.out.println("\n[" + f.getName() + "]");
			System.out.println(tool.getImageMeta(f, StandardFormat.HUMAN_READABLE,
					asList((Tag[]) StandardTag.values())));
		}

		tool.close();
	}
}