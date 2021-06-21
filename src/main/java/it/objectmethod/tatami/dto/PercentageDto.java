package it.objectmethod.tatami.dto;

import java.util.Date;
import java.util.List;

import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.enums.PercentageOperation;
import it.objectmethod.tatami.entity.enums.PercentageStatus;
import lombok.Data;

@Data
public class PercentageDto {
	private Long id;
	private PercentageOperation operation;
	private User relatedTo;
	private Boolean locked;
	private User lockedBy;
	private Date createdAt;
	private Date lastUpdated;
	private Double progression;
	private PercentageStatus progressionStatus;
	private List<PercentageErrorDto> percentageErrors;
	private List<PercentageQueryParamsDto> percentageQueryParams;
}
