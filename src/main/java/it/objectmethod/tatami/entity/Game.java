package it.objectmethod.tatami.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "game")
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "player_id_1")
	private Long playerId1;

	@Column(name = "last_in_game_1")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInGame1;

	@Column(name = "player_id_2")
	private Long playerId2;

	@Column(name = "last_in_game_2")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInGame2;

	@Column(name = "player_id_3")
	private Long playerId3;

	@Column(name = "last_in_game_3")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInGame3;

	@Column(name = "player_id_4")
	private Long playerId4;

	@Column(name = "last_in_game_4")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInGame4;

	@Column(name = "game_name")
	private String gameName;

	@Column(name = "board")
	private String board;
}
