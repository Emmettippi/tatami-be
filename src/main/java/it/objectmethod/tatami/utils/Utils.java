package it.objectmethod.tatami.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

	public static final String TATAMI_AUTH_TOKEN = "tatami-auth-token";

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

	@SafeVarargs
	public static <E> List<E> asList(E... items) {
		if (items == null) {
			return null;
		}
		List<E> itemList = new ArrayList<>();
		for (E i : items) {
			itemList.add(i);
		}
		return itemList;
	}

	public static <E> E[][] rotate(E[][] square, boolean clockwise) {
		if (square == null) {
			return square;
		}
		int length = square.length;
		for (int i = 0; i < length; i++) {
			if (square[i].length != length) {
				return square;
			}
		}

		int specularI;
		int specularJ;
		for (int i = 0; i < length / 2; i++) {
			specularI = length - i - 1;
			for (int j = i; j < specularI; j++) {
				specularJ = length - j - 1;

				E tmp = square[i][j];
				if (clockwise) {
					square[i][j] = square[specularJ][i];
					square[specularJ][i] = square[specularI][specularJ];
					square[specularI][specularJ] = square[j][specularI];
					square[j][specularI] = tmp;
				} else {
					square[i][j] = square[j][specularI];
					square[j][specularI] = square[specularI][specularJ];
					square[specularI][specularJ] = square[specularJ][i];
					square[specularJ][i] = tmp;
				}
			}
		}

		return square;
	}

	public static String copy(String str) {
		if (str == null) {
			return null;
		}
		return new String(str);
	}

	public static Double parseDouble(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Integer parseInt(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean parseBool(String str) {
		if (!"1".equals(str) && !"0".equals(str)) {
			return null;
		}
		return Boolean.valueOf("1".equals(str));
	}

	public static DateTimeFormatter guessFormatter(String date) {
		if (date.contains("T")) {
			if (date.contains(".") && date.length() == 21) {
				return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S");
			}
			if (date.contains(".") && date.length() == 22) {
				return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
			}
			if (date.contains(".") && date.length() == 23) {
				return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
			} // 2020-10-16T09:22:15'

			// FallBack
			return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		}
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseDate(String str) {
		if (str == null || !(str.matches(localDateRegex) || str.matches(localDateTimeRegex)
			|| str.matches(localDateTimeNoSecRegex))) {
			return null;
		}
		try {
			if (str.matches(localDateRegex)) {
				return dateFormatter.parse(str);
			} else if (str.matches(localDateTimeRegex)) {
				return dateTimeFormatter.parse(str);
			} else if (str.matches(localDateTimeNoSecRegex)) {
				return dateTimeNoSecFormatter.parse(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static LocalDate parseLocalDate(String str) {
		if (str == null || !str.matches(localDateRegex)) {
			return null;
		}
		if (str.matches(localDateRegex)) {
			return LocalDate.parse(str);
		}
		return null;
	}

	public static LocalTime parseLocalTime(String str) {
		if (str == null || !(str.matches(localTimeRegex) || str.matches(localTimeNoSecRegex))) {
			return null;
		}
		if (str.matches(localTimeRegex)) {
			return LocalTime.parse(str);
		} else if (str.matches(localTimeNoSecRegex)) {
			return LocalTime.parse(str + ":00");
		}
		return null;
	}

	private static final String localDateRegex = "^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$";
	private static final String localTimeRegex = "^[0-2][0-9]:[0-5][0-9]:[0-5][0-9]$";
	private static final String localTimeNoSecRegex = "^[0-2][0-9]:[0-5][0-9]$";
	private static final String localDateTimeRegex = "^[0-9]{4}-[0-1][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]$";
	private static final String localDateTimeNoSecRegex = "^[0-9]{4}-[0-1][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]$";
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateTimeNoSecFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static final DateTimeFormatter localTimeNoSecFormatter = DateTimeFormatter.ofPattern("HH:mm");

	public static String toSafeString(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	public static String toSafeStringReplacing(String str, String safe, Character... unsafeChars) {
		if (str == null) {
			return "";
		}
		if (safe == null || unsafeChars == null) {
			return str;
		}
		for (Character ch : unsafeChars) {
			str = str.replaceAll(ch.toString(), safe);
		}
		return str;
	}

	public static String convertToString(Number num) {
		if (num == null) {
			return null;
		}
		return num.toString();
	}

	public static String convertToString(Date date) {
		return convertToString(date, false);
	}

	public static String convertToString(Date date, boolean time) {
		return convertToString(date, time, true);
	}

	public static String convertToString(Date date, boolean time, boolean seconds) {
		if (date == null) {
			return null;
		}
		if (time) {
			if (seconds) {
				return dateTimeFormatter.format(date);
			} else {
				return dateTimeNoSecFormatter.format(date);
			}
		} else {
			return dateFormatter.format(date);
		}
	}

	public static String convertToString(LocalDate date) {
		if (date == null) {
			return null;
		}
		return localDateFormatter.format(date);
	}

	public static String convertToString(LocalTime time) {
		return convertToString(time, true);
	}

	public static String convertToString(LocalTime time, boolean seconds) {
		if (time == null) {
			return null;
		}
		if (seconds) {
			return localTimeFormatter.format(time);
		} else {
			return localTimeNoSecFormatter.format(time);
		}
	}

	public static String convertToString(Boolean bool) {
		if (bool == null) {
			return null;
		}
		return Boolean.TRUE.equals(bool) ? "1" : "0";
	}
}
