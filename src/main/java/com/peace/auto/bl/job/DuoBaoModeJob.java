package com.peace.auto.bl.job;

import com.peace.auto.bl.task.DuoBao;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class DuoBaoModeJob implements Job, TaskJob {

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(DuoBaoModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(21, 30, 0)).build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }
    }

    private static void duobaoMode(List<Region> regions, List<String> users) throws InterruptedException, FindFailed, IOException {
        DENG_LU.checkUser(regions.get(0), status, users.get(0));
        DENG_LU.checkUser(regions.get(1), status, users.get(1));
        DENG_LU.checkUser(regions.get(2), status, users.get(2));

        new DuoBao().duobao(regions.get(0), regions.get(1), regions.get(2));
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Do job");

        if (isValidTime(LocalTime.of(21, 29), LocalTime.of(22, 29))) {
            execute();
        }
    }

    @Override
    public void execute() {
        try {
            AndroidScreen region1 = DEVICE_1.getRegion();
            AndroidScreen region2 = DEVICE_2.getRegion();
            AndroidScreen region3 = DEVICE_3.getRegion();

            List<Region> regions = Arrays.asList(region1, region2, region3);
            log.info("{}", regions);

            duobaoMode(regions, Arrays.asList("peace", "peace0ph006", "peace0ph004"));
            duobaoMode(regions, Arrays.asList("peace0ph001", "peace0ph006", "peace0ph004"));
            Thread.sleep(10 * 60 * 1000L);

            duobaoMode(regions, Arrays.asList("peace0ph001", "peace0ph006", "peace0ph004"));
            duobaoMode(regions, Arrays.asList("peace", "peace0ph008", "peace0ph007"));
            Thread.sleep(10 * 60 * 1000L);

            duobaoMode(regions, Arrays.asList("peace", "peace0ph008", "peace0ph007"));
            duobaoMode(regions, Arrays.asList("peace0ph001", "peace0ph008", "peace0ph007"));

        } catch (InterruptedException e) {
            log.error("{}", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("{}", e);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } finally {
            try {
                DEVICE_1.stopDevice();
                DEVICE_2.stopDevice();
                DEVICE_3.stopDevice();
            } catch (IOException e) {
                log.error("{}", e);
            } catch (InterruptedException e) {
                log.error("{}", e);
            }
        }
    }
}
