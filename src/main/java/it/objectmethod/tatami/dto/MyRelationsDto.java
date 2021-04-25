package it.objectmethod.tatami.dto;

import java.util.List;

import lombok.Data;

@Data
public class MyRelationsDto {

	public List<UserDto> friends;
	public List<UserDto> askingFriends;
	public List<UserDto> pendingFriends;
	public List<UserDto> blocked;
}
