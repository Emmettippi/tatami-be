package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import it.objectmethod.tatami.entity.enums.LogAction;
import lombok.Data;

@Data
//@Entity
//@Table(name = "log_game")
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "game_id")
	private Long gameId;

	@Column(name = "log_number")
	private Long logNumber;

	@Column(name = "log_action")
	@Enumerated(EnumType.STRING)
	private LogAction logAction;

	@Column(name = "log_status")
	@Enumerated(EnumType.STRING)
	private LogAction logStatus;

	@Column(name = "num_param_1")
	private Long numParam1;

	@Column(name = "num_param_2")
	private Long numParam2;

	@Column(name = "num_param_3")
	private Long numParam3;

	@Column(name = "num_param_4")
	private Long numParam4;

	@Column(name = "num_param_5")
	private Long numParam5;

	@Column(name = "num_param_6")
	private Long numParam6;

	@Column(name = "str_param_1")
	private Long strParam1;

	@Column(name = "str_param_2")
	private Long strParam2;

	@Column(name = "str_param_3")
	private Long strParam3;

	@Column(name = "str_param_4")
	private Long strParam4;

	@Column(name = "str_param_5")
	private Long strParam5;

	@Column(name = "str_param_6")
	private Long strParam6;
}
