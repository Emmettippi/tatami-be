package it.objectmethod.tatami.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public class SchedulerConfig implements SchedulingConfigurer {

	@Value("${scheduler.thread.pool.size}")
	private int POOL_SIZE;

	@Value("${scheduler.prefix.thread.pool.name}")
	private String prefixThreadPoolName;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

		scheduler.setPoolSize(POOL_SIZE);
		scheduler.setThreadNamePrefix(prefixThreadPoolName);
		scheduler.initialize();

		taskRegistrar.setTaskScheduler(scheduler);
	}
}