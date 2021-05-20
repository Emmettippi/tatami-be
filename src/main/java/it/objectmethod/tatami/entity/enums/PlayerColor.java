package it.objectmethod.tatami.entity.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.objectmethod.tatami.utils.Utils;

public enum PlayerColor {
	WHITE(0), YELLOW(1), ORANGE(2), RED(3), MAGENTA(4), VIOLET(5), BLUE(6), CYAN(7), LIME(8), GREEN(9), BROWN(
		10), BLACK(11);

	public int number;

	private static Random rand = new Random();

	PlayerColor(int number) {
		this.number = number;
	}

	public static PlayerColor fromNumber(int number) {
		for (PlayerColor color : PlayerColor.values()) {
			if (color.number == number) {
				return color;
			}
		}
		return null;
	}

	public static PlayerColor chooseRandomExcept(PlayerColor... eceptColors) {
		return chooseRandomExcept(Utils.asList(eceptColors));
	}

	public static PlayerColor chooseRandomExcept(List<PlayerColor> eceptColors) {
		List<Integer> except = new ArrayList<>();
		if (eceptColors != null) {
			for (PlayerColor ec : eceptColors) {
				if (ec != null) {
					except.add(Integer.valueOf(ec.number));
				}
			}
		}
		List<Integer> availables = new ArrayList<>();
		for (PlayerColor c : PlayerColor.values()) {
			Integer num = Integer.valueOf(c.number);
			if (!except.contains(num)) {
				availables.add(num);
			}
		}

		if (availables.isEmpty()) {
			return null;
		}
		return fromNumber(rand.nextInt(availables.size()));
	}
}
