package com.peace.auto.bl.job;

import com.peace.auto.bl.Task;
import com.peace.auto.bl.TaskItem;
import com.peace.auto.bl.task.DuoBao;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.peace.auto.bl.common.Devices.*;

/**
 * Created by mind on 5/28/16.
 */
@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class AutoMode implements Job {

    private static final String XUN_BAO_KEY = Task.QI_BING_XUN_BAO.toString();

    public static void init(Scheduler scheduler) {
        JobDetail job = JobBuilder.newJob(AutoMode.class).usingJobData(XUN_BAO_KEY, false).build();
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

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        if (jobDataMap.getBoolean(XUN_BAO_KEY)) {
            log.info("Do xun bao");
            new XunBaoModeJob().xunbao();
            jobDataMap.put(XUN_BAO_KEY, false);
            System.exit(-1);
        }

        while (true) {
            List<TaskItem> userTasks = status.getUserTasks();
            if (userTasks == null || userTasks.size() <= 0) {
                log.error("Task is empty {}.", userTasks);
                break;
            }

            TaskItem ti;
            Optional<TaskItem> first = userTasks.stream().filter(x -> x.getTask() == Task.QI_BING_DUO_BAO).findFirst();
            if (first.isPresent()) {
                ti = first.get();
            } else {
                ti = userTasks.get(0);
            }

            if (LocalDateTime.now().isAfter(ti.getExecutableTime())) {
                switch (ti.getTask()) {
                    case QI_BING_LING_TU:
                        log.info("Do ling tu, {}", ti);
                        lingTu("peace", "peace0ph001");
                        lingTu("peace0ph002", "peace0ph003");
                        lingTu("peace0ph004", "peace0ph006");
                        lingTu("peace0ph007", "peace0ph008");
                        System.exit(-1);
                        break;
                    case QI_BING_XUN_BAO:
                        log.info("Do xun bao, {}", ti);
//                        new XunBaoModeJob().execute();
                        break;
                    case QI_BING_DUO_BAO:
                        log.info("Do duo bao, {}", ti);
                        new DuoBaoModeJob().duobao(ti.getUserName());
                        if (status.todayFinishCount(Task.QI_BING_DUO_BAO, status.peaceName()) == 3
                                && status.todayFinishCount(Task.QI_BING_DUO_BAO, "peace0ph001") == 3) {
                            System.exit(-1);
                        }
                        break;
                    default:
                        log.info("Do task, {}", ti);
                        new OrderModeJob().doTask(ti.getUserName());
                        break;
                }
            } else {
                if (ti.getTask() == Task.QI_BING_XUN_BAO) {
                    log.info("Prepare xun bao, {}", ti);
                    new XunBaoModeJob().prepare();
                    jobDataMap.put(XUN_BAO_KEY, true);
                } else {
                    try {
                        DENG_LU.checkUser(DEVICE_1.getRegion(), status, ti.getUserName());
                    } catch (Exception e) {
                        log.info("{}", e);
                    }
                }

                Duration duration = Duration.between(LocalDateTime.now(), ti.getExecutableTime());
                long delaySeconds = duration.getSeconds();
                if (delaySeconds <= 0) {
                    log.info("Time delay is negative {}. {}", delaySeconds, ti);
                    continue;
                }

                addNewTrigger(context, delaySeconds);
                log.info("After {} seconds to do job. {}", delaySeconds, ti);
                break;
            }
        }
    }

    private void lingTu(String user1, String user2) {
        if (!status.canDo(Task.QI_BING_LING_TU, user1) || !status.canDo(Task.QI_BING_LING_TU, user2)) {
            return;
        }

        try {
            AndroidScreen region1 = DEVICE_1.getRegion();
            AndroidScreen region2 = DEVICE_2.getRegion();

            DENG_LU.checkUser(region1, status, user1);
            DENG_LU.checkUser(region2, status, user2);

            new DuoBao().xunbao(region1, region2, true, status.getRoomNo(), null);

            status.Done(Task.QI_BING_LING_TU, LocalDateTime.now(), user1);
            status.Done(Task.QI_BING_LING_TU, LocalDateTime.now(), user2);
        } catch (Exception e) {
            log.error("{}", e);

            try {
                DEVICE_1.stopDevice();
                DEVICE_2.stopDevice();
            } catch (Exception e1) {
                log.error("{}", e1);
            }

            System.exit(-1);
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
