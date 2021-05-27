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

	@PostMapping("/cancel-friendship/{relationId}")
	public ResponseEntity<Boolean> cancelFriendshipRequest(@PathVariable("relationId") Long askingRelationId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.cancelFriendshipRequest(askingRelationId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/accept-friendship/{relationId}")
	public ResponseEntity<Boolean> acceptFriendship(@PathVariable("relationId") Long relationId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.acceptFriendship(relationId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/refuse-friendship/{relationId}")
	public ResponseEntity<Boolean> refuseFriendship(@PathVariable("relationId") Long relationId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.refuseFriendship(relationId, loggedUserId);
		return new ResponseEntity<>(Boolean.valueOf(result), result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/remove-friendship/{relationId}")
	public ResponseEntity<Boolean> removeFriendship(@PathVariable("relationId") Long relationId,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		boolean result = userUserService.removeFriendship(relationId, loggedUserId);
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
