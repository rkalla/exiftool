package com.thebuzzmedia.exiftool.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class IOs {

	private static final String BR = System.getProperty("line.separator");

	private IOs() {
	}

	public static String readInputStream(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line).append(BR);
			}
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		finally {
			try {
				br.close();
			}
			catch (IOException ex) {
				// No worries
			}
		}

		return sb.toString().trim();
	}
}
