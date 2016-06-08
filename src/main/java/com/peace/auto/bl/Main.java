package com.peace.auto.bl;

import com.peace.auto.bl.common.Device;
import com.peace.auto.bl.job.AutoMode;
import com.peace.auto.bl.job.DuoBaoModeJob;
import com.peace.auto.bl.job.OrderModeJob;
import com.peace.auto.bl.job.XunBaoModeJob;
import com.peace.auto.bl.task.Building;
import com.peace.auto.bl.task.LianMeng;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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

        Device.killAllBoxSVC();
//        time();

//        new XunBaoModeJob().execute();
        new DuoBaoModeJob().execute();

//        testMode();
//        ttt();

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

        DENG_LU.checkUser(region, status, status.peaceName());
//        DENG_LU.checkUser(region, status, "peace0ph008");

        new LianMeng().Done(region, status);

        region.close();
    }
}
