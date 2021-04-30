package it.objectmethod.tatami.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.UserUserDto;
import it.objectmethod.tatami.dto.mapper.UserMapper;
import it.objectmethod.tatami.dto.mapper.UserUserMapper;
import it.objectmethod.tatami.entity.Percentage;
import it.objectmethod.tatami.entity.PercentageQueryParams;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.UserUser;
import it.objectmethod.tatami.entity.enums.PercentageOperation;
import it.objectmethod.tatami.entity.enums.UserRelation;
import it.objectmethod.tatami.repository.UserRepository;
import it.objectmethod.tatami.repository.UserUserRepository;

@Service
public class UserUserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserUserRepository userUserRepository;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserUserMapper userUserMapper;
	@Autowired
	private PercentageService percentageService;
	@Autowired
	private UserService userService;

	public boolean askFriendship(Long askingUserId, UserDto mySelf) {
		if (!this.userService.checkMyself(mySelf)) {
			return false;
		}
		boolean blocked = false;
		List<User> blockers = userRepository.findByUserId2AndRelation(mySelf.getId(), UserRelation.BLOCKED.name());
		for (User u : blockers) {
			if (u.getId().equals(askingUserId)) {
				blocked = true;
				break;
			}
		}
		if (blocked) {
			return false;
		}

		PercentageQueryParams params = new PercentageQueryParams();
		params.setIntegerParam1(mySelf.getId());
		params.setIntegerParam2(askingUserId);
		percentageService.prepareQueryParams(userMapper.toEntity(mySelf), params, PercentageOperation.ASK_FRIENDSHIP);
		return true;
	}

	public void acceptFriendship(Long id, UserDto mySelf) {
		if (!this.userService.checkMyself(mySelf)) {
			return;
		}
		UserUser pendingRelation = userUserRepository.getOne(id);
		UserUser askingRelation = userUserRepository.findByUser1_IdAndUser2_IdAndRelationship(
			pendingRelation.getUser2().getId(), pendingRelation.getUser1().getId(), UserRelation.ASKING_FRIENDSHIP);
		pendingRelation.setRelationship(UserRelation.FRIEND);
		askingRelation.setRelationship(UserRelation.FRIEND);
		userUserRepository.save(pendingRelation);
		userUserRepository.save(askingRelation);
	}

	public void removeFriendship(Long relationId, UserDto mySelf) {
		this.removeFriendship(relationId, mySelf, false);
	}
	public void removeFriendship(Long relationId, UserDto mySelf, boolean avoidCheck) {
		if (!this.userService.checkMyself(mySelf, avoidCheck)) {
			return;
		}
		UserUser relation = userUserRepository.getOne(relationId);
		UserUser friendRelation = userUserRepository.findByUser1_IdAndUser2_IdAndRelationship(
			relation.getUser2().getId(), relation.getUser1().getId(), relation.getRelationship());
		this.delete(relation.getId());
		this.delete(friendRelation.getId());
	}

	public void blockUser(Long userId, UserDto mySelf) {
		if (!this.userService.checkMyself(mySelf)) {
			return;
		}
		UserUser friendRelation = userUserRepository.findByUser1_IdAndUser2_IdAndRelationship(mySelf.getId(), userId,
			UserRelation.FRIEND);
		if (friendRelation != null) {
			this.removeFriendship(friendRelation.getId(), mySelf);
		}
		UserUser blockRelation = new UserUser();
		blockRelation.setRelationship(UserRelation.BLOCKED);
		blockRelation.setUser1(userRepository.getOne(mySelf.getId()));
		blockRelation.setUser2(userRepository.getOne(userId));
		userUserRepository.save(blockRelation);
	}

	public void unlockUser(Long relationId, UserDto mySelf) {
		if (!this.userService.checkMyself(mySelf)) {
			return;
		}
		this.delete(relationId);
	}

	public Percentage handleFriendship(Percentage perc) {
		PercentageQueryParams params = perc.getPercentageQueryParams().get(0);
		List<UserUserDto> relations = new ArrayList<>();

		UserUser blockedRelation = userUserRepository.findByUser1_IdAndUser2_IdAndRelationship(
			params.getIntegerParam2(), params.getIntegerParam1(), UserRelation.BLOCKED);
		if (blockedRelation != null) {
			perc.setProgression(Double.valueOf(1));
			return percentageService.save(perc, false);
		}

		UserUser relation = userUserRepository.findByUser1_IdAndUser2_Id(params.getIntegerParam1(),
			params.getIntegerParam2()).stream().findFirst().orElse(null);
		if (relation == null) {
			UserUser request = new UserUser();
			request.setUser1(userRepository.getOne(params.getIntegerParam1()));
			request.setUser2(userRepository.getOne(params.getIntegerParam2()));
			request.setRelationship(UserRelation.ASKING_FRIENDSHIP);
			UserUser response = new UserUser();
			response.setUser1(request.getUser2());
			response.setUser2(request.getUser1());
			response.setRelationship(UserRelation.PENDING_FRIENDSHIP);
			relations.add(userUserMapper.toDto(userUserRepository.save(request)));
			relations.add(userUserMapper.toDto(userUserRepository.save(response)));
		} else if (UserRelation.PENDING_FRIENDSHIP.equals(relation.getRelationship())) {
			UserUser askingRelation = userUserRepository.findByUser1_IdAndUser2_Id(params.getIntegerParam2(),
				params.getIntegerParam1()).get(0);
			relation.setRelationship(UserRelation.FRIEND);
			askingRelation.setRelationship(UserRelation.FRIEND);
			relations.add(userUserMapper.toDto(userUserRepository.save(relation)));
			relations.add(userUserMapper.toDto(userUserRepository.save(askingRelation)));
		}
		perc.setProgression(Double.valueOf(1));
		return percentageService.save(perc, false);
	}

	public List<UserUserDto> getAllRelationsByUser1(Long user1Id) {
		if (user1Id == null) {
			return null;
		}
		List<UserUser> relations = userUserRepository.findByUser1_Id(user1Id);
		return userUserMapper.toDto(relations);
	}

	public List<UserUserDto> getAllRelationsByUser1And2(Long user1Id, Long user2Id) {
		if (user1Id == null || user2Id == null) {
			return null;
		}
		List<UserUser> relations = userUserRepository.findByUser1_IdAndUser2_Id(user1Id, user2Id);
		return userUserMapper.toDto(relations);
	}

	public void onUserDelete(Long userId) {
		if (userId == null) {
			return;
		}
		userUserRepository.deleteByUserId(userId);
	}

	public void delete(Long id) {
		if (id == null) {
			return;
		}
		userUserRepository.deleteById(id);
	}
}
