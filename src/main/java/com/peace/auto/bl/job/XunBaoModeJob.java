package com.peace.auto.bl.job;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import com.peace.auto.bl.task.DuoBao;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

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

    @Override
    public void execute() {
        prepare();
        xunbao();
    }

    public void prepare() {
//        try {
//            AndroidScreen region1 = DEVICE_1.getRegion();
//            AndroidScreen region2 = DEVICE_2.getRegion();
//
//            DENG_LU.checkAndChangeUser(region1, status, status.peaceName());
//            DENG_LU.checkAndChangeUser(region2, status, "peace0ph001");
//
//            status.setCurrentUser(status.peaceName());
//        } catch (Exception e) {
//            log.error("{}", e);
//
//            try {
//                DEVICE_1.stopDevice();
//                DEVICE_2.stopDevice();
//            } catch (Exception e1) {
//                log.error("{}", e1);
//            }
//        }
    }

    public void xunbao() {
        try {
            AndroidScreen region1 = DEVICE_1.getRegion();
            AndroidScreen region2 = DEVICE_2.getRegion();

            xunbao(region1, region2, Status.USERS.get(0), Status.USERS.get(1));
            xunbao(region1, region2, Status.USERS.get(2), Status.USERS.get(3));
            xunbao(region1, region2, Status.USERS.get(4), Status.USERS.get(5));
            xunbao(region1, region2, Status.USERS.get(6), Status.USERS.get(7));

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

    private void xunbao(Region region1, Region region2, String userName1, String userName2) throws FindFailed, InterruptedException {
        DENG_LU.checkAndChangeUser(region1, status, userName1);
        DENG_LU.checkAndChangeUser(region2, status, userName2);

        status.setCurrentUser(userName1);
        new DuoBao().xunbao(region1, region2, false, status.getRoomNo(), null);

        status.setCurrentUser(userName1);
        status.Done(Task.QI_BING_XUN_BAO);

        status.setCurrentUser(userName2);
        status.Done(Task.QI_BING_XUN_BAO);
    }
}
