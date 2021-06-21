package it.objectmethod.tatami.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.objectmethod.tatami.dto.PercentageQueryParamsDto;
import it.objectmethod.tatami.entity.PercentageQueryParams;

@Mapper(componentModel = "spring")
public abstract class PercentageQueryParamsMapper {

	@Mapping(target = "percentageId", source = "percentage.id")
	public abstract PercentageQueryParamsDto toDto(PercentageQueryParams queryParams);

	public abstract List<PercentageQueryParamsDto> toDto(List<PercentageQueryParams> queryParams);
}
