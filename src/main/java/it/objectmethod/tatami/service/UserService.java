package it.objectmethod.tatami.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.controller.dto.LoginDto;
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.mapper.UserMapper;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.enums.UserStatus;
import it.objectmethod.tatami.repository.UserRepository;
import it.objectmethod.tatami.utils.Utils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	public UserDto login(LoginDto loginDto) {
		if (loginDto == null || Utils.isBlank(loginDto.getPassword()) || Utils.isBlank(loginDto.getUsername())) {
			return null;
		}
		String md5Pass = userRepository.md5Password(loginDto.getPassword());
		User user = userRepository.findByUsernameAndPassword(loginDto.getUsername(), md5Pass);
		if (user != null) {
			user.setUserStatus(UserStatus.ONLINE);
			user.setLastOnline(Utils.now());
			user = userRepository.save(user);
		}
		return userMapper.toDto(user);
	}

	public UserDto create(UserDto dto) {
		User existing = userRepository.findByUsername(dto.getUsername());
		if (existing != null) {
			return null;
		}
		String md5Pass = userRepository.md5Password(dto.getPassword());
		User entity = userMapper.toEntity(dto);
		entity.setPassword(md5Pass);
		return userMapper.toDto(userRepository.save(entity));
	}

	public UserDto update(UserDto dto) {
		if (dto == null || dto.getId() == null) {
			return dto;
		}
		User old = userRepository.getOne(dto.getId());
		User user = null;
		if (old.getPassword().equals(dto.getPassword())) {
			user = userMapper.toEntity(dto);
			if (!Utils.isBlank(dto.getNewPassword())) {
				String md5NewPass = userRepository.md5Password(dto.getNewPassword());
				user.setPassword(md5NewPass);
			}
			user.setUserStatus(UserStatus.ONLINE);
			user.setLastOnline(Utils.now());
			user = userRepository.save(user);
		}
		return userMapper.toDto(user);
	}

	public UserDto getOne(Long id) {
		if (id == null) {
			return null;
		}
		User user = userRepository.getOne(id);
		if (user != null) {
			user.setPassword(null);
		}
		return userMapper.toDto(user);
	}

	public void delete(UserDto dto) {
		if (dto == null || dto.getId() == null) {
			return;
		}
		User old = userRepository.getOne(dto.getId());
		if (old.getPassword().equals(dto.getPassword())) {
			// TODO add userUser logic
			userRepository.deleteById(dto.getId());
		}
		return;
	}

	public List<UserDto> search(String nickname) {
		if (Utils.isBlank(nickname)) {
			return new ArrayList<>();
		}
		return userMapper.toDto(userRepository.findByNicknameContains(nickname));
	}
}
