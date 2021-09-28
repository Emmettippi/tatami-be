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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.objectmethod.tatami.controller.dto.LoginDto;
import it.objectmethod.tatami.controller.dto.UserSearchQueryParams;
import it.objectmethod.tatami.dto.MyRelationsDto;
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.dto.UserEditDto;
import it.objectmethod.tatami.dto.UserSearchResponseDto;
import it.objectmethod.tatami.service.JWTService;
import it.objectmethod.tatami.service.UserService;
import it.objectmethod.tatami.utils.Utils;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JWTService jwtService;

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
	public ResponseEntity<UserDto> update(@Validated @RequestBody UserEditDto body,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		ResponseEntity<UserDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		if (loggedUserId != null && body != null && loggedUserId.equals(body.getId())) {
			resp = new ResponseEntity<>(userService.update(body, true), HttpStatus.OK);
		} else {
			resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return resp;
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getOne(@PathVariable Long id,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		return new ResponseEntity<>(userService.getOne(id, loggedUserId), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id, @RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		ResponseEntity<UserDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		if (loggedUserId != null && loggedUserId.equals(id)) {
			userService.delete(id);
			resp = new ResponseEntity<>(null, HttpStatus.OK);
		} else {
			resp = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		return resp;
	}

	@GetMapping("/search")
	public List<UserSearchResponseDto> search(@Validated UserSearchQueryParams params) {
		return userService.search(params);
	}

	@PostMapping("/update-last-online/{id}")
	public ResponseEntity<UserDto> updateLastOnline(@PathVariable Long id,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		ResponseEntity<UserDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		if (loggedUserId != null && loggedUserId.equals(id)) {
			resp = new ResponseEntity<>(userService.updateLastOnline(id), HttpStatus.OK);
		} else {
			resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return resp;
	}

	@GetMapping("/my-relations/{id}")
	public ResponseEntity<MyRelationsDto> search(@PathVariable Long id,
		@RequestHeader(Utils.TATAMI_AUTH_TOKEN) String authToken) {
		ResponseEntity<MyRelationsDto> resp;
		Long loggedUserId = jwtService.getUserIdByToken(authToken);
		if (loggedUserId != null && loggedUserId.equals(id)) {
			MyRelationsDto relations = new MyRelationsDto();
			relations.setFriends(userService.getFriends(id));
			relations.setAskingFriends(userService.getAskingFriends(id));
			relations.setPendingFriends(userService.getPendingFriendRequests(id));
			relations.setBlocked(userService.getBlocked(id));
			resp = new ResponseEntity<>(relations, HttpStatus.OK);
		} else {
			resp = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		return resp;
	}
}
