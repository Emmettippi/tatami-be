package it.objectmethod.tatami.entity.enums;

import java.util.ArrayList;
import java.util.List;

public enum PercentageOperation {

	ASK_FRIENDSHIP, JOIN_LOBBY;

	public static List<PercentageOperation> all() {
		List<PercentageOperation> all = new ArrayList<>();
		all.add(ASK_FRIENDSHIP);
		all.add(JOIN_LOBBY);
		return all;
	}
}
