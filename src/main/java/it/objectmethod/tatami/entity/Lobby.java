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
@Table(name = "lobby")
public class Lobby {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id_1")
	private Long userId1;

	@Column(name = "last_in_lobby_1")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInLobby1;

	@Column(name = "user_id_2")
	private Long userId2;

	@Column(name = "last_in_lobby_2")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInLobby2;

	@Column(name = "user_id_3")
	private Long userId3;

	@Column(name = "last_in_lobby_3")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInLobby3;

	@Column(name = "user_id_4")
	private Long userId4;

	@Column(name = "last_in_lobby_4")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastInLobby4;
}
