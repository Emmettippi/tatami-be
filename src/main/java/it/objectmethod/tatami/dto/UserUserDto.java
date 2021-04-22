package it.objectmethod.tatami.dto;

import it.objectmethod.tatami.entity.enums.UserRelation;
import lombok.Data;

@Data
public class UserUserDto {

	private Long id;
	private Long user1Id;
	private Long user2Id;
	private UserRelation relationship;
}
