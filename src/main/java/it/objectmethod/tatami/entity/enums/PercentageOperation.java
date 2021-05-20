package it.objectmethod.tatami.entity.enums;

import java.util.ArrayList;
import java.util.List;

public enum PercentageOperation {

	ASK_FRIENDSHIP, JOIN_LOBBY, START_GAME;

	public static List<PercentageOperation> all() {
		List<PercentageOperation> all = new ArrayList<>();
		all.add(ASK_FRIENDSHIP);
		all.add(JOIN_LOBBY);
		all.add(START_GAME);
		return all;
	}

	public static Boolean forLock(PercentageOperation po) {
		return Boolean.FALSE;
	}

	public Boolean forLock() {
		return Boolean.FALSE;
	}
}
