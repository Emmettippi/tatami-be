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
import it.objectmethod.tatami.service.PercentageService;
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
	@Autowired
	private PercentageService percentageService;

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
			List<UserDto> usersOfPage = userService.findByStatusNotOffline(page, size);
			for (UserDto user : usersOfPage) {
				if (user.getLastOnline() == null) {
					user.setUserStatus(UserStatus.OFFLINE);
					userService.update(user, false);
					continue;
				}
				long lastOnlineInMillis = user.getLastOnline().getTime();
				if (nowInMillis - lastOnlineInMillis > 300000) { // 5 min
					user.setUserStatus(UserStatus.OFFLINE);
					userService.update(user, false);
					log.info("!!! --- User " + user.getId() + " set to OFFLINE due to inactivity over 5min --- !!!");
				} else if (!UserStatus.NOT_RESPONDING.equals(user.getUserStatus())
					&& nowInMillis - lastOnlineInMillis > 60000) { // 1 min
					user.setUserStatus(UserStatus.NOT_RESPONDING);
					userService.update(user, false);
					log.info(
						"!!! --- User " + user.getId() + " set to NOT_RESPONDING due to inactivity over 1min --- !!!");
				}
			}
			page++;
			offset = page * size;
		} while (offset < totalUsers);
	}

	public void removeFromLobby() {
		long totalLobbies = lobbyService.count();
		if (totalLobbies == 0) {
			return;
		}
		int page = 0;
		int size = 1000;
		int offset = 0;
		long nowInMillis = System.currentTimeMillis();
		do {
			List<LobbyDto> lobbiesOfPage = lobbyService.findPaged(page, size);
			for (LobbyDto lobby : lobbiesOfPage) {
				if (lobby.getLastInLobby4() != null && nowInMillis - lobby.getLastInLobby4().longValue() > 10000) { // 10 s
					lobby = this.lobbyService.exitLobby(lobby.getId(), lobby.getUserId4());
					log.info("!!! --- User " + lobby.getUserId4() + " removed from lobby " + lobby.getId()
						+ " due to inactivity over 10s --- !!!");
				}
				if (lobby.getLastInLobby3() != null && nowInMillis - lobby.getLastInLobby3().longValue() > 10000) {
					lobby = this.lobbyService.exitLobby(lobby.getId(), lobby.getUserId3());
					log.info("!!! --- User " + lobby.getUserId3() + " removed from lobby " + lobby.getId()
						+ " due to inactivity over 10s --- !!!");
				}
				if (lobby.getLastInLobby2() != null && nowInMillis - lobby.getLastInLobby2().longValue() > 10000) {
					lobby = this.lobbyService.exitLobby(lobby.getId(), lobby.getUserId2());
					log.info("!!! --- User " + lobby.getUserId2() + " removed from lobby " + lobby.getId()
						+ " due to inactivity over 10s --- !!!");
				}
				if (lobby.getLastInLobby1() != null && nowInMillis - lobby.getLastInLobby1().longValue() > 10000) {
					lobby = this.lobbyService.exitLobby(lobby.getId(), lobby.getUserId1());
					log.info("!!! --- User " + lobby.getUserId1() + " removed from lobby " + lobby.getId()
						+ " due to inactivity over 10s --- !!!");
				}
				if (lobby.isEmpty()) {
					this.lobbyService.delete(lobby.getId());
					log.info("!!! --- Lobby " + lobby.getId() + "deleted because empty --- !!!");
				}
			}
			page++;
			offset = page * size;
		} while (offset < totalLobbies);
	}

	public void emptyPercentage() {
		this.percentageService.emptyPercentage();
	}
}
