package it.objectmethod.tatami.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.Percentage;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.enums.PercentageOperation;
import it.objectmethod.tatami.entity.enums.PercentageStatus;

@Repository
public interface PercentageRepository extends JpaRepository<Percentage, Long> {

	/**
	 * Use this method mostly with status NEW and RETRY
	 * 
	 * @param operation the operationType
	 * @param progressionStatuses the statuses
	 * @return a list of percentages
	 */
	List<Percentage> findByOperationAndProgressionStatusIn(PercentageOperation operation,
		List<PercentageStatus> progressionStatuses);

	/**
	 * Use this method to find progressing percentages which locks out any other
	 * operations of the same type
	 * 
	 * @param operation the operationType
	 * @param progressionStatuses the statuses
	 * @return a list of percentages
	 */
	List<Percentage> findByOperationAndProgressionStatusInAndLockedIsTrue(PercentageOperation operation,
		List<PercentageStatus> progressionStatuses);

	/**
	 * Use this method to find progressing percentages which locks out other
	 * operations of the same type for the same user!
	 * 
	 * @param operation the operationType
	 * @param progressionStatuses the statuses
	 * @param lockedBy the user locked out for this operation
	 * @return a list of percentages
	 */
	List<Percentage> findByOperationAndProgressionStatusInAndLockedIsTrueAndLockedBy(PercentageOperation operation,
		List<PercentageStatus> progressionStatuses, User lockedBy);

	List<Percentage> findByRelatedToOrderByCreatedAtDesc(User user);

	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM percentage_progress WHERE progression_status = 'COMPLETED'")
	void emptyPercentage();
}
