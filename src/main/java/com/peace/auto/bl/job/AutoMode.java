package com.peace.auto.bl.job;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.task.IDo;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.util.List;

import static com.peace.auto.bl.common.Devices.DENG_LU;
import static com.peace.auto.bl.common.Devices.DEVICE_1;
import static com.peace.auto.bl.common.Devices.status;

/**
 * Created by mind on 5/28/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class AutoMode implements Job, TaskJob {

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(OrderModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(0, 15, 0)).build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Do job.");

        execute();

//        Trigger tr = TriggerBuilder.newTrigger().forJob(context.getJobDetail()).startAt(DateBuilder.futureDate(i, DateBuilder.IntervalUnit.SECOND)).build();
//
//        try {
//            context.getScheduler().scheduleJob(tr);
//        } catch (SchedulerException e) {
//            log.info("{}", e);
//
//        }
    }

    @Override
    public void execute() {
        AndroidScreen region = null;

        boolean bNeedRestart = false;
        try {
            region = DEVICE_1.getRegion(status);

            for (int i = 0; i < Status.getUserCount(); i++) {

                List<IDo> tasks = status.getTasks(status.getCurrentUser());
                log.info("currentUser: {}, tasks: {}", status.getCurrentUser(), tasks);

                for (IDo iDo : tasks) {
                    if (iDo.Done(region, status)) {
                        Thread.sleep(3 * 1000L);
                    }
                }

                DENG_LU.Done(region, status);
            }
        } catch (InterruptedException e) {
            log.error("{}", e);
            bNeedRestart = true;
        } catch (IOException e) {
            log.error("{}", e);
            bNeedRestart = true;
        } catch (FindFailed findFailed) {
            if (region != null) {
                region.saveScreenCapture(".", "error");
            }
            log.error("region: {}, {}", region, findFailed);
            bNeedRestart = true;
        }

        if (bNeedRestart) {
            try {
                DEVICE_1.stopDevice();
            } catch (IOException e) {
                log.error("{}", e);
            } catch (InterruptedException e) {
                log.error("{}", e);
            }
        }
    }
}
