package it.objectmethod.tatami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.controller.dto.LoginDto;
import it.objectmethod.tatami.controller.dto.UserSearchQueryParams;
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.UserEditDto;
import it.objectmethod.tatami.dto.UserSearchResponseDto;
import it.objectmethod.tatami.dto.mapper.UserMapper;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.UserUser;
import it.objectmethod.tatami.entity.enums.UserRelation;
import it.objectmethod.tatami.entity.enums.UserStatus;
import it.objectmethod.tatami.repository.CustomRepository;
import it.objectmethod.tatami.repository.UserRepository;
import it.objectmethod.tatami.repository.UserUserRepository;
import it.objectmethod.tatami.utils.Utils;

@Service
public class UserService {

	@Autowired
	private JWTService jwtService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserUserRepository userUserRepository;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CustomRepository customRepository;

	public UserDto login(LoginDto loginDto) {
		if (loginDto == null || Utils.isBlank(loginDto.getPassword()) || Utils.isBlank(loginDto.getUsername())) {
			return null;
		}
		String md5Pass = userRepository.md5Password(loginDto.getPassword());
		User user = userRepository.findByUsernameAndPassword(loginDto.getUsername(), md5Pass);
		String token = null;
		if (user != null) {
			user.setUserStatus(UserStatus.ONLINE);
			user.setLastOnline(Utils.now());
			user = userRepository.save(user);

			token = jwtService.createJWTToken(user);
		}
		return userMapper.toDto(user, token);
	}

	public UserDto create(UserDto dto) {
		User existing = Utils.coalesce(userRepository.findByUsername(dto.getUsername()),
			userRepository.findByEmailIgnoreCase(dto.getEmail()));
		if (existing != null) {
			return null;
		}
		String md5Pass = userRepository.md5Password(dto.getPassword());
		User entity = userMapper.toEntity(dto);
		entity.setUserStatus(UserStatus.OFFLINE);
		entity.setLastOnline(Utils.now());
		entity.setPassword(md5Pass);
		return userMapper.toDto(userRepository.save(entity));
	}

	public UserDto update(UserEditDto dto, boolean isOnline) {
		User user = null;
		user = userMapper.toEntity(dto);
		User oldUser = userRepository.getOne(dto.getId());
		if (!Utils.isBlank(dto.getNewPassword())) {
			String md5OldPass = userRepository.md5Password(dto.getPassword());
			if (md5OldPass == null || oldUser == null || !md5OldPass.equals(oldUser.getPassword())) {
				return null;
			}
			String md5NewPass = userRepository.md5Password(dto.getNewPassword());
			user.setPassword(md5NewPass);
		} else {
			user.setPassword(oldUser.getPassword());
		}
		if (isOnline) {
			user.setUserStatus(UserStatus.ONLINE);
			user.setLastOnline(Utils.now());
		}
		user = userRepository.save(user);
		return userMapper.toDto(user);
	}

	public UserDto forceGetOne(Long id) {
		if (id == null) {
			return null;
		}
		return userMapper.toDto(userRepository.getOne(id));
	}

	public UserDto getOne(Long id, Long loggedUserId) {
		return this.getOne(id, loggedUserId, false);
	}

	public UserDto getOne(Long id, Long loggedUserId, boolean forceGet) {
		if (id == null) {
			return null;
		}
		if (forceGet) {
			return userMapper.toDto(userRepository.getOne(id));
		}
		if (loggedUserId == null) {
			return null;
		}

		UserUser blockedRelation = userUserRepository.findByUser1_IdAndUser2_IdAndRelationship(id, loggedUserId,
			UserRelation.BLOCKED);
		if (blockedRelation != null) {
			return null;
		}

		User user = userRepository.getOne(id);
		if (user != null) {
			user.setPassword(null);
		}
		return userMapper.toDto(user);
	}

	public void delete(Long id) {
		if (id != null) {
			userUserRepository.deleteByUserId(id);
			userRepository.deleteById(id);
		}
		return;
	}

	public List<UserSearchResponseDto> search(UserSearchQueryParams params) {
		return customRepository.searchForFriends(params);
	}

	public List<UserDto> getFriends(Long id) {
		return userMapper.toDto(userRepository.findByUserIdAndRelation(id, UserRelation.FRIEND.name()));
	}

	public List<UserDto> getAskingFriends(Long id) {
		return userMapper.toDto(userRepository.findByUserIdAndRelation(id, UserRelation.ASKING_FRIENDSHIP.name()));
	}

	public List<UserDto> getPendingFriendRequests(Long id) {
		return userMapper.toDto(userRepository.findByUserIdAndRelation(id, UserRelation.PENDING_FRIENDSHIP.name()));
	}

	public List<UserDto> getBlocked(Long id) {
		return userMapper.toDto(userRepository.findByUserIdAndRelation(id, UserRelation.BLOCKED.name()));
	}

	public long count() {
		return this.userRepository.count();
	}

	public List<UserDto> findPaged(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<User> users = this.userRepository.findAll(pageable);
		return userMapper.toDto(users.getContent());
	}

	public List<UserDto> findByStatusNotOffline(int page, int size) {
		return userMapper
			.toDto(this.userRepository.findByStatusNotOffline(Long.valueOf(page * size), Long.valueOf(size)));
	}

	public UserDto updateLastOnline(Long id) {
		if (id == null) {
			return null;
		}
		User user = userRepository.getOne(id);
		if (user == null) {
			return null;
		}
		user.setLastOnline(Utils.now());
		return userMapper.toDto(userRepository.save(user));
	}
}
