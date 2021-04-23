package it.objectmethod.tatami.scheduled;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.UserUserDto;
import it.objectmethod.tatami.entity.enums.UserRelation;
import it.objectmethod.tatami.service.UserService;
import it.objectmethod.tatami.service.UserUserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduledBonification {

	@Autowired
	private UserService userService;
	@Autowired
	private UserUserService userUserService;

	public void userRelationBonification() {
		long totalUsers = userService.count();
		if (totalUsers == 0) {
			return;
		}
		int page = 0;
		int size = 1000;
		int offset = 0;
		do {
			List<UserDto> usersOfPage = userService.findPaged(page, size);
			for (UserDto user : usersOfPage) {
				List<UserUserDto> relations = userUserService.getAllRelationsByUser1(user.getId());
				Set<Long> usersRelated = relations.stream().map(UserUserDto::getUser2Id).collect(Collectors.toSet());
				for (Long userRelated : usersRelated) {
					List<UserUserDto> relationsForUser = relations.stream()
						.filter(r -> r.getUser2Id().equals(userRelated)).collect(Collectors.toList());
					List<UserUserDto> counterRelations = userUserService.getAllRelationsByUser1And2(userRelated,
						user.getId());

					UserUserDto blockedRelation = relationsForUser.stream()
						.filter(r -> UserRelation.BLOCKED.equals(r.getRelationship())).findFirst().orElse(null);
					if (blockedRelation != null && !counterRelations.isEmpty()) {
						relationsForUser.stream().filter(r -> !UserRelation.BLOCKED.equals(r.getRelationship()))
							.forEach(r -> userUserService.delete(r.getId()));
						counterRelations.stream().filter(r -> !UserRelation.BLOCKED.equals(r.getRelationship()))
							.forEach(r -> userUserService.delete(r.getId()));
						log.info(
							"!!! --- Bonifyed user " + user.getId() + " with " + userRelated + " for BLOCKED --- !!!");
					} else if (relationsForUser.size() > 1) {
						UserUserDto counterFriendToKeep = new UserUserDto();
						UserUserDto friendToKeep = relationsForUser.stream()
							.filter(r -> UserRelation.FRIEND.equals(r.getRelationship())).findFirst()
							.orElse(new UserUserDto());
						if (friendToKeep.getId() != null) {
							counterFriendToKeep = counterRelations.stream()
								.filter(r -> UserRelation.FRIEND.equals(r.getRelationship())).findFirst()
								.orElse(new UserUserDto());
							if (counterFriendToKeep.getId() == null) {
								friendToKeep = new UserUserDto();
							}
						}
						UserUserDto _friendToKeep = friendToKeep;
						UserUserDto _counterFriendToKeep = counterFriendToKeep;
						relationsForUser.stream().filter(r -> !r.getId().equals(_friendToKeep.getId()))
							.forEach(r -> userUserService.delete(r.getId()));
						counterRelations.stream().filter(r -> !r.getId().equals(_counterFriendToKeep.getId()))
							.forEach(r -> userUserService.delete(r.getId()));
						log.info(
							"!!! --- Bonifyed user " + user.getId() + " with " + userRelated + " for FRIEND --- !!!");
					}
				}
			}
			page++;
			offset = page * size;
		} while (offset < totalUsers);
	}
}
