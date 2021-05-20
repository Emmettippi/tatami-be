package it.objectmethod.tatami.scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.objectmethod.tatami.entity.Percentage;
import it.objectmethod.tatami.entity.enums.PercentageOperation;
import it.objectmethod.tatami.service.LobbyService;
import it.objectmethod.tatami.service.PercentageService;
import it.objectmethod.tatami.service.UserUserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduledPercentage {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private PercentageService percentageService;
	@Autowired
	private UserUserService userUserService;
	@Autowired
	private LobbyService lobbyService;

	public void doPercentage() {
		List<Percentage> percs = percentageService.checkPreparedTaskToRun(PercentageOperation.all());
		int size = percs.size();
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				log.info("Cron Percentage: Current Time - {}", FORMATTER.format(LocalDateTime.now()));
			}
			Percentage p = percs.get(i);
			log.info("!!! --- Init PercentageWorkerTask {} of {} with id: {} || TYPE: {} --- !!!", i + 1, size,
				p.getId(), p.getOperation().name());
			switch (p.getOperation()) {
			case ASK_FRIENDSHIP:
				this.caseAskFiendship(p, i, size);
				break;
			case JOIN_LOBBY:
				this.caseJoinLobby(p);
				break;
			case START_GAME:
				this.caseStartGame(p, i, size);
				break;
			default:
				break;
			}
		}
	}

	private void caseAskFiendship(Percentage p, int i, int size) {
		p = userUserService.handleFriendship(p);
		this.logStatus(p, i, size);
	}

	private void caseJoinLobby(Percentage p) {
		// TODO startGame
		p = lobbyService.handleJoinLobby(p);
	}

	private void caseStartGame(Percentage p, int i, int size) {
		this.logStatus(p, i, size);
	}

	private void logStatus(Percentage p, int i, int size) {
		log.info("Process {} at {}% in status {}", p.getId(), 100 * p.getProgression(),
			p.getProgressionStatus().name());
		log.info("!!! --- End PercentageWorkerTask {} of {} with id: {} --- !!!", i + 1, size, p.getId());
	}
}
