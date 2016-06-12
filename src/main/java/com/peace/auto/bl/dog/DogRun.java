package com.peace.auto.bl.dog;

import lombok.extern.slf4j.Slf4j;
import org.omg.SendingContext.RunTime;
import org.quartz.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                run = Runtime.getRuntime().exec(new String[]{"java", "-cp", "bl-1.0-SNAPSHOT.jar", "com.peace.auto.bl.Main"});

//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(run.getInputStream()));
//                for (int i = 0; i < 50; i++) {
//                    log.info("py: {}", bufferedReader.readLine());
//                }

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
            run.destroyForcibly();
            run = null;
        }
    }
}
