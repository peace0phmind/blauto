package com.peace.auto.bl.job;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Task;
import com.peace.auto.bl.task.DuoBao;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class DuoBaoModeJob implements Job, TaskJob {

    private static List<List<String>> duobaoList = Arrays.asList(
            Arrays.asList("peace0ph002", "peace0ph003"),
            Arrays.asList("peace0ph004", "peace0ph006"),
            Arrays.asList("peace0ph007", "peace0ph008")
    );

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(DuoBaoModeJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(21, 30, 0)).build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("{}", e);
        }
    }

    public static void duobao(String userName) {
        AndroidScreen region1;
        AndroidScreen region2;
        AndroidScreen region3;

        try {
            region1 = DEVICE_1.getRegion();
            region2 = DEVICE_2.getRegion();
            region3 = DEVICE_3.getRegion();

            int count = status.todayFinishCount(Task.QI_BING_DUO_BAO, userName);
            String user1 = duobaoList.get(count).get(0);
            String user2 = duobaoList.get(count).get(1);

            DENG_LU.checkUser(region1, status, userName);
            DENG_LU.checkUser(region2, status, user1);
            DENG_LU.checkUser(region3, status, user2);

            boolean ok = new DuoBao().duobao(region1, region2, region3, status.getRoomNo());
            status.Done(Task.QI_BING_XUN_BAO, LocalDateTime.now(), user1);
            status.Done(Task.QI_BING_XUN_BAO, LocalDateTime.now(), user2);

            if (ok) {
                status.setCurrentUser(userName);
                status.Done(Task.QI_BING_DUO_BAO);
            }

            if (!status.peaceName().equals(userName)) {
                // 判断账号是否要额外寻宝
                if (status.todayFinishCount(Task.QI_BING_XUN_BAO, user1) < Task.QI_BING_XUN_BAO.getDayLimit(status.peaceName())) {
                    new DuoBao().xunbao(region2, region3, false, status.getRoomNo(), null);
                    status.Done(Task.QI_BING_XUN_BAO, LocalDateTime.now(), user1);
                    status.Done(Task.QI_BING_XUN_BAO, LocalDateTime.now(), user2);
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
            System.exit(-1);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Do job");

        if (isValidTime(LocalTime.of(21, 29), LocalTime.of(22, 29))) {
            execute();
        }
    }

    @Override
    public void execute() {
        try {
            duobao("peace");
            duobao("peace0ph001");
            Thread.sleep(9 * 60 * 1000L);

            duobao("peace");
            duobao("peace0ph001");
            Thread.sleep(9 * 60 * 1000L);

            duobao("peace");
            duobao("peace0ph001");
        } catch (Exception e) {
            log.error("{}", e);
            System.exit(-1);
        }
    }
}
