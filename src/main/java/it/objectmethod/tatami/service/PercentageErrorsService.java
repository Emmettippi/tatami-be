package it.objectmethod.tatami.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.entity.PercentageError;
import it.objectmethod.tatami.repository.PercentageErrorsRepository;

@Service
public class PercentageErrorsService {

	@Autowired
	private PercentageErrorsRepository percentageErrorsRepository;

	public List<PercentageError> findByPercentageId(Long percentageId) {
		return percentageErrorsRepository.findByPercentageId(percentageId);
	}

	public PercentageError save(PercentageError percentageError) {
		return percentageErrorsRepository.save(percentageError);
	}
}
