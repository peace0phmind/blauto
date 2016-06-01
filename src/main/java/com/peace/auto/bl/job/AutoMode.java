package com.peace.auto.bl.job;

import com.peace.auto.bl.TaskItem;
import com.peace.auto.bl.task.DengLu;
import com.peace.auto.bl.task.IDo;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.sikuli.script.FindFailed;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 5/28/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class AutoMode implements Job {

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(AutoMode.class).build();
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

        AndroidScreen region = null;

        boolean bNeedRestart = false;
        try {
            region = DEVICE_1.getRegion();

            List<TaskItem> taskItems = status.getUserTasks();
            Map<Boolean, List<TaskItem>> trueForExecuteCollect = taskItems.stream().collect(Collectors.groupingBy(e -> e.getExecutableTime().isBefore(LocalDateTime.now())));
            List<TaskItem> trueForExecuteList = trueForExecuteCollect.get(true);
            log.info("taskItems: {}, trueForExecuteList: {}", taskItems, trueForExecuteList);

            while (trueForExecuteList.size() > 0) {
                String userName = trueForExecuteList.get(0).getUserName();
                log.info("Change to user: {}", userName);
                DENG_LU.checkUser(region, status, trueForExecuteList.get(0).getUserName());

                List<IDo> tasks = status.getTasks(status.getCurrentUser());
                log.info("currentUser: {}, tasks: {}", status.getCurrentUser(), tasks);

                for (IDo iDo : tasks) {
                    if (iDo.Done(region, status)) {
                        Thread.sleep(3 * 1000L);
                    }
                }

                // get next task user
                taskItems = status.getUserTasks();
                trueForExecuteCollect = taskItems.stream().collect(Collectors.groupingBy(e -> e.getExecutableTime().isBefore(LocalDateTime.now())));
                trueForExecuteList = trueForExecuteCollect.get(true);
                log.info("taskItems: {}, trueForExecuteList: {}", taskItems, trueForExecuteList);
            }

            if (trueForExecuteCollect.get(false).size() == 0) {
                addNewTrigger(context, 30 * 60);
                log.info("wait half an hour for next jobs.");
            } else {
                TaskItem taskItem = trueForExecuteCollect.get(false).get(0);
                log.info("Change to user for job: {}", taskItem.getUserName());
                DENG_LU.checkUser(region, status, taskItem.getUserName());

                Duration duration = Duration.between(taskItem.getExecutableTime(), LocalDateTime.now());
                log.info("After {} seconds to do job.", duration.getSeconds());

                addNewTrigger(context, duration.getSeconds());
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

            addNewTrigger(context, 5);
        }
    }

    private void addNewTrigger(JobExecutionContext context, long seconds) {
        Trigger tr = TriggerBuilder.newTrigger().forJob(context.getJobDetail())
                .startAt(DateBuilder.futureDate((int) seconds, DateBuilder.IntervalUnit.SECOND)).build();

        try {
            context.getScheduler().scheduleJob(tr);
        } catch (SchedulerException e) {
            log.info("{}", e);
        }
    }
}
