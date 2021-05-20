package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import it.objectmethod.tatami.entity.enums.ObjectiveType;
import lombok.Data;

@Data
@Entity
@Table(name = "objective")
public class Objective {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "objective_type")
	@Enumerated(EnumType.STRING)
	private ObjectiveType objectiveType;

	@Column(name = "description")
	private String description;
}
