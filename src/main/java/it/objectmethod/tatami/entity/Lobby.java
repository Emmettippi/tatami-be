package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import it.objectmethod.tatami.entity.enums.LobbyType;
import lombok.Data;

@Data
@Entity
@Table(name = "lobby")
public class Lobby {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_1_id")
	private Long userId1;

	@Column(name = "last_in_lobby_1")
	private Long lastInLobby1;

	@Column(name = "user_2_id")
	private Long userId2;

	@Column(name = "last_in_lobby_2")
	private Long lastInLobby2;

	@Column(name = "user_3_id")
	private Long userId3;

	@Column(name = "last_in_lobby_3")
	private Long lastInLobby3;

	@Column(name = "user_4_id")
	private Long userId4;

	@Column(name = "last_in_lobby_4")
	private Long lastInLobby4;

	@Column(name = "lobby_type")
	@Enumerated(EnumType.STRING)
	private LobbyType lobbyType;

	@Column(name = "lobby_name")
	private String lobbyName;

	@Column(name = "game_id")
	private Long gameId;

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
}
