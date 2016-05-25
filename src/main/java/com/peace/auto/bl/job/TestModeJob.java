package com.peace.auto.bl.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.time.LocalDateTime;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class TestModeJob implements Job, TaskJob {

    public TestModeJob(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(TestModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(11, 15, 0))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(3)).build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }
    }

    @Override
    public void execute() {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("{}", LocalDateTime.now());
            Thread.sleep(9 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
