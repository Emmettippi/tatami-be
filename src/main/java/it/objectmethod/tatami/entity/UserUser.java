package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import it.objectmethod.tatami.entity.enums.UserRelation;
import lombok.Data;

@Data
@Entity
@Table(name = "user_user")
public class UserUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id_1")
	private Long userId1;

	@Column(name = "user_id_2")
	private Long userId2;

	@Column(name = "relationship")
	@Enumerated(EnumType.STRING)
	private UserRelation relationship;
}
