package it.objectmethod.tatami.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.PercentageError;

@Repository
public interface PercentageErrorsRepository extends JpaRepository<PercentageError, Long> {

	List<PercentageError> findByPercentageId(Long id);
}
