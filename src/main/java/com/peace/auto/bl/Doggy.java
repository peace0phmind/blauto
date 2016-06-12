package com.peace.auto.bl;

import com.peace.auto.bl.dog.DogRun;
import com.peace.auto.bl.dog.DogStop;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by mind on 6/11/16.
 */
@Slf4j
public class Doggy {
    public static void main(String[] args) throws SchedulerException {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();

        DogRun.init(defaultScheduler);
        DogStop.init(defaultScheduler);

        defaultScheduler.start();

        log.info("Doggy run...");
    }
}
