package it.objectmethod.tatami.controller.dto;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class UserSearchQueryParams {
	@NotNull
	private Long userId;
	@NotNull
	private String nickname;
	private String username;
	@NotNull
	private Integer page;
	@NotNull
	private Integer size;
}
