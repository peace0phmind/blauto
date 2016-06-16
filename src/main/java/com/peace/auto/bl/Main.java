package com.peace.auto.bl;

import com.peace.auto.bl.common.Device;
import com.peace.auto.bl.job.DuoBaoModeJob;
import com.peace.auto.bl.job.OrderModeJob;
import com.peace.auto.bl.job.XunBaoModeJob;
import com.peace.auto.bl.task.ChuZheng;
import com.peace.auto.bl.task.NongChang;
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

//        Device.killAllBoxSVC();
//        time();

//        new DuoBaoModeJob().execute();
//        new XunBaoModeJob().execute(null);

        testMode();
//        System.exit(0);
    }

    private static void time() throws SchedulerException {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        OrderModeJob.init(defaultScheduler);
//        DuoBaoModeJob.init(defaultScheduler);
//        AutoMode.init(defaultScheduler);
        defaultScheduler.start();
    }

    private static void testMode() throws IOException, InterruptedException, FindFailed {
        AndroidScreen region = DEVICE_1.getRegion(true);

//        DENG_LU.checkUser(region, status, status.peaceName());
        DENG_LU.checkUser(region, status, "peace0ph001");

        if (new ChuZheng().CanDo(status, "peace0ph001")) {
            new ChuZheng().Done(region, status);
        }

        region.close();
    }
}
