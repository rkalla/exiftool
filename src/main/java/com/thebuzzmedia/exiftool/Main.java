package com.thebuzzmedia.exiftool;

import java.io.File;
import java.io.IOException;

import com.thebuzzmedia.exiftool.ExifTool.Feature;
import com.thebuzzmedia.exiftool.ExifTool.Tag;

public class Main {
	public static final int ITERS = 1;
	public static String file = "img_1972.jpg";

	public static void main(String[] args) throws IOException,
			InterruptedException {
		System.setProperty("imgscalr.ext.exiftool.path",
				"C:\\Users\\Riyad\\Downloads\\exiftool.exe");
		System.setProperty("imgscalr.ext.exiftool.debug", "true");

		ExifTool et1 = new ExifTool();
		ExifTool et2 = new ExifTool(Feature.STAY_OPEN);

		long s = System.currentTimeMillis();
//		for (int i = 0; i < ITERS; i++)
//			et1.getImageMeta(new File(file), Tag.values());
//
//		System.out.println("Stay Open FALSE, Elapsed Time: "
//				+ (System.currentTimeMillis() - s) + " ms\n");

		s = System.currentTimeMillis();
		for (int i = 0; i < ITERS; i++)
			et2.getImageMeta(new File(file), Tag.values());

		System.out.println("Stay Open  TRUE, Elapsed Time: "
				+ (System.currentTimeMillis() - s) + " ms");

		et2.close();
	}
}