/**
 * Copyright 2011 The Buzz Media, LLC
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

package com.thebuzzmedia.exiftool;

import com.thebuzzmedia.exiftool.core.StandardTag;

import java.io.File;
import java.io.IOException;

import static java.util.Arrays.asList;

public class Benchmark {
	public static final int ITERS = 25;
	public static final Tag[] TAGS = StandardTag.values();
	public static final File[] IMAGES = new File("src/test/resources/images")
			.listFiles();

	public static void main(String[] args) throws Exception {
//		System.setProperty("exiftool.withPath", "D:\\Tools\\exiftool.exe");
//		System.setProperty("exiftool.debug", "false");

		System.out.println("Benchmark [tags=" + TAGS.length + ", images="
				+ IMAGES.length + ", iterations=" + ITERS + "]");
		System.out.println("\t" + (IMAGES.length * ITERS)
				+ " ExifTool process calls, "
				+ (TAGS.length * IMAGES.length * ITERS)
				+ " total operations.\n");

		ExifTool tool = new ExifToolBuilder()
			.build();

		ExifTool toolSO = new ExifToolBuilder()
			.enableStayOpen()
			.build();

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

		for (File image : images) {
			tool.getImageMeta(image, asList(TAGS));
		}

		return (System.currentTimeMillis() - startTime);
	}
}