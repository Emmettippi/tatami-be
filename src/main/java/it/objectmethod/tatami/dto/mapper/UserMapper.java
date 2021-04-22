package it.objectmethod.tatami.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.entity.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

	public abstract UserDto toDto(User user);

	public abstract List<UserDto> toDto(List<User> user);

	public abstract User toEntity(UserDto dto);
}
