package it.objectmethod.tatami.scheduled;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.dto.LobbyDto;
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.UserUserDto;
import it.objectmethod.tatami.entity.enums.UserRelation;
import it.objectmethod.tatami.entity.enums.UserStatus;
import it.objectmethod.tatami.service.LobbyService;
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
	@Autowired
	private LobbyService lobbyService;

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

	public void setOffline() {
		long totalUsers = userService.count();
		if (totalUsers == 0) {
			return;
		}
		int page = 0;
		int size = 1000;
		int offset = 0;
		long nowInMillis = System.currentTimeMillis();
		do {
			List<UserDto> usersOfPage = userService.findPaged(page, size);
			for (UserDto user : usersOfPage) {
				if (UserStatus.OFFLINE.equals(user.getUserStatus()) || user.getLastOnline() == null) {
					continue;
				}
				long lastOnlineInMillis = user.getLastOnline().getTime();
				if (nowInMillis - lastOnlineInMillis > 300000) { // 5 min
					user.setUserStatus(UserStatus.OFFLINE);
					userService.update(user);
				} else if (!UserStatus.NOT_RESPONDING.equals(user.getUserStatus())
					&& nowInMillis - lastOnlineInMillis > 60000) { // 1 min
					user.setUserStatus(UserStatus.NOT_RESPONDING);
					userService.update(user);
				}
			}
			page++;
			offset = page * size;
		} while (offset < totalUsers);
	}

	public void removeFromLobby() {
		long totalUsers = lobbyService.count();
		if (totalUsers == 0) {
			return;
		}
		int page = 0;
		int size = 1000;
		int offset = 0;
		long nowInMillis = System.currentTimeMillis();
		do {
			List<LobbyDto> lobbiesOfPage = lobbyService.findPaged(page, size);
			for (LobbyDto lobby : lobbiesOfPage) {
				if (lobby.getLastInLobby4() != null && nowInMillis - lobby.getLastInLobby4().longValue() > 10000) {
					lobby = this.lobbyService.exitLobby(userService.forceGetOne(lobby.getUserId1()), lobby.getId());
				}
				if (lobby.getLastInLobby3() != null && nowInMillis - lobby.getLastInLobby3().longValue() > 10000) {
					lobby = this.lobbyService.exitLobby(userService.forceGetOne(lobby.getUserId1()), lobby.getId());
				}
				if (lobby.getLastInLobby2() != null && nowInMillis - lobby.getLastInLobby2().longValue() > 10000) {
					lobby = this.lobbyService.exitLobby(userService.forceGetOne(lobby.getUserId1()), lobby.getId());
				}
				if (lobby.getLastInLobby1() != null && nowInMillis - lobby.getLastInLobby1().longValue() > 10000) {
					lobby = this.lobbyService.exitLobby(userService.forceGetOne(lobby.getUserId1()), lobby.getId());
				}
				if (lobby.isEmpty()) {
					this.lobbyService.delete(lobby.getId());
				}
			}
			page++;
			offset = page * size;
		} while (offset < totalUsers);
	}
}
