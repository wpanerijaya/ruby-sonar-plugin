package com.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	private static List<File> resultFile = new ArrayList<File>();

	private FileUtil() {

	}

	public static List<File> findDirectories(final File basedir,
			final String name, Boolean flag) {
		if (basedir == null) {
			return resultFile;
		}
		if (flag) {
			resultFile = new ArrayList<File>();
		}
		String[] files = basedir.list();

		for (String filename : files) {
			File file = new File(basedir.getAbsolutePath() + "/" + filename);
			if (file.isDirectory() && filename.equals(name)) {
				resultFile.add(file);
			} else if (file.isDirectory()) {
				findDirectories(file, name, false);
			}
		}
		return resultFile;
	}

	public static List<File> findDirectories(final String basedir,
			final String name, Boolean flag) {
		if (basedir == null) {
			return resultFile;
		}
		if (flag) {
			resultFile = new ArrayList<File>();
		}
		File dir = new File(basedir);
		String[] files = dir.list();

		for (String filename : files) {
			File file = new File(dir.getAbsolutePath() + "/" + filename);
			if (file.isDirectory() && filename.equals(name)) {
				resultFile.add(file);
			} else if (file.isDirectory()) {
				findDirectories(file, name, false);
			}
		}
		return resultFile;
	}
}
