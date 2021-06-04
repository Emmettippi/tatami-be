package it.objectmethod.tatami.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.objectmethod.tatami.service.JWTService;
import it.objectmethod.tatami.service.UserUserService;
import it.objectmethod.tatami.utils.Utils;

@RestController
@RequestMapping("/api/relation")
public class RelationController {

	@Autowired
	private JWTService jwtService;
	@Autowired
	private UserUserService userUserService;

	@PostMapping("/ask-friendship/{userId}")
	public ResponseEntity<Boolean> askFriendship(@PathVariable("userId") Long askingUserId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.askFriendship(askingUserId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/cancel-friendship/{userId}")
	public ResponseEntity<Boolean> cancelFriendshipRequest(@PathVariable("userId") Long userId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.cancelFriendshipRequest(userId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/accept-friendship/{userId}")
	public ResponseEntity<Boolean> acceptFriendship(@PathVariable("userId") Long userId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.acceptFriendship(userId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/refuse-friendship/{userId}")
	public ResponseEntity<Boolean> refuseFriendship(@PathVariable("userId") Long userId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.refuseFriendship(userId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/remove-friendship/{userId}")
	public ResponseEntity<Boolean> removeFriendship(@PathVariable("userId") Long userId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.removeFriendship(userId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/block-user/{userId}")
	public ResponseEntity<Boolean> block(@PathVariable("userId") Long userId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.blockUser(userId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/unlock-user/{relationId}")
	public ResponseEntity<Boolean> unlock(@PathVariable("relationId") Long relationId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.unlockUser(relationId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}
