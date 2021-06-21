package it.objectmethod.tatami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.controller.dto.LobbySearchQueryParams;
import it.objectmethod.tatami.dto.LobbyDto;
import it.objectmethod.tatami.dto.PercentageDto;
import it.objectmethod.tatami.dto.UserUserDto;
import it.objectmethod.tatami.dto.mapper.LobbyMapper;
import it.objectmethod.tatami.dto.mapper.PercentageMapper;
import it.objectmethod.tatami.entity.Lobby;
import it.objectmethod.tatami.entity.Percentage;
import it.objectmethod.tatami.entity.PercentageError;
import it.objectmethod.tatami.entity.PercentageQueryParams;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.enums.LobbyType;
import it.objectmethod.tatami.entity.enums.PercentageLogMessages;
import it.objectmethod.tatami.entity.enums.PercentageOperation;
import it.objectmethod.tatami.entity.enums.PlayerColor;
import it.objectmethod.tatami.entity.enums.UserRelation;
import it.objectmethod.tatami.repository.CustomRepository;
import it.objectmethod.tatami.repository.LobbyRepository;
import it.objectmethod.tatami.repository.UserRepository;
import it.objectmethod.tatami.utils.Utils;

@Service
public class LobbyService {

	@Autowired
	private LobbyMapper lobbyMapper;
	@Autowired
	private PercentageMapper percentageMapper;

	@Autowired
	private LobbyRepository lobbyRepository;
	@Autowired
	private PercentageService percentageService;
	@Autowired
	private PercentageErrorsService percentageErrorsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomRepository customRepository;
	@Autowired
	private UserUserService userRelationService;

	public LobbyDto createLobby(Long loggedUserId, String lobbyName, String lobbyType) {
		if (loggedUserId == null) {
			return null;
		}
		Lobby newLobby = new Lobby();
		newLobby.setUserId1(loggedUserId);
		newLobby.setLastInLobby1(Long.valueOf(Utils.now().getTime()));
		newLobby.setColor1(PlayerColor.chooseRandomExcept());
		newLobby.setClosed(Boolean.FALSE);
		if (!Utils.isBlank(lobbyType)) {
			newLobby.setLobbyType(LobbyType.valueOf(lobbyType));
		}
		newLobby = lobbyRepository.save(newLobby);
		return lobbyMapper.toDto(newLobby);
	}

	public PercentageDto joinLobby(Long lobbyId, Long loggedUserId) {
		if (lobbyId == null || loggedUserId == null) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		if (lobby == null || Boolean.TRUE.equals(lobby.getClosed())) {
			return null;
		}

		PercentageQueryParams params = new PercentageQueryParams();
		params.setIntegerParam1(loggedUserId);
		params.setIntegerParam2(lobbyId);
		Percentage p = percentageService.prepareQueryParams(userRepository.getOne(loggedUserId), params,
			PercentageOperation.JOIN_LOBBY);
		return percentageMapper.toDto(p);
	}

	public Percentage handleJoinLobby(Percentage perc) {
		PercentageQueryParams params = perc.getPercentageQueryParams().get(0);
		Long userJoining = params.getIntegerParam1();
		Lobby lobby = lobbyRepository.getOne(params.getIntegerParam2());

		if (lobby == null) {
			PercentageError error = new PercentageError();
			error.setMessageKey(PercentageLogMessages.NONEXISTEND_LOBBY);
			error.setExtraParam1(String.valueOf(params.getIntegerParam2()));
			error.setPercentage(perc);
			error = percentageErrorsService.save(error);

			perc.setProgression(Double.valueOf(1));
			perc.setPercentageErrors(Utils.asList(error));
			return percentageService.save(perc, true);
		}

		if (Boolean.TRUE.equals(lobby.getClosed())) {
			PercentageError error = new PercentageError();
			error.setMessageKey(PercentageLogMessages.LOBBY_IS_CLOSED);
			error.setExtraParam1(String.valueOf(params.getIntegerParam2()));
			error.setPercentage(perc);
			error = percentageErrorsService.save(error);

			perc.setProgression(Double.valueOf(1));
			perc.setPercentageErrors(Utils.asList(error));
			return percentageService.save(perc, true);
		}

		List<UserUserDto> relations1 = userRelationService.getAllRelationsByUser1And2(lobby.getUserId1(), userJoining);
		List<UserUserDto> relations2 = userRelationService.getAllRelationsByUser1And2(lobby.getUserId2(), userJoining);
		List<UserUserDto> relations3 = userRelationService.getAllRelationsByUser1And2(lobby.getUserId3(), userJoining);
		List<UserUserDto> relations4 = userRelationService.getAllRelationsByUser1And2(lobby.getUserId4(), userJoining);
		if (LobbyType.PRIVATE.equals(lobby.getLobbyType())) {
			boolean friends = false;
			if (relations1 != null) {
				for (UserUserDto rel : relations1) {
					if (UserRelation.FRIEND.equals(rel.getRelationship())) {
						friends = true;
						break;
					}
				}
			}
			if (!friends && relations2 != null) {
				for (UserUserDto rel : relations2) {
					if (UserRelation.FRIEND.equals(rel.getRelationship())) {
						friends = true;
						break;
					}
				}
			}
			if (!friends && relations3 != null) {
				for (UserUserDto rel : relations3) {
					if (UserRelation.FRIEND.equals(rel.getRelationship())) {
						friends = true;
						break;
					}
				}
			}
			if (!friends && relations4 != null) {
				for (UserUserDto rel : relations4) {
					if (UserRelation.FRIEND.equals(rel.getRelationship())) {
						friends = true;
						break;
					}
				}
			}

			if (!friends) {
				PercentageError error = new PercentageError();
				error.setMessageKey(PercentageLogMessages.LOBBY_IS_PRIVATE);
				error.setExtraParam1(String.valueOf(params.getIntegerParam2()));
				error.setPercentage(perc);
				error = percentageErrorsService.save(error);

				perc.setProgression(Double.valueOf(1));
				perc.setPercentageErrors(Utils.asList(error));
				return percentageService.save(perc, true);
			}
		}

		boolean blocked = false;
		if (relations1 != null) {
			for (UserUserDto rel : relations1) {
				if (UserRelation.BLOCKED.equals(rel.getRelationship())) {
					blocked = true;
					break;
				}
			}
		}
		if (!blocked && relations2 != null) {
			for (UserUserDto rel : relations2) {
				if (UserRelation.BLOCKED.equals(rel.getRelationship())) {
					blocked = true;
					break;
				}
			}
		}
		if (!blocked && relations3 != null) {
			for (UserUserDto rel : relations3) {
				if (UserRelation.BLOCKED.equals(rel.getRelationship())) {
					blocked = true;
					break;
				}
			}
		}
		if (!blocked && relations4 != null) {
			for (UserUserDto rel : relations4) {
				if (UserRelation.BLOCKED.equals(rel.getRelationship())) {
					blocked = true;
					break;
				}
			}
		}
		if (blocked) {
			PercentageError error = new PercentageError();
			error.setMessageKey(PercentageLogMessages.UNABLE_TO_JOIN);
			error.setExtraParam1(String.valueOf(params.getIntegerParam2()));
			error.setPercentage(perc);
			error = percentageErrorsService.save(error);

			perc.setProgression(Double.valueOf(1));
			perc.setPercentageErrors(Utils.asList(error));
			return percentageService.save(perc, true);
		}

		User user = userRepository.getOne(userJoining);
		if (!lobby.isFull() && !lobby.isUserAlreadyInside(userJoining)) {
			if (lobby.getUserId1() == null) {
				lobby.setUserId1(user.getId());
				lobby.setLastInLobby1(Utils.now().getTime());
				lobby.setColor1(PlayerColor.chooseRandomExcept(lobby.getColorsInUse()));
				lobbyRepository.save(lobby);
			} else if (lobby.getUserId2() == null) {
				lobby.setUserId2(user.getId());
				lobby.setLastInLobby2(Utils.now().getTime());
				lobby.setColor2(PlayerColor.chooseRandomExcept(lobby.getColorsInUse()));
				lobbyRepository.save(lobby);
			} else if (lobby.getUserId3() == null) {
				lobby.setUserId3(user.getId());
				lobby.setLastInLobby3(Utils.now().getTime());
				lobby.setColor3(PlayerColor.chooseRandomExcept(lobby.getColorsInUse()));
				lobbyRepository.save(lobby);
			} else if (lobby.getUserId4() == null) {
				lobby.setUserId4(user.getId());
				lobby.setLastInLobby4(Utils.now().getTime());
				lobby.setColor4(PlayerColor.chooseRandomExcept(lobby.getColorsInUse()));
				lobbyRepository.save(lobby);
			}
		}

		perc.setProgression(Double.valueOf(1));
		return percentageService.save(perc, false);
	}

	public LobbyDto exitLobby(Long lobbyId, Long loggedUserId) {
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		if (lobby == null || loggedUserId == null) {
			return null;
		}
		if (lobby.isClosed()) {
			return null;
		}
		if (loggedUserId.equals(lobby.getUserId1())) {
			lobby.setLastInLobby1(lobby.getLastInLobby2());
			lobby.setLastInLobby2(lobby.getLastInLobby3());
			lobby.setLastInLobby3(lobby.getLastInLobby4());
			lobby.setLastInLobby4(null);
			lobby.setUserId1(lobby.getUserId2());
			lobby.setUserId2(lobby.getUserId3());
			lobby.setUserId3(lobby.getUserId4());
			lobby.setUserId4(null);
			lobby.setColor1(lobby.getColor2());
			lobby.setColor2(lobby.getColor3());
			lobby.setColor3(lobby.getColor4());
			lobby.setColor4(null);
		} else if (loggedUserId.equals(lobby.getUserId2())) {
			lobby.setLastInLobby2(lobby.getLastInLobby3());
			lobby.setLastInLobby3(lobby.getLastInLobby4());
			lobby.setLastInLobby4(null);
			lobby.setUserId2(lobby.getUserId3());
			lobby.setUserId3(lobby.getUserId4());
			lobby.setUserId4(null);
			lobby.setColor2(lobby.getColor3());
			lobby.setColor3(lobby.getColor4());
			lobby.setColor4(null);
		} else if (loggedUserId.equals(lobby.getUserId3())) {
			lobby.setLastInLobby3(lobby.getLastInLobby4());
			lobby.setLastInLobby4(null);
			lobby.setUserId3(lobby.getUserId4());
			lobby.setUserId4(null);
			lobby.setColor3(lobby.getColor4());
			lobby.setColor4(null);
		} else if (loggedUserId.equals(lobby.getUserId4())) {
			lobby.setLastInLobby4(null);
			lobby.setUserId4(null);
			lobby.setColor4(null);
		}
		return lobbyMapper.toDto(lobbyRepository.save(lobby));
	}

	public LobbyDto getOne(Long id) {
		if (id == null) {
			return null;
		}
		return lobbyMapper.toDto(lobbyRepository.getOne(id));
	}

	public List<LobbyDto> findPaged(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Lobby> lobbies = this.lobbyRepository.findByClosedFalse(pageable.getOffset(), pageable.getPageSize());
		return lobbyMapper.toDto(lobbies);
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

	public LobbyDto updateLastOnline(Long lobbyId, Long loggedUserId) {
		if (loggedUserId == null || lobbyId == null) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		if (loggedUserId.equals(lobby.getUserId1())) {
			lobby.setLastInLobby1(Utils.now().getTime());
		} else if (loggedUserId.equals(lobby.getUserId2())) {
			lobby.setLastInLobby2(Utils.now().getTime());
		} else if (loggedUserId.equals(lobby.getUserId3())) {
			lobby.setLastInLobby3(Utils.now().getTime());
		} else if (loggedUserId.equals(lobby.getUserId4())) {
			lobby.setLastInLobby4(Utils.now().getTime());
		}
		return lobbyMapper.toDto(lobbyRepository.save(lobby));
	}

	public LobbyDto updateLobbyName(Long lobbyId, Long loggedUserId, String name) {
		if (lobbyId == null || loggedUserId == null) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		if (!loggedUserId.equals(lobby.getUserId1())) {
			return null;
		}
		lobby.setLobbyName(name);
		lobby.setLastInLobby1(Utils.now().getTime());
		return lobbyMapper.toDto(lobbyRepository.save(lobby));
	}

	public LobbyDto updateLobbyType(Long lobbyId, Long loggedUserId, LobbyType lobbyType) {
		if (lobbyId == null || loggedUserId == null) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		if (!loggedUserId.equals(lobby.getUserId1())) {
			return null;
		}
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

	public LobbyDto startGame(Long lobbyId, Long loggedUserId) {
		if (lobbyId == null || loggedUserId == null) {
			return null;
		}
		Lobby lobby = lobbyRepository.getOne(lobbyId);
		if (!loggedUserId.equals(lobby.getUserId1())) {
			return null;
		}
		lobby.setClosed(Boolean.TRUE);

		PercentageQueryParams params = new PercentageQueryParams();
		params.setIntegerParam1(lobbyId);
		percentageService.prepareQueryParams(userRepository.getOne(loggedUserId), params,
			PercentageOperation.START_GAME);

		return lobbyMapper.toDto(lobbyRepository.save(lobby));
	}
}
