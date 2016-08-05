package com.peace.auto.bl.job;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.common.Device;
import com.peace.auto.bl.task.IDo;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.List;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class OrderModeJob implements Job, TaskJob {

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(OrderModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(0, 15, 0))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).withRepeatCount(30)).build();
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever()).build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Do job.");

        for (int i = 0; i < Status.getUserCount(); i++) {
            execute();
        }
    }

    @Override
    public void execute() {
        doTask(DEVICE_1, status.getNextLoginName());
    }

    public void doTask(Device device, String userName) {
        AndroidScreen region = null;

        try {
            region = device.getRegion();
            DENG_LU.checkAndChangeUser(region, status, userName);
            List<IDo> tasks = status.getTasks(status.getCurrentUser());
            log.info("currentUser: {}, tasks: {}", status.getCurrentUser(), tasks);

            for (IDo iDo : tasks) {
                if (iDo.CanDo(status, status.getCurrentUser())) {
                    if (iDo.Done(region, status)) {
                        Thread.sleep(3 * 1000L);
                    }
                }
            }
        } catch (Exception e) {
            log.error("region: {}, {}", region, e);
            try {
                if (region != null) {
                    String currentUser = status.getCurrentUser();
                    if (currentUser == null || currentUser.trim().equals("")) {
                        String wantUser = status.getWantUser();
                        if (wantUser == null || wantUser.trim().equals("")) {
                            currentUser = "error";
                        } else {
                            currentUser = wantUser + "-w";
                        }
                    }
                    region.saveScreenCapture(".", currentUser);
                }
                device.stopDevice();
            } catch (Exception e1) {
                log.error("{}", e1);
            }

            System.exit(-1);
        }
    }
}
