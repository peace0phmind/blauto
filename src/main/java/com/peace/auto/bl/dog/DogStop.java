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
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(23, 58, 0))
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
        try {
            Scheduler scheduler = context.getScheduler();
            Optional<JobExecutionContext> first = scheduler.getCurrentlyExecutingJobs().stream().filter(x -> DogRun.class.getName().equals(x.getJobDetail().getJobClass().getName())).findFirst();
            if (first.isPresent()) {
                JobDetail jobDetail = first.get().getJobDetail();
                log.info("Interrupt job: {}", jobDetail);
                context.getScheduler().interrupt(jobDetail.getKey());
            }
        } catch (SchedulerException e) {
            log.info("{}", e);
        }
    }
}
