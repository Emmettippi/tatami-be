package it.objectmethod.tatami.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.objectmethod.tatami.controller.dto.LobbySearchQueryParams;
import it.objectmethod.tatami.dto.LobbyDto;
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.entity.enums.LobbyType;
import it.objectmethod.tatami.service.LobbyService;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

	@Autowired
	private LobbyService lobbyService;

	@PostMapping("/join/{id}")
	public boolean joinLobby(@Validated @RequestBody UserDto mySelf, @PathVariable("id") Long lobbyId) {
		return lobbyService.joinLobby(mySelf, lobbyId);
	}

	@PostMapping("/set-visibility/{id}/{visibility}")
	public LobbyDto setLobbyVisibility(@Validated @RequestBody UserDto mySelf, @PathVariable("id") Long lobbyId,
		@PathVariable("visibility") String visibility) {
		return this.lobbyService.updateLobbyType(mySelf, lobbyId, LobbyType.valueOf(visibility));
	}

	@PostMapping("/set-name/{id}/{lobbyName}")
	public LobbyDto setLobbyName(@Validated @RequestBody UserDto mySelf, @PathVariable("id") Long lobbyId,
		@PathVariable("lobbyName") String lobbyName) {
		return this.lobbyService.updateLobbyName(mySelf, lobbyId, lobbyName);
	}

	@PostMapping("/exit/{id}")
	public void exitLobby(@Validated @RequestBody UserDto mySelf, @PathVariable("id") Long lobbyId) {
		this.lobbyService.exitLobby(mySelf, lobbyId);
	}

	@PostMapping("/update-last-online/{id}")
	public LobbyDto updateLastOnline(@Validated @RequestBody UserDto mySelf, @PathVariable("id") Long lobbyId) {
		return this.lobbyService.updateLastOnline(mySelf, lobbyId);
	}

	@GetMapping("/{id}")
	public LobbyDto getOne(@PathVariable("id") Long lobbyId) {
		return this.lobbyService.getOne(lobbyId);
	}

	@GetMapping("/search")
	public List<LobbyDto> searchLobby(LobbySearchQueryParams lobbyParams) {
		return this.lobbyService.searchPaged(lobbyParams);
	}

	@PostMapping("/start-game/{id}")
	public void startGame(@Validated @RequestBody UserDto mySelf) {

	}
}
