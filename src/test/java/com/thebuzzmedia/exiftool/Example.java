package com.thebuzzmedia.exiftool;

import java.io.File;

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
			System.out.println(tool.getImageMeta(f, Format.HUMAN_READABLE,
					Tag.values()));
		}

		tool.close();
	}
}