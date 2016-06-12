package com.peace.auto.bl;

import com.peace.auto.bl.common.Device;
import com.peace.auto.bl.job.AutoMode;
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
public class Auto {

    public static void main(String[] args) throws FindFailed, InterruptedException, IOException, SchedulerException {
        Settings.OcrTextRead = true;
        log.info("Begin auto mode, {}", ManagementFactory.getRuntimeMXBean().getName());

        Device.killAllBoxSVC();

        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        AutoMode.init(defaultScheduler);
        defaultScheduler.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    log.info("run shut down hook.");
                    defaultScheduler.shutdown();
                    DEVICE_1.stopDevice();
                    DEVICE_2.stopDevice();
                    DEVICE_3.stopDevice();
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }
        });
    }
}
