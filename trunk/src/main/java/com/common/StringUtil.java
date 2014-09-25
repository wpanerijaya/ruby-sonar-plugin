package com.common;

public class StringUtil {
	public static String safeString(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	public static Integer safeInteger(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}
}
