package it.objectmethod.tatami.utils;

import java.util.Date;

public class Utils {

	public static boolean isBlank(String str) {
		return str == null || str.trim().isEmpty();
	}

	public static Date now() {
		return new Date();
	}
}
