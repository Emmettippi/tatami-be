package it.objectmethod.tatami.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.PercentageQueryParams;

@Repository
public interface PercentageQueryParamsRepository extends JpaRepository<PercentageQueryParams, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM percentage_query_params WHERE percentage_id = :percentageId")
	List<PercentageQueryParams> findByPercentageId(@Param("percentageId") Long id);
}
