package com.thebuzzmedia.exiftool;

import java.io.File;
import java.io.IOException;

import com.thebuzzmedia.exiftool.ExifTool.Feature;
import com.thebuzzmedia.exiftool.ExifTool.Tag;

public class Benchmark {
	public static final int ITERS = 25;
	public static final Tag[] TAGS = Tag.values();
	public static final File[] IMAGES = new File("src/test/resources")
			.listFiles();

	public static void main(String[] args) throws IOException,
			InterruptedException {
//		System.setProperty("exiftool.path", "D:\\Tools\\exiftool.exe");
//		System.setProperty("exiftool.debug", "false");

		System.out.println("Benchmark [tags=" + TAGS.length + ", images="
				+ IMAGES.length + ", iterations=" + ITERS + "]");
		System.out.println("\t" + (IMAGES.length * ITERS)
				+ " ExifTool process calls, "
				+ (TAGS.length * IMAGES.length * ITERS)
				+ " total operations.\n");

		ExifTool tool = new ExifTool();
		ExifTool toolSO = new ExifTool(Feature.STAY_OPEN);

		/*
		 * -stay_open False
		 */
		System.out.println("\t[-stay_open False]");
		long elapsedTime = 0;

		for (int i = 0; i < ITERS; i++)
			elapsedTime += run(tool, IMAGES);

		System.out.println("\t\tElapsed Time: " + elapsedTime + " ms ("
				+ ((double) elapsedTime / 1000) + " secs)");

		/*
		 * -stay_open True
		 */
		System.out.println("\t[-stay_open True]");
		long elapsedTimeSO = 0;

		for (int i = 0; i < ITERS; i++) {
			elapsedTimeSO += run(toolSO, IMAGES);
		}

		System.out.println("\t\tElapsed Time: " + elapsedTimeSO + " ms ("
				+ ((double) elapsedTimeSO / 1000) + " secs - "
				+ ((float) elapsedTime / (float) elapsedTimeSO) + "x faster)");

		// Shut down the running exiftool proc.
		toolSO.close();
	}

	private static long run(ExifTool tool, File[] images)
			throws IllegalArgumentException, SecurityException, IOException {
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < images.length; i++)
			tool.getImageMeta(images[i], TAGS);

		return (System.currentTimeMillis() - startTime);
	}
}