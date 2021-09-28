package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import it.objectmethod.tatami.entity.enums.PlayerColor;
import lombok.Data;

@Data
@Entity
@Table(name = "player")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "objective_id")
	private Long objectiveId;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "color")
	@Enumerated(EnumType.STRING)
	private PlayerColor color;

	@Column(name = "x")
	private Long x;

	@Column(name = "y")
	private Long y;
}
