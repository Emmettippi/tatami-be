package it.objectmethod.tatami.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.objectmethod.tatami.controller.dto.LobbyQueryParams;
import it.objectmethod.tatami.controller.dto.LobbySearchQueryParams;
import it.objectmethod.tatami.dto.LobbyDto;
import it.objectmethod.tatami.dto.PercentageDto;
import it.objectmethod.tatami.entity.enums.LobbyType;
import it.objectmethod.tatami.service.JWTService;
import it.objectmethod.tatami.service.LobbyService;
import it.objectmethod.tatami.utils.Utils;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

	@Autowired
	private JWTService jwtService;
	@Autowired
	private LobbyService lobbyService;

	@PostMapping("/create")
	public ResponseEntity<LobbyDto> createLobby(@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken,
		@RequestBody LobbyQueryParams lobbyParams) {
		ResponseEntity<LobbyDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		if (lobbyParams == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		LobbyDto dto = this.lobbyService.createLobby(loggedUserId, lobbyParams.getName(), lobbyParams.getLobbyType());
		if (dto == null) {
			resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			resp = new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return resp;
	}

	@PostMapping("/join/{id}")
	public ResponseEntity<PercentageDto> joinLobby(@PathVariable("id") Long lobbyId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		PercentageDto resp = lobbyService.joinLobby(lobbyId, loggedUserId);
		return new ResponseEntity<>(resp, resp != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/set-visibility/{id}/{visibility}")
	public ResponseEntity<LobbyDto> setLobbyVisibility(@PathVariable("id") Long lobbyId,
		@PathVariable("visibility") String visibility, @RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		ResponseEntity<LobbyDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		LobbyDto dto = this.lobbyService.updateLobbyType(lobbyId, loggedUserId, LobbyType.valueOf(visibility));
		if (dto == null) {
			resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			resp = new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return resp;
	}

	@PostMapping("/set-name/{id}/{lobbyName}")
	public ResponseEntity<LobbyDto> setLobbyName(@PathVariable("id") Long lobbyId,
		@PathVariable("lobbyName") String lobbyName,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		ResponseEntity<LobbyDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		LobbyDto dto = this.lobbyService.updateLobbyName(lobbyId, loggedUserId, lobbyName);
		if (dto == null) {
			resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			resp = new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return resp;
	}

	@PostMapping("/exit/{id}")
	public ResponseEntity<?> exitLobby(@PathVariable("id") Long lobbyId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		this.lobbyService.exitLobby(lobbyId, loggedUserId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/update-last-online/{id}")
	public ResponseEntity<LobbyDto> updateLastOnline(@PathVariable("id") Long lobbyId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		ResponseEntity<LobbyDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		LobbyDto dto = this.lobbyService.updateLastOnline(lobbyId, loggedUserId);
		if (dto == null) {
			resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			resp = new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return resp;
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
	public ResponseEntity<LobbyDto> startGame(@PathVariable("id") Long lobbyId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		ResponseEntity<LobbyDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		LobbyDto dto = this.lobbyService.startGame(lobbyId, loggedUserId);
		if (dto == null) {
			resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			resp = new ResponseEntity<>(dto, HttpStatus.OK);
		}
		return resp;
	}
}
