package com.peace.auto.bl;

import com.peace.auto.bl.job.AutoMode;
import com.peace.auto.bl.task.ChuZheng;
import com.peace.auto.bl.task.ExchangeCodeTask;
import com.peace.auto.bl.task.TianSheng;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.time.LocalDateTime;

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
        AndroidScreen region = DEVICE_1.getRegion(true);

//        DENG_LU.checkUser(region, status, status.peaceName());
        String user = "peace";
        DENG_LU.checkUser(region, status, user);

        if (new ExchangeCodeTask().CanDo(status, user)) {
            new ExchangeCodeTask().Done(region, status);
        }

        region.close();
    }
}
