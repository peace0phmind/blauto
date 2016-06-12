package com.peace.auto.bl.job;

import com.peace.auto.bl.TaskItem;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.peace.auto.bl.common.Devices.status;

/**
 * Created by mind on 5/28/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class AutoMode implements Job {

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(AutoMode.class).build();
//        Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.tomorrowAt(0, 15, 0)).build();
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

        List<TaskItem> userTasks = status.getUserTasks();

        while (userTasks != null && userTasks.size() > 0) {
            TaskItem ti = userTasks.get(0);

            if (LocalDateTime.now().isAfter(ti.getExecutableTime())) {
                switch (ti.getTask()) {
                    case QI_BING_XUN_BAO_PREPARE:
                        log.info("Do xun bao prepare, {}", ti);
                        new XunBaoModeJob().prepare();
                        break;
                    case QI_BING_XUN_BAO:
                        log.info("Do xun bao, {}", ti);
                        new XunBaoModeJob().execute();
                        break;
                    default:
                        log.info("Do task, {}", ti);
                        new OrderModeJob().doTask(ti.getUserName());
                        break;
                }
            } else {
                Duration duration = Duration.between(LocalDateTime.now(), ti.getExecutableTime());
                log.info("After {} seconds to do job. {}", duration.getSeconds(), ti);

                addNewTrigger(context, Math.abs(duration.getSeconds()));
                break;
            }

            userTasks = status.getUserTasks();
        }
    }

    private void addNewTrigger(JobExecutionContext context, long seconds) {
        Trigger tr = TriggerBuilder.newTrigger().forJob(context.getJobDetail())
                .startAt(DateBuilder.futureDate((int) seconds + 1, DateBuilder.IntervalUnit.SECOND)).build();

        try {
            context.getScheduler().scheduleJob(tr);
        } catch (SchedulerException e) {
            log.info("{}", e);
        }
    }
}
