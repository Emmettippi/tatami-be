package it.objectmethod.tatami.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.objectmethod.tatami.controller.dto.LoginDto;
import it.objectmethod.tatami.controller.dto.UserSearchQueryParams;
import it.objectmethod.tatami.dto.MyRelationsDto;
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.UserSearchResponseDto;
import it.objectmethod.tatami.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@Validated @RequestBody LoginDto body) {
		UserDto userLogged = userService.login(body);
		return new ResponseEntity<>(userLogged, userLogged == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<UserDto> create(@Validated @RequestBody UserDto body) {
		UserDto userCreated = userService.create(body);
		return new ResponseEntity<>(userCreated, userCreated == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
	}

	@PutMapping("/update")
	public UserDto update(@Validated @RequestBody UserDto body) {
		return userService.update(body);
	}

	@PostMapping("/{id}")
	public UserDto getOne(@PathVariable Long id, @RequestBody UserDto body) {
		return userService.getOne(id, body);
	}

	@DeleteMapping("/delete")
	public void delete(@Validated @RequestBody UserDto body) {
		userService.delete(body);
	}

	@GetMapping("/search")
	public List<UserSearchResponseDto> search(@Validated UserSearchQueryParams params) {
		return userService.search(params);
	}

	@PostMapping("/update-last-online")
	public UserDto updateLastOnline(@Validated @RequestBody UserDto mySelf) {
		return userService.updateLastOnline(mySelf);
	}

	@PostMapping("/my-relations")
	public MyRelationsDto search(@RequestBody UserDto mySelf) {
		MyRelationsDto relations = new MyRelationsDto();
		relations.setFriends(userService.getFriends(mySelf.getId(), mySelf));
		relations.setAskingFriends(userService.getAskingFriends(mySelf.getId(), mySelf));
		relations.setPendingFriends(userService.getPendingFriendRequests(mySelf.getId(), mySelf));
		relations.setBlocked(userService.getBlocked(mySelf.getId(), mySelf));
		return relations;
	}
}
