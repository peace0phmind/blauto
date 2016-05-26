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

import static com.peace.auto.bl.common.CommonUtils.*;

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
        AndroidScreen region1 = null;
        AndroidScreen region2 = null;
        AndroidScreen region3 = null;
        try {
            region1 = startDevice(DEVICE_1);
            region2 = startDevice(DEVICE_2);
            region3 = startDevice(DEVICE_3);

            List<Region> regions = Arrays.asList(region1, region2, region3);
            log.info("{}", regions);

            DENG_LU.QiDong(region1, status, "peace");
            DENG_LU.QiDong(region2, status, "peace0ph006");
            DENG_LU.QiDong(region3, status, "peace0ph004");

            duobaoMode(regions, Arrays.asList("peace", "peace0ph006", "peace0ph004"));
            duobaoMode(regions, Arrays.asList("peace0ph001", "peace0ph006", "peace0ph004"));
            Thread.sleep(10 * 60 * 1000L);

            duobaoMode(regions, Arrays.asList("peace0ph001", "peace0ph006", "peace0ph004"));
            duobaoMode(regions, Arrays.asList("peace", "peace0ph008", "peace0ph007"));
            Thread.sleep(10 * 60 * 1000L);

            duobaoMode(regions, Arrays.asList("peace", "peace0ph008", "peace0ph007"));
            duobaoMode(regions, Arrays.asList("peace0ph001", "peace0ph008", "peace0ph007"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FindFailed findFailed) {
            findFailed.printStackTrace();
        } finally {
            if (region1 != null) {
                region1.close();
            }

            if (region2 != null) {
                region2.close();
            }

            if (region3 != null) {
                region3.close();
            }

            try {
                stopDevice(DEVICE_1);
                stopDevice(DEVICE_2);
                stopDevice(DEVICE_3);
            } catch (IOException e) {
                log.error("{}", e);
            }
        }
    }
}
