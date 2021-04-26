package it.objectmethod.tatami.utils;

import java.util.Date;

public class Utils {

	public static boolean isBlank(String str) {
		return str == null || str.trim().isEmpty();
	}

	public static Date now() {
		return new Date();
	}

	@SafeVarargs
	public static <E> E coalesce(E... items) {
		if (items == null) {
			return null;
		}
		E item = null;
		for (E i : items) {
			if (i != null) {
				item = i;
				break;
			}
		}
		return item;
	}
}
