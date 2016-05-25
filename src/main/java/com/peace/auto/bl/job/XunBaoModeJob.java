package com.peace.auto.bl.job;

import com.peace.auto.bl.task.DuoBao;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.time.LocalTime;

import static com.peace.auto.bl.common.CommonUtils.*;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class XunBaoModeJob implements Job, TaskJob {

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(XunBaoModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(13, 36, 0)).build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Do job");

        if (isValidTime(LocalTime.of(13, 35), LocalTime.of(14, 0))) {
            execute();
        }
    }

    @Override
    public void execute() {
        Settings.OcrTextRead = true;
        AndroidScreen region1 = null;
        AndroidScreen region2 = null;
        try {
            region1 = startDevice(DEVICE_1);
            region2 = startDevice(DEVICE_2);

            DENG_LU.QiDong(region1, status, "peace");
            DENG_LU.QiDong(region2, status, "peace0ph001");

            new DuoBao().xunbao(region1, region2, false);
            Thread.sleep(5 * 60 * 1000L);

            new DuoBao().xunbao(region1, region2, false);
            Thread.sleep(5 * 60 * 1000L);

            new DuoBao().xunbao(region1, region2, false);
            Thread.sleep(3 * 1000L);

        } catch (InterruptedException e) {
            log.error("{}", e);
        } catch (IOException e) {
            log.error("{}", e);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } finally {
            if (region1 != null) {
                region1.close();
            }
            if (region2 != null) {
                region2.close();
            }

            try {
                stopDevice(DEVICE_1);
            } catch (IOException e) {
                log.error("{}", e);
            }

            try {
                stopDevice(DEVICE_2);
            } catch (IOException e) {
                log.error("{}", e);
            }

        }
    }
}
