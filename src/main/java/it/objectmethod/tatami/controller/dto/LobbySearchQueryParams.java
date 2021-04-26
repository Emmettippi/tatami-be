package it.objectmethod.tatami.controller.dto;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class LobbySearchQueryParams {
	@NotNull
	private Long userId;
	private String lobbyType;
	private String name;
	@NotNull
	private Integer page;
	@NotNull
	private Integer size;
}
