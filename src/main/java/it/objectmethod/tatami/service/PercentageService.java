package it.objectmethod.tatami.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.objectmethod.tatami.entity.Percentage;
import it.objectmethod.tatami.entity.PercentageQueryParams;
import it.objectmethod.tatami.entity.User;
import it.objectmethod.tatami.entity.enums.PercentageOperation;
import it.objectmethod.tatami.entity.enums.PercentageStatus;
import it.objectmethod.tatami.repository.PercentageQueryParamsRepository;
import it.objectmethod.tatami.repository.PercentageRepository;
import it.objectmethod.tatami.repository.UserRepository;
import it.objectmethod.tatami.utils.Utils;

@Service
public class PercentageService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PercentageRepository percentageRepository;
	@Autowired
	private PercentageQueryParamsRepository percentageQueryParamsRepository;

	public Percentage save(Percentage percentage) {
		return this.save(percentage, false);
	}

	public Percentage save(Percentage percentage, boolean withErrors) {
		if (percentage == null) {
			return null;
		}
		if (percentage.getProgression().doubleValue() >= 1.0) {
			percentage.setProgressionStatus(
				withErrors ? PercentageStatus.COMPLETED_WITH_ERRORS : PercentageStatus.COMPLETED);
			percentage.setLocked(Boolean.FALSE);
		}
		return percentageRepository.save(percentage);
	}

	public Percentage findById(Long id) {
		if (id == null) {
			return null;
		}
		return percentageRepository.getOne(id);
	}

	public List<Percentage> checkPreparedTaskToRun(List<PercentageOperation> operations) {
		List<Percentage> percentages = new ArrayList<>();

		List<Percentage> p;
		for (PercentageOperation o : operations) {
			List<PercentageOperation> ops = new ArrayList<>();

			switch (o) {
			case ASK_FRIENDSHIP:
				ops.add(PercentageOperation.ASK_FRIENDSHIP);
				p = this.caseFindNotLockedWithoutUser(ops);
				break;
			case JOIN_LOBBY:
				ops.add(PercentageOperation.JOIN_LOBBY);
				p = this.caseFindNotLockedWithoutUser(ops);
				break;
			default:
				p = null;
			}
			if (p != null && !p.isEmpty()) {
				percentages.addAll(p);
			}
		}
		return percentages;
	}

	/**
	 * Finds the percentages which lock the relative operation WITHOUT user clause
	 * 
	 * @param operation operation to check
	 * @return percentages ready to be processed
	 */
	public List<Percentage> checkLockedOperation(PercentageOperation operation, boolean onlyRunning) {
		List<PercentageOperation> ops = new ArrayList<>();
		ops.add(operation);
		return this.checkLockedOperations(ops, onlyRunning);
	}

	/**
	 * Finds the percentages which lock the relative operations WITHOUT user clause
	 * 
	 * @param operations operations to check
	 * @param onlyRunning it true will check only for PROCESSING process, else also
	 *            for NEW and RETRY
	 * @return percentages ready to be processed
	 */
	public List<Percentage> checkLockedOperations(List<PercentageOperation> operations, boolean onlyRunning) {
		List<Percentage> lockedPercentages = new ArrayList<>();
		List<Percentage> lp;
		for (PercentageOperation operation : operations) {
			List<PercentageStatus> statuses = new ArrayList<>();
			statuses.add(PercentageStatus.PROGRESSING);
			if (!onlyRunning) {
				statuses.add(PercentageStatus.NEW);
				statuses.add(PercentageStatus.RETRY);
			}
			lp = percentageRepository.findByOperationAndProgressionStatusInAndLockedIsTrue(operation, statuses);
			if (lp != null && !lp.isEmpty()) {
				lockedPercentages.addAll(lp);
			}
		}
		return lockedPercentages;
	}

	/**
	 * Finds the percentages which lock the relative operation WITH user clause
	 * 
	 * @param operation operation to check
	 * @param onlyRunning it true will check only for PROCESSING process, else also
	 *            for NEW and RETRY
	 * @param userId the user id
	 * @return percentages ready to be processed
	 */
	public List<Percentage> checkLockedByUserOperation(PercentageOperation operation, Long userId,
		boolean onlyRunning) {
		List<PercentageOperation> ops = new ArrayList<>();
		ops.add(operation);
		return this.checkLockedByUserOperations(ops, userId, onlyRunning);
	}

	/**
	 * Finds the percentages which lock the relative operations WITH user clause
	 * 
	 * @param operations operations to check
	 * @param userId the user id
	 * @param onlyRunning it true will check only for PROCESSING process, else also
	 *            for NEW and RETRY
	 * @return percentages ready to be processed
	 */
	public List<Percentage> checkLockedByUserOperations(List<PercentageOperation> operations, Long userId,
		boolean onlyRunning) {
		List<Percentage> lockedPercentages = new ArrayList<>();
		List<Percentage> lp;
		for (PercentageOperation operation : operations) {
			List<PercentageStatus> statuses = new ArrayList<>();
			statuses.add(PercentageStatus.PROGRESSING);
			if (!onlyRunning) {
				statuses.add(PercentageStatus.NEW);
				statuses.add(PercentageStatus.RETRY);
			}
			lp = percentageRepository.findByOperationAndProgressionStatusInAndLockedIsTrueAndLockedBy(operation,
				statuses, userRepository.getOne(userId));
			if (lp != null && !lp.isEmpty()) {
				lockedPercentages.addAll(lp);
			}
		}
		return lockedPercentages;
	}

	/**
	 * Finds the percentages ready to be processed not locked, without user clause
	 * 
	 * @param operations operations to check
	 * @return percentages ready to be processed
	 */
	private List<Percentage> caseFindNotLockedWithoutUser(List<PercentageOperation> operations) {
		List<Percentage> lockedPercentages = null;
		List<Percentage> preparedPercentages = new ArrayList<>();
		for (PercentageOperation operation : operations) {
			lockedPercentages = this.checkLockedOperation(operation, true);
			if (lockedPercentages == null || lockedPercentages.isEmpty()) {
				List<PercentageStatus> statuses = new ArrayList<>();
				statuses.add(PercentageStatus.NEW);
				statuses.add(PercentageStatus.RETRY);
				preparedPercentages
					.addAll(percentageRepository.findByOperationAndProgressionStatusIn(operation, statuses));
			}
		}
		return preparedPercentages;
	}

	//	/**
	//	 * TODO HAS TO BE DONE Finds the percentages ready to be processed not locked,
	//	 * WITH user clause
	//	 * 
	//	 * @param operations operations to check
	//	 * @param userId the userId
	//	 * @return percentages ready to be processed
	//	 */
	//	private List<Percentage> caseFindNotLockedByUser(List<PercentageOperation> operations, Long userId) {
	//		return new ArrayList<>();
	//	}

	public List<PercentageQueryParams> findByPercentageId(Long percentageId) {
		if (percentageId == null) {
			return null;
		}
		return percentageQueryParamsRepository.findByPercentageId(percentageId);
	}

	public Percentage prepareQueryParams(User loggedUser, PercentageQueryParams params, PercentageOperation operation) {
		Percentage perc = new Percentage();
		perc.setCreatedAt(Utils.now());
		perc.setLastUpdated(Utils.now());
		perc.setRelatedTo(loggedUser);
		perc.setOperation(operation);
		perc.setProgressionStatus(PercentageStatus.NEW);
		perc.setProgression(Double.valueOf(0));

		perc = this.save(perc);
		params.setPercentage(perc);
		percentageQueryParamsRepository.save(params);

		return perc;
	}

	@Transactional
	public void emptyPercentage() {
		this.percentageRepository.emptyPercentage();
	}
}
