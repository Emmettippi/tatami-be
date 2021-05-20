package it.objectmethod.tatami.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.objectmethod.tatami.entity.enums.UserStatus;
import lombok.Data;

@Data
@Entity
@Table(name = "user_info")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "email")
	private String email;

	@Column(name = "user_status")
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	@Column(name = "last_online")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastOnline;

	//	@Lob
	//	@Column(name = "profile_image")
	//	private byte[] profileImage;
}
