package com.peace.auto.bl.job;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.task.IDo;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static com.peace.auto.bl.common.CommonUtils.*;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class OrderModeJob implements Job, TaskJob {

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(OrderModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(0, 15, 0))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).withRepeatCount(20)).build();
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

        if (isValidTime(LocalTime.of(0, 15), LocalTime.of(13, 0))) {
            execute();
        }

        if (isValidTime(LocalTime.of(14, 0), LocalTime.of(21, 0))) {
            execute();
        }
    }

    @Override
    public void execute() {
        Settings.OcrTextRead = true;

        AndroidScreen region = null;
        try {
            region = startDevice(DEVICE_1);
            DENG_LU.QiDong(region, status);

            for (int i = 0; i < Status.getUserCount(); i++) {
                List<IDo> tasks = status.getTasks(status.getCurrentUser());
                log.info("currentUser: {}, tasks: {}", status.getCurrentUser(), tasks);

                for (IDo iDo : tasks) {
                    if (iDo.Done(region, status)) {
                        Thread.sleep(3 * 1000L);
                    }
                }

                // 最后一次不用重启
                if (i < Status.getUserCount() - 1) {
                    DENG_LU.Done(region, status);
                } else {
                    status.setCurrentUser(status.getNextLoginName());
                }
            }
        } catch (InterruptedException e) {
            log.error("{}", e);
        } catch (IOException e) {
            log.error("{}", e);
        } catch (FindFailed findFailed) {
            if (region != null) {
                region.saveScreenCapture(".", "error");
            }
            log.error("region: {}, {}", region, findFailed);
        } finally {
            if (region != null) {
                region.close();
            }
            try {
                stopDevice(DEVICE_1);
            } catch (IOException e) {
                log.error("{}", e);
            }
        }
    }
}
