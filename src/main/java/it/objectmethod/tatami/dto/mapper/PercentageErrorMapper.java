package it.objectmethod.tatami.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.objectmethod.tatami.dto.PercentageErrorDto;
import it.objectmethod.tatami.entity.PercentageError;

@Mapper(componentModel = "spring")
public abstract class PercentageErrorMapper {

	@Mapping(target = "percentageId", source = "percentage.id")
	public abstract PercentageErrorDto toDto(PercentageError error);

	public abstract List<PercentageErrorDto> toDto(List<PercentageError> error);
}
