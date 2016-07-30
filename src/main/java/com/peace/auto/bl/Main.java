package com.peace.auto.bl;

import com.peace.auto.bl.job.AutoMode;
import com.peace.auto.bl.job.OrderModeJob;
import com.peace.auto.bl.task.*;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws FindFailed, InterruptedException, IOException, SchedulerException {
        Settings.OcrTextRead = true;

//        status.getUserTasks().forEach(x -> log.info("{}", x));

//        Device.killAllBoxSVC();
//        time();

//        new DuoBaoModeJob().execute();
//        new XunBaoModeJob().execute(null);

        testMode();
//        System.exit(0);
//        testGetTask();
//        time();
    }

    private static void time() throws SchedulerException {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
//        OrderModeJob.init(defaultScheduler);
//        DuoBaoModeJob.init(defaultScheduler);
        AutoMode.init(defaultScheduler);
        defaultScheduler.start();
    }

    private static void testGetTask() {
//        LocalDateTime now = LocalDateTime.now().withHour(11).withMinute(0);
//        for (int i = 0; i < 1000; i++) {
//            log.info("{}, {}", now.plusMinutes(i), status.getUserTasks(now.plusMinutes(i)).get(0));
//        }

        status.getUserTasks(LocalDateTime.now());
    }

    private static void testMode() throws IOException, InterruptedException, FindFailed {
        AndroidScreen region = DEVICE_2.getRegion(true);

//        DENG_LU.checkUser(region, status, status.peaceName());
        String user = "peace";

        DENG_LU.checkUser(region, status, user);
        List<IDo> tasks = status.getTasks(status.getCurrentUser());
        log.info("currentUser: {}, tasks: {}", status.getCurrentUser(), tasks);

        for (IDo iDo : tasks) {
            if (iDo.CanDo(status, status.getCurrentUser())) {
                if (iDo.Done(region, status)) {
                    Thread.sleep(3 * 1000L);
                }
            }
        }

//        if (new ShiChang().CanDo(status, user)) {
//            new ShiChang().Done(region, status);
//        }

        region.close();
    }
}
