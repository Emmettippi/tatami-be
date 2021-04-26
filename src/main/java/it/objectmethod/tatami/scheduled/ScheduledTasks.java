package it.objectmethod.tatami.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	@Autowired
	private ScheduledPercentage scheduledPercentage;

	@Autowired
	private ScheduledBonification scheduledBonification;

	@Scheduled(cron = "${scheduler.cron.expression.percentage}")
	public void scheduledPercentageWorker() {
		scheduledPercentage.doPercentage();
	}

	@Scheduled(cron = "${scheduler.cron.expression.user_relation_bonification}")
	public void scheduledUserRelationBonification() {
		scheduledBonification.userRelationBonification();
		scheduledBonification.emptyPercentage();
	}

	@Scheduled(cron = "${scheduler.cron.expression.user_offline}")
	public void scheduledOfflineChecker() {
		scheduledBonification.setOffline();
	}

	@Scheduled(cron = "${scheduler.cron.expression.lobby_checker}")
	public void scheduledLobbyBonfication() {
		scheduledBonification.removeFromLobby();
	}
}
