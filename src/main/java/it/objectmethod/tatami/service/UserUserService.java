package it.objectmethod.tatami.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.UserUserDto;
import it.objectmethod.tatami.dto.mapper.UserMapper;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.repository.UserRepository;
import it.objectmethod.tatami.repository.UserUserRepository;

@Service
public class UserUserService {

	@Autowired
	private UserUserRepository userUserRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	public UserUserDto askFriendShip(UserDto mySelf, Long askingUserId) {
		if (!this.checkMyself(mySelf)) {
			return null;
		}

		return null;
	}

	private boolean checkMyself(UserDto mySelf) {
		if (mySelf == null || mySelf.getId() == null) {
			return false;
		}
		User old = userRepository.getOne(mySelf.getId());
		return old != null && old.getPassword().equals(mySelf.getPassword());
	}
}
