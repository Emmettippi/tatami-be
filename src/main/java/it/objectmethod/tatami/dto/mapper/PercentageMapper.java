package it.objectmethod.tatami.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import it.objectmethod.tatami.dto.PercentageDto;
import it.objectmethod.tatami.entity.Percentage;

@Mapper(componentModel = "spring", uses = { PercentageErrorMapper.class, PercentageQueryParamsMapper.class })
public abstract class PercentageMapper {

	public abstract PercentageDto toDto(Percentage perc);

	public abstract List<PercentageDto> toDto(List<Percentage> perc);
}
