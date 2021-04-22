package it.objectmethod.tatami.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import it.objectmethod.tatami.dto.UserDto;
import it.objectmethod.tatami.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public UserDto login(@Validated @RequestBody LoginDto body) {
		return userService.login(body);
	}

	@PostMapping("/create")
	public UserDto create(@Validated @RequestBody UserDto body) {
		return userService.create(body);
	}

	@PutMapping("/update")
	public UserDto update(@Validated @RequestBody UserDto body) {
		return userService.update(body);
	}

	@PutMapping("/{id}")
	public UserDto getOne(@PathVariable Long id) {
		return userService.getOne(id);
	}

	@DeleteMapping("/delete")
	public void delete(@Validated @RequestBody UserDto body) {
		userService.delete(body);
	}

	@GetMapping("/search/{nickname}")
	public List<UserDto> search(@PathVariable String nickname) {
		return userService.search(nickname);
	}
}
