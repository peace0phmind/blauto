package com.peace.auto.bl.job;

import com.peace.auto.bl.Task;
import com.peace.auto.bl.task.DuoBao;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.time.LocalTime;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class XunBaoModeJob implements Job, TaskJob {

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(XunBaoModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(13, 52, 0)).build();

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
            for (int i = 0; i < 3; i++) {
                execute();
                try {
                    if (i < 3 - 1) {
                        Thread.sleep(5 * 60 * 1000L);
                    }
                } catch (InterruptedException e) {
                    log.error("{}", e);
                }
            }
        }
    }

    @Override
    public void execute() {
        prepare();
        xunbao();
    }

    public void prepare() {
        try {
            AndroidScreen region1 = DEVICE_1.getRegion();
            AndroidScreen region2 = DEVICE_2.getRegion();

            DENG_LU.checkUser(region1, status, status.peaceName());
            DENG_LU.checkUser(region2, status, "peace0ph001");

            status.setCurrentUser(status.peaceName());
            status.Done(Task.QI_BING_XUN_BAO_PREPARE);

        } catch (Exception e) {
            log.error("{}", e);

            try {
                DEVICE_1.stopDevice();
                DEVICE_2.stopDevice();
            } catch (Exception e1) {
                log.error("{}", e1);
            }
        }
    }

    public void xunbao() {
        try {
            AndroidScreen region1 = DEVICE_1.getRegion();
            AndroidScreen region2 = DEVICE_2.getRegion();

            new DuoBao().xunbao(region1, region2, false, status.getRoomNo(), null);

            status.setCurrentUser(status.peaceName());
            status.Done(Task.QI_BING_XUN_BAO);
        } catch (Exception e) {
            log.error("{}", e);

            try {
                DEVICE_1.stopDevice();
                DEVICE_2.stopDevice();
            } catch (Exception e1) {
                log.error("{}", e1);
            }
        }
    }
}
