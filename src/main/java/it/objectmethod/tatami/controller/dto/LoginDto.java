package it.objectmethod.tatami.controller.dto;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class LoginDto {
	@NotNull
	private String username;
	@NotNull
	private String password;
}
