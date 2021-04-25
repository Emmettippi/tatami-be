package it.objectmethod.tatami.dto.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import it.objectmethod.tatami.dto.LobbyDto;
import it.objectmethod.tatami.entity.Lobby;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.repository.UserRepository;

@Mapper(componentModel = "spring")
public abstract class LobbyMapper {

	@Autowired
	private UserRepository userRepository;

	public abstract LobbyDto toDto(Lobby entity);

	public abstract List<LobbyDto> toDto(List<Lobby> entity);

	public abstract Lobby toEntity(LobbyDto dto);

	@AfterMapping
	public void afterMappingDto(@MappingTarget LobbyDto dto, Lobby entity) {
		User user = null;
		if (dto.getUserId1() != null) {
			user = userRepository.getOne(dto.getUserId1());
			if (user != null) {
				dto.setNickname1(user.getNickname());
			}
		}
		if (dto.getUserId2() != null) {
			user = null;
			user = userRepository.getOne(dto.getUserId2());
			if (user != null) {
				dto.setNickname2(user.getNickname());
			}
		}
		if (dto.getUserId3() != null) {
			user = null;
			user = userRepository.getOne(dto.getUserId3());
			if (user != null) {
				dto.setNickname3(user.getNickname());
			}
		}
		if (dto.getUserId4() != null) {
			user = null;
			user = userRepository.getOne(dto.getUserId4());
			if (user != null) {
				dto.setNickname4(user.getNickname());
			}
		}
	}
}
