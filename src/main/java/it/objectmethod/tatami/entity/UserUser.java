package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	@ManyToOne
	@JoinColumn(nullable = false, name = "user_1_id")
	private User user1;

	@ManyToOne
	@JoinColumn(nullable = false, name = "user_2_id")
	private User user2;

	@Column(name = "relationship")
	@Enumerated(EnumType.STRING)
	private UserRelation relationship;
}
