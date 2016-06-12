package com.peace.auto.bl.dog;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by mind on 6/11/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class DogRun implements InterruptableJob {

    private Process run;
    private boolean _interrupted = false;

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(DogRun.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(0, 15, 0))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).withRepeatCount(30)).build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            while (!_interrupted) {
                log.info("Begin run process.");
                run = Runtime.getRuntime().exec(new String[]{"java", "-cp", "bl-1.0-SNAPSHOT.jar", "com.peace.auto.bl.Auto"});
                run.waitFor();
                log.info("End of process");
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        _interrupted = true;

        if (run != null) {
            run.destroy();
            log.info("destroy job");

            try {
                if (!run.waitFor(120, TimeUnit.SECONDS)) {
                    log.info("force destroy job");
                    run.destroyForcibly();
                }
            } catch (InterruptedException e) {
                log.error("force destroy job, {}", e);
                run.destroyForcibly();
            }

            run = null;
        }
    }
}
