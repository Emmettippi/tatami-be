package it.objectmethod.tatami.entity.enums;

import static it.objectmethod.tatami.entity.enums.GreenCardType.ACTION;
import static it.objectmethod.tatami.entity.enums.GreenCardType.PERMANENT;
import static it.objectmethod.tatami.entity.enums.GreenCardType.POWER;

public enum GreenCardSubtype {

	MOVEMENT(POWER), ECONOMY(POWER), CONTROL(POWER), BURDEN(ACTION), SLOWDOWN(ACTION), ECODOWN(ACTION), QUICKBUY(
		ACTION), CHALLENGE(ACTION), EMERGENCY(ACTION), GET_FREE_TATAMI(ACTION), TELEPORT(ACTION), CHALLENGE_DRAW(
			ACTION), UNLOCK_OUT(ACTION), LOCK_ONE(ACTION), INJURY(ACTION), DIG(ACTION), STEAL(ACTION), GET_ANY_TATAMI(
				ACTION), TIME_WALK(ACTION), DISCOUNT(PERMANENT), TRANSITER(PERMANENT), PIGGY_BANK(PERMANENT), REROLLER(
					PERMANENT), BIG_MONEY(PERMANENT), REROLL_1(PERMANENT), SPEEDY(PERMANENT), UNLOCKER(
						PERMANENT), KEEPER(PERMANENT), MONEY_KEEPER(PERMANENT), NO_ADVANTAGE(
							PERMANENT), CHALLENGER(PERMANENT), DIGGER(PERMANENT), CONTROLLER(PERMANENT);

	GreenCardType greenCardType;

	GreenCardSubtype(GreenCardType greenCardType) {
		this.greenCardType = greenCardType;
	}

	public GreenCardType greenCardType() {
		return this.greenCardType;
	}
}
