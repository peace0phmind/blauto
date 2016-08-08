package com.peace.auto.bl.dog;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Optional;

/**
 * Created by mind on 6/11/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class DogStop implements Job {
    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(DogStop.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(22, 30, 0))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).withRepeatCount(30)).build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Stop job.");
        JobDetail jobDetail = null;
        Scheduler scheduler = context.getScheduler();
        try {
            Optional<JobExecutionContext> first = scheduler.getCurrentlyExecutingJobs().stream().filter(x -> DogRun.class.getName().equals(x.getJobDetail().getJobClass().getName())).findFirst();
            if (first.isPresent()) {
                jobDetail = first.get().getJobDetail();
                // set next job start
                Trigger tr = TriggerBuilder.newTrigger().forJob(jobDetail).startAt(DateBuilder.tomorrowAt(0, 15, 0)).build();
                scheduler.scheduleJob(tr);

                log.info("Interrupt job: {}", jobDetail);
                context.getScheduler().interrupt(jobDetail.getKey());
            }
        } catch (SchedulerException e) {
            log.error("{}", e);
            if (e instanceof JobPersistenceException) {
                log.info("Job persistence exception");
                if (jobDetail != null) {
                    try {
                        Trigger tr = TriggerBuilder.newTrigger().startAt(DateBuilder.tomorrowAt(0, 15, 0)).build();
                        scheduler.scheduleJob(JobBuilder.newJob(DogRun.class).build(), tr);
                    } catch (SchedulerException e1) {
                        log.error("{}", e);
                    }
                }
            }
        }
    }
}
