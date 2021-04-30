package it.objectmethod.tatami.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.controller.dto.LoginDto;
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.mapper.UserMapper;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.UserUser;
import it.objectmethod.tatami.entity.enums.UserRelation;
import it.objectmethod.tatami.entity.enums.UserStatus;
import it.objectmethod.tatami.repository.UserRepository;
import it.objectmethod.tatami.repository.UserUserRepository;
import it.objectmethod.tatami.utils.Utils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserUserRepository userUserRepository;
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
		User existing = Utils.coalesce(userRepository.findByUsername(dto.getUsername()),
			userRepository.findByEmail(dto.getEmail()));
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

	public UserDto update(UserDto dto) {
		return this.update(dto, false);
	}

	public UserDto update(UserDto dto, boolean avoidCheck) {
		User user = null;
		if (this.checkMyself(dto, avoidCheck)) {
			user = userMapper.toEntity(dto);
			if (!Utils.isBlank(dto.getNewPassword())) {
				String md5NewPass = userRepository.md5Password(dto.getNewPassword());
				user.setPassword(md5NewPass);
			}
			if (!avoidCheck) {
				user.setUserStatus(UserStatus.ONLINE);
				user.setLastOnline(Utils.now());
			}
			user = userRepository.save(user);
		}
		return userMapper.toDto(user);
	}

	public UserDto forceGetOne(Long id) {
		if (id == null) {
			return null;
		}
		return userMapper.toDto(userRepository.getOne(id));
	}

	public UserDto getOne(Long id, UserDto mySelf) {
		return this.getOne(id, mySelf, false);
	}

	public UserDto getOne(Long id, UserDto mySelf, boolean avoidCheck) {
		if (id == null || !this.checkMyself(mySelf, avoidCheck)) {
			return null;
		}

		UserUser blockedRelation = userUserRepository.findByUser1_IdAndUser2_IdAndRelationship(id, mySelf.getId(),
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

	public void delete(UserDto dto) {
		this.delete(dto, false);
	}

	public void delete(UserDto dto, boolean avoidCheck) {
		if (!this.checkMyself(dto, avoidCheck)) {
			return;
		}
		User old = userRepository.getOne(dto.getId());
		if (old.getPassword().equals(dto.getPassword())) {
			userUserRepository.deleteByUserId(dto.getId());
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

	public List<UserDto> getFriends(Long id, UserDto mySelf) {
		return this.getFriends(id, mySelf, false);
	}

	public List<UserDto> getFriends(Long id, UserDto mySelf, boolean avoidCheck) {
		if (!this.checkMyself(mySelf, avoidCheck)) {
			return null;
		}
		return userMapper.toDto(userRepository.findByUserIdAndRelation(id, UserRelation.FRIEND.name()));
	}

	public List<UserDto> getAskingFriends(Long id, UserDto mySelf) {
		return this.getAskingFriends(id, mySelf, false);
	}

	public List<UserDto> getAskingFriends(Long id, UserDto mySelf, boolean avoidCheck) {
		if (!this.checkMyself(mySelf, avoidCheck)) {
			return null;
		}
		return userMapper.toDto(userRepository.findByUserIdAndRelation(id, UserRelation.ASKING_FRIENDSHIP.name()));
	}

	public List<UserDto> getPendingFriendRequests(Long id, UserDto mySelf) {
		return this.getPendingFriendRequests(id, mySelf, false);
	}

	public List<UserDto> getPendingFriendRequests(Long id, UserDto mySelf, boolean avoidCheck) {
		if (!this.checkMyself(mySelf, avoidCheck)) {
			return null;
		}
		return userMapper.toDto(userRepository.findByUserIdAndRelation(id, UserRelation.PENDING_FRIENDSHIP.name()));
	}

	public List<UserDto> getBlocked(Long id, UserDto mySelf) {
		return this.getBlocked(id, mySelf, false);
	}

	public List<UserDto> getBlocked(Long id, UserDto mySelf, boolean avoidCheck) {
		if (!this.checkMyself(mySelf, avoidCheck)) {
			return null;
		}
		return userMapper.toDto(userRepository.findByUserIdAndRelation(id, UserRelation.BLOCKED.name()));
	}

	public boolean checkMyself(UserDto mySelf) {
		return this.checkMyself(mySelf, false);
	}

	public boolean checkMyself(UserDto mySelf, boolean avoidCheck) {
		if (mySelf == null || mySelf.getId() == null) {
			return false;
		}
		if (avoidCheck) {
			return true;
		}
		userRepository.flush();
		User old = userRepository.getOne(mySelf.getId());
		String oldPassword = old.getPassword();
		return old != null && oldPassword.equals(mySelf.getPassword());
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

	public UserDto updateLastOnline(UserDto mySelf) {
		return this.updateLastOnline(mySelf, false);
	}

	public UserDto updateLastOnline(UserDto mySelf, boolean avoidCheck) {
		if (!this.checkMyself(mySelf, avoidCheck)) {
			return null;
		}
		User user = userRepository.getOne(mySelf.getId());
		user.setLastOnline(Utils.now());
		return userMapper.toDto(userRepository.save(user));
	}
}
