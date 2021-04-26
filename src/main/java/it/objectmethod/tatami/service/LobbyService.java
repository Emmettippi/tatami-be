package it.objectmethod.tatami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.controller.dto.LobbySearchQueryParams;
import it.objectmethod.tatami.dto.LobbyDto;
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.mapper.LobbyMapper;
import it.objectmethod.tatami.dto.mapper.UserMapper;
import it.objectmethod.tatami.entity.Lobby;
import it.objectmethod.tatami.entity.Percentage;
import it.objectmethod.tatami.entity.PercentageQueryParams;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.enums.LobbyType;
import it.objectmethod.tatami.entity.enums.PercentageOperation;
import it.objectmethod.tatami.repository.CustomRepository;
import it.objectmethod.tatami.repository.LobbyRepository;
import it.objectmethod.tatami.repository.UserRepository;
import it.objectmethod.tatami.utils.Utils;

@Service
public class LobbyService {

	@Autowired
	private LobbyMapper lobbyMapper;
	@Autowired
	private LobbyRepository lobbyRepository;
	@Autowired
	private PercentageService percentageService;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomRepository customRepository;

	public boolean joinLobby(UserDto mySelf, Long lobbyId) {
		if (!this.userService.checkMyself(mySelf) || lobbyId == null) {
			return false;
		}
		PercentageQueryParams params = new PercentageQueryParams();
		params.setIntegerParam1(mySelf.getId());
		params.setIntegerParam2(lobbyId);
		percentageService.prepareQueryParams(userMapper.toEntity(mySelf), params, PercentageOperation.JOIN_LOBBY);
		return true;
	}

	public Percentage handleJoinLobby(Percentage perc) {
		PercentageQueryParams params = perc.getPercentageQueryParams().get(0);
		Lobby lobby = lobbyRepository.getOne(params.getIntegerParam2());
		User user = userRepository.getOne(params.getIntegerParam1());
		if (!lobby.isFull()) {
			if (lobby.getUserId1() == null) {
				lobby.setUserId1(user.getId());
				lobby.setLastInLobby1(Utils.now().getTime());
				lobbyRepository.save(lobby);
			} else if (lobby.getUserId2() == null) {
				lobby.setUserId2(user.getId());
				lobby.setLastInLobby2(Utils.now().getTime());
				lobbyRepository.save(lobby);
			} else if (lobby.getUserId3() == null) {
				lobby.setUserId3(user.getId());
				lobby.setLastInLobby3(Utils.now().getTime());
				lobbyRepository.save(lobby);
			} else if (lobby.getUserId4() == null) {
				lobby.setUserId4(user.getId());
				lobby.setLastInLobby4(Utils.now().getTime());
				lobbyRepository.save(lobby);
			}
		}

		perc.setProgression(Double.valueOf(1));
		return percentageService.save(perc, false);
	}

	public LobbyDto exitLobby(UserDto mySelf, Long lobbyId) {
		if (!this.userService.checkMyself(mySelf) || lobbyId == null) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		if (lobby == null) {
			return null;
		}
		if (mySelf.getId().equals(lobby.getUserId1())) {
			lobby.setLastInLobby1(lobby.getLastInLobby2());
			lobby.setLastInLobby2(lobby.getLastInLobby3());
			lobby.setLastInLobby3(lobby.getLastInLobby4());
			lobby.setLastInLobby4(null);
			lobby.setUserId1(lobby.getUserId2());
			lobby.setUserId2(lobby.getUserId3());
			lobby.setUserId3(lobby.getUserId4());
			lobby.setUserId4(null);
			lobby = lobbyRepository.save(lobby);
		} else if (mySelf.getId().equals(lobby.getUserId2())) {
			lobby.setLastInLobby2(lobby.getLastInLobby3());
			lobby.setLastInLobby3(lobby.getLastInLobby4());
			lobby.setLastInLobby4(null);
			lobby.setUserId2(lobby.getUserId3());
			lobby.setUserId3(lobby.getUserId4());
			lobby.setUserId4(null);
			lobby = lobbyRepository.save(lobby);
		} else if (mySelf.getId().equals(lobby.getUserId3())) {
			lobby.setLastInLobby3(lobby.getLastInLobby4());
			lobby.setLastInLobby4(null);
			lobby.setUserId3(lobby.getUserId4());
			lobby.setUserId4(null);
			lobby = lobbyRepository.save(lobby);
		} else if (mySelf.getId().equals(lobby.getUserId4())) {
			lobby.setLastInLobby4(null);
			lobby.setUserId4(null);
			lobby = lobbyRepository.save(lobby);
		}
		return lobbyMapper.toDto(lobby);
	}

	public LobbyDto getOne(Long id) {
		if (id == null) {
			return null;
		}
		return lobbyMapper.toDto(lobbyRepository.getOne(id));
	}

	public List<LobbyDto> findPaged(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Lobby> lobbies = this.lobbyRepository.findAll(pageable);
		return lobbyMapper.toDto(lobbies.getContent());
	}

	public long count() {
		return lobbyRepository.count();
	}

	public void delete(Long id) {
		if (id == null) {
			return;
		}
		this.lobbyRepository.deleteById(id);
	}

	public LobbyDto updateLastOnline(UserDto mySelf, Long lobbyId) {
		if (!this.userService.checkMyself(mySelf)) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		if (mySelf.getId().equals(lobby.getUserId1())) {
			lobby.setLastInLobby1(Utils.now().getTime());
		} else if (mySelf.getId().equals(lobby.getUserId2())) {
			lobby.setLastInLobby2(Utils.now().getTime());
		} else if (mySelf.getId().equals(lobby.getUserId3())) {
			lobby.setLastInLobby3(Utils.now().getTime());
		} else if (mySelf.getId().equals(lobby.getUserId4())) {
			lobby.setLastInLobby4(Utils.now().getTime());
		}
		return lobbyMapper.toDto(lobbyRepository.save(lobby));
	}

	public LobbyDto updateLobbyName(UserDto mySelf, Long lobbyId, String name) {
		if (!this.userService.checkMyself(mySelf)) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		lobby.setLobbyName(name);
		lobby.setLastInLobby1(Utils.now().getTime());
		return lobbyMapper.toDto(lobbyRepository.save(lobby));
	}

	public LobbyDto updateLobbyType(UserDto mySelf, Long lobbyId, LobbyType lobbyType) {
		if (!this.userService.checkMyself(mySelf)) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		lobby.setLobbyType(lobbyType);
		lobby.setLastInLobby1(Utils.now().getTime());
		return lobbyMapper.toDto(lobbyRepository.save(lobby));
	}

	public List<LobbyDto> searchPaged(LobbySearchQueryParams params) {
		if (params == null || params.getUserId() == null || params.getSize() == null || params.getPage() == null) {
			return null;
		}
		return lobbyMapper.toDto(customRepository.searchLobbiesPaged(params));
	}
}
