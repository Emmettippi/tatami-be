package it.objectmethod.tatami.dto;

import java.util.List;

import it.objectmethod.tatami.entity.enums.LobbyType;
import it.objectmethod.tatami.entity.enums.PlayerColor;
import it.objectmethod.tatami.utils.Utils;
import lombok.Data;

@Data
public class LobbyDto {

	private Long id;
	private String gameOrder;

	private Long userId1;
	private String nickname1;
	private Long lastInLobby1;
	private PlayerColor color1;

	private Long userId2;
	private String nickname2;
	private Long lastInLobby2;
	private PlayerColor color2;

	private Long userId3;
	private String nickname3;
	private Long lastInLobby3;
	private PlayerColor color3;

	private Long userId4;
	private String nickname4;
	private Long lastInLobby4;
	private PlayerColor color4;

	private LobbyType lobbyType;
	private String lobbyName;
	private Long gameId;
	private Boolean closed;

	private Long friends;

	public boolean isFull() {
		return this.userId1 != null && this.userId2 != null && this.userId3 != null && this.userId4 != null;
	}

	public boolean isEmpty() {
		return this.userId1 == null && this.userId2 == null && this.userId3 == null && this.userId4 == null;
	}

	public boolean isUserAlreadyInside(Long userId) {
		if (userId == null) {
			return true;
		}
		return !userId.equals(this.userId1) && !userId.equals(this.userId2) && !userId.equals(this.userId3)
			&& !userId.equals(this.userId4);
	}

	public boolean isClosed() {
		return Boolean.TRUE.equals(closed);
	}

	public List<PlayerColor> getColorsInUse() {
		return Utils.asList(this.color1, this.color2, this.color3, this.color4);
	}
}
