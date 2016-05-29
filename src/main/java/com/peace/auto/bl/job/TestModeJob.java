package com.peace.auto.bl.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class TestModeJob implements Job, TaskJob {

    private Random random = new Random();

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(TestModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(11, 15, 0)).build();

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
        int i = random.nextInt(10) + 1;

        log.info("Do job: {}, {}", LocalDateTime.now(), i);

        Trigger tr = TriggerBuilder.newTrigger().forJob(context.getJobDetail()).startAt(DateBuilder.futureDate(i, DateBuilder.IntervalUnit.SECOND)).build();

        try {
            context.getScheduler().scheduleJob(tr);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }

    }
}
