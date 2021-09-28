package it.objectmethod.tatami.dto.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import it.objectmethod.tatami.dto.TatamiDto;
import it.objectmethod.tatami.entity.Tatami;

@Mapper(componentModel = "spring")
public abstract class TatamiMapper {

	@Mapping(target = "board", ignore = true)
	public abstract TatamiDto toDto(Tatami tatami);

	@AfterMapping
	public void afterMapping(@MappingTarget TatamiDto dto, Tatami tatami) {
		String[][] board = new String[5][5];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = String.valueOf(tatami.getBoard().charAt(i * board.length + j));
			}
		}
	}
}
