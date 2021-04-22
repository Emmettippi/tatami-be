package it.objectmethod.tatami.entity.enums;

import static it.objectmethod.tatami.entity.enums.GreenCardType.ACTION;
import static it.objectmethod.tatami.entity.enums.GreenCardType.PERMANENT;
import static it.objectmethod.tatami.entity.enums.GreenCardType.POWER;

public enum GreenCardSubtype {

	MOVEMENT(POWER), ECONOMY(POWER), CONTROL(POWER), CHALLENGE(ACTION), REROLLER(PERMANENT);

	GreenCardType greenCardType;

	GreenCardSubtype(GreenCardType greenCardType) {
		this.greenCardType = greenCardType;
	}

	GreenCardType greenCardType() {
		return this.greenCardType;
	}
}
