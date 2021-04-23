package it.objectmethod.tatami.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.objectmethod.tatami.dto.UserUserDto;
import it.objectmethod.tatami.entity.UserUser;

@Mapper(componentModel = "spring")
public abstract class UserUserMapper {

	@Mapping(target = "user1Id", source = "user1.id")
	@Mapping(target = "user2Id", source = "user2.id")
	public abstract UserUserDto toDto(UserUser entity);

	public abstract List<UserUserDto> toDto(List<UserUser> entities);
}
