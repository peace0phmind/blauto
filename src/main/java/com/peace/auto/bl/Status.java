package com.peace.auto.bl;

import com.google.common.collect.Lists;
import com.peace.auto.bl.task.IDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mind on 3/21/16.
 */
@Slf4j
@Data
public class Status {

    private static final String LOG_FILE = "./log_map.bin";

    private static final List<String> users = Arrays.asList(
            "peace",
            "peace0ph001",
            "peace0ph002",
            "peace0ph004",
            "peace0ph006",
            "peace0ph007",
            "peace0ph008"
    );

    private String currentUser;
    private String wantUser;
    private Map<String, List<DoLog>> logMap = new HashMap<>();

    public static int getUserCount() {
        return users.size();
    }

    public Status() {
        loadObjects();
    }

    public String getNextLoginName() {
        if (currentUser == null && wantUser == null) {
            return users.get(0);
        }

        String user = currentUser == null ? wantUser : currentUser;

        int index = users.indexOf(user);
        if (-1 == index) {
            return users.get(0);
        }

        return users.get((index + 1) % users.size());
    }

    public TaskItem getNextUserTask() {
        ArrayList<Task> tasks = Lists.newArrayList(Task.values());
        List<TaskItem> taskItems = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(5);

        users.forEach(u -> tasks.forEach(t -> {
            // 忽略活跃度和领取任务的任务计算
            if (t == Task.HUO_YUE_DU || t == Task.LIN_QU_REN_WU) {
                return;
            }

            int dayLimit = t.getDayLimit(u);

            if (dayLimit < 0) {
                return;
            }

            if (dayLimit > 0) {
                if (todayFinishCount(t, u) >= dayLimit) {
                    return;
                }
            }

            LocalDateTime executableTime = localDateTime;
            if (t.getFinishSecond() > 0) {
                LocalDateTime lastFinishTime = getLastFinishTime(t, u);
                if (lastFinishTime != null) {
                    executableTime = lastFinishTime;
                }
            }

            if (t == Task.SHENG_HUO) {
                if (LocalTime.now().isBefore(LocalTime.of(14, 0))) {
                    executableTime = localDateTime.withHour(11).withMinute(30);
                } else {
                    executableTime = localDateTime.withHour(20).withMinute(30);
                }
            }

            taskItems.add(new TaskItem(u, t, executableTime));
        }));

        List<TaskItem> sortedTasks = taskItems.stream().sorted((x, y) -> x.getExecutableTime().compareTo(y.getExecutableTime())).collect(Collectors.toList());
        sortedTasks.forEach(x -> log.info("{}", x));
        if (sortedTasks == null) {
            return null;
        }

        return sortedTasks.get(0);
    }

    public List<IDo> getTasks(String userName) {
        List<Class<? extends IDo>> ret = new ArrayList<>();

        Lists.newArrayList(Task.values()).forEach(t -> {
            if (canDo(t, userName)) {
                if (!ret.contains(t.getIDoClass())) {
                    ret.add(t.getIDoClass());
                }
            }
        });

        return ret.stream().map(x -> {
            try {
                return x.newInstance();
            } catch (InstantiationException e) {
                log.info("{}", e);
            } catch (IllegalAccessException e) {
                log.info("{}", e);
            }

            return null;
        }).filter(Objects::nonNull).filter(x -> x.CanDo(this, userName)).collect(Collectors.toList());
    }

    public boolean changeUser(int num) {
        this.currentUser = users.get(num - 1);
        log.info("num: {}, currentUser:{}, wantUser:{}", num, currentUser, wantUser);
        return currentUser.equals(wantUser);
    }

    public void Done(Task task) {
        Done(task, LocalDateTime.now().plusSeconds(task.getFinishSecond()));
    }

    public void Done(Task task, LocalDateTime finishTime) {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        List<DoLog> userLogs = getLogs(currentUser);
        userLogs.add(new DoLog(LocalDateTime.now(), finishTime, task));
        userLogs.removeIf(x -> x.getExecuteTime().isBefore(threeDaysAgo));
        saveObjects();
    }

    public LocalDateTime getLastFinishTime(Task task) {
        return getLastFinishTime(task, currentUser);
    }

    public LocalDateTime getLastFinishTime(Task task, String userName) {
        Optional<DoLog> lastFinishTime = getLogs(userName).stream().filter(x -> x.getTask() == task).sorted((x, y) -> y.getFinishTime().compareTo(x.getFinishTime())).findFirst();
        if (lastFinishTime.isPresent()) {
            return lastFinishTime.get().getFinishTime();
        } else {
            return null;
        }
    }

    public long todayFinishCount(Task task) {
        return todayFinishCount(task, currentUser);
    }

    private long todayFinishCount(Task task, String userName) {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return getLogs(userName).stream().filter(x -> x.getTask() == task && x.getExecuteTime().isAfter(today)).count();
    }

    public boolean canDo(Task task) {
        return canDo(task, currentUser);
    }

    public boolean canDo(Task task, String userName) {
        int dayLimit = task.getDayLimit(userName);

        if (dayLimit < 0) {
            return false;
        }

        if (dayLimit > 0) {
            if (todayFinishCount(task, userName) >= dayLimit) {
                return false;
            }
        }

        if (task.getFinishSecond() != 0) {
            LocalDateTime lastFinishTime = getLastFinishTime(task, userName);
            if (lastFinishTime == null) {
                return true;
            } else {
                return LocalDateTime.now().isAfter(lastFinishTime);
            }
        }

        return true;
    }

    private List<DoLog> getLogs(String userName) {
        List<DoLog> doLogs = logMap.get(userName);
        if (doLogs == null) {
            doLogs = new ArrayList<>();
            logMap.put(userName, doLogs);
        }

        return doLogs;
    }

    private void saveObjects() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LOG_FILE))) {
            oos.writeObject(logMap);
            oos.flush();
        } catch (IOException e) {
            log.error("Save Objects exception: {}", e);
        }
    }

    private void loadObjects() {
        if (Files.exists(Paths.get(LOG_FILE))) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LOG_FILE))) {
                logMap = (Map<String, List<DoLog>>) ois.readObject();
            } catch (FileNotFoundException e) {
                log.error("{}", e);
            } catch (IOException e) {
                log.error("{}", e);
            } catch (ClassNotFoundException e) {
                log.error("{}", e);
            }
        }
    }
}

@Data
@AllArgsConstructor
class DoLog implements Serializable {

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 任务类型
     */
    private Task task;
}

@Data
@AllArgsConstructor
class TaskItem implements Serializable {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 任务类型
     */
    private Task task;

    /**
     * 任务可执行时间
     */
    private LocalDateTime executableTime;
}
