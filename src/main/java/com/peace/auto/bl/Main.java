package com.peace.auto.bl;

import com.peace.auto.bl.common.Device;
import com.peace.auto.bl.job.AutoMode;
import com.peace.auto.bl.job.DuoBaoModeJob;
import com.peace.auto.bl.job.OrderModeJob;
import com.peace.auto.bl.job.XunBaoModeJob;
import com.peace.auto.bl.task.Building;
import com.peace.auto.bl.task.ShenQi;
import com.peace.auto.bl.task.ShouGuFang;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.util.Random;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class Main {

    public static void main(String[] args) throws FindFailed, InterruptedException, IOException, SchedulerException {
        Settings.OcrTextRead = true;

//        status.getUserTasks();

        Device.killAllBoxSVC();
        time();
//        new XunBaoModeJob().execute();
//        new DuoBaoModeJob().execute();

//        testMode();
    }

    private static void time() throws SchedulerException {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        OrderModeJob.init(defaultScheduler);
//        XunBaoModeJob.init(defaultScheduler);
//        DuoBaoModeJob.init(defaultScheduler);
//        AutoMode.init(defaultScheduler);
        defaultScheduler.start();
    }

    private static void testMode() throws IOException, InterruptedException, FindFailed {
//        AndroidScreen region = startDevice(DEVICE_1);
        AndroidScreen region = DEVICE_1.getRegion(true);

        DENG_LU.checkUser(region, status, "peace");
        Random random = new Random();

//        new ShouGuFang().Done(region, status);
        for (int i = 0; i < 1; i++) {
            new ShenQi().Done(region, status);
            Thread.sleep((random.nextInt(20) + 1) * 1000L);
        }


        region.close();
//        stopDevice(DEVICE_1);
    }
}
