package com.peace.auto.bl;

import com.peace.auto.bl.common.Device;
import com.peace.auto.bl.job.AutoMode;
import com.peace.auto.bl.task.HaiDiShiJie;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws FindFailed, InterruptedException, IOException, SchedulerException {
        Settings.OcrTextRead = true;
        log.info("Begin auto mode, {}", ManagementFactory.getRuntimeMXBean().getName());

        Device.killAllBoxSVC();
        time();

//        new XunBaoModeJob().execute(null);
//        new OrderModeJob().execute(null);
//        new DuoBaoModeJob().execute();

//        testMode();
    }

    private static void time() throws SchedulerException {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
//        OrderModeJob.init(defaultScheduler);
//        DuoBaoModeJob.init(defaultScheduler);
        AutoMode.init(defaultScheduler);
        defaultScheduler.start();
    }

    private static void testMode() throws IOException, InterruptedException, FindFailed {
        AndroidScreen region = DEVICE_1.getRegion(true);

//        DENG_LU.checkUser(region, status, status.peaceName());
        DENG_LU.checkUser(region, status, "peace0ph004");

        new HaiDiShiJie().Done(region, status);

        region.close();
    }
}
