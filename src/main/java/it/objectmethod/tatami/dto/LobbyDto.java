package it.objectmethod.tatami.dto;

import it.objectmethod.tatami.entity.enums.LobbyType;
import lombok.Data;

@Data
public class LobbyDto {

	private Long id;
	private Long userId1;
	private String nickname1;
	private Long lastInLobby1;
	private Long userId2;
	private String nickname2;
	private Long lastInLobby2;
	private Long userId3;
	private String nickname3;
	private Long lastInLobby3;
	private Long userId4;
	private String nickname4;
	private Long lastInLobby4;
	private LobbyType lobbyType;
	private String lobbyName;
	private Long gameId;

	public boolean isFull() {
		return this.userId1 != null && this.userId2 != null && this.userId3 != null && this.userId4 != null;
	}

	public boolean isEmpty() {
		return this.userId1 == null && this.userId2 == null && this.userId3 == null && this.userId4 == null;
	}
}
