package com.thebuzzmedia.exiftool;

import java.io.File;
import java.io.IOException;

public class Example {
	public static final int ITERS = 100;
	public static final File[] IMAGES = new File("src/test/resources")
			.listFiles();

	public static void main(String[] args) throws IOException,
			InterruptedException {
//		System.setProperty("imgscalr.ext.exiftool.path",
//				"D:\\Tools\\exiftool.exe");
//		System.setProperty("imgscalr.ext.exiftool.debug", "false");

		ExifTool tool = new ExifTool();

		for (File f : IMAGES) {
			System.out.println("\n[" + f.getName() + "]");
			System.out.println(tool.getImageMeta(f, Format.HUMAN_READABLE,
					Tag.values()));
		}

		tool.close();
	}
}