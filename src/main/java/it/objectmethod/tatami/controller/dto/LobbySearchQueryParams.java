package it.objectmethod.tatami.controller.dto;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class LobbySearchQueryParams extends LobbyQueryParams {
	@NotNull
	private Long userId;
	@NotNull
	private Integer page;
	@NotNull
	private Integer size;
}
