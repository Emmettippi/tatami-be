package it.objectmethod.tatami.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.service.UserUserService;

@RestController
@RequestMapping("/api/relation")
public class RelationController {

	@Autowired
	private UserUserService userUserService;

	@PostMapping("/ask-friendship/{userId}")
	public boolean askFriendship(@RequestBody UserDto mySelf, @PathVariable("userId") Long askingUserId) {
		return userUserService.askFriendship(askingUserId, mySelf);
	}

	@PostMapping("/accept-friendship/{userId}")
	public boolean acceptFriendship(@RequestBody UserDto mySelf, @PathVariable("userId") Long acceptingUserId) {
		userUserService.acceptFriendship(acceptingUserId, mySelf);
		return true;
	}

	@PostMapping("/remove-friendship/{relationId}")
	public boolean removeFriendship(@RequestBody UserDto mySelf, @PathVariable("relationId") Long relationId) {
		userUserService.removeFriendship(relationId, mySelf);
		return true;
	}

	@PostMapping("/block-user/{userId}")
	public boolean block(@RequestBody UserDto mySelf, @PathVariable("userId") Long userId) {
		userUserService.blockUser(userId, mySelf);
		return true;
	}

	@PostMapping("/unlock-user/{relationId}")
	public boolean unlock(@RequestBody UserDto mySelf, @PathVariable("relationId") Long relationId) {
		userUserService.unlockUser(relationId, mySelf);
		return true;
	}
}
