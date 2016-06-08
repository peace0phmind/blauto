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

import static com.peace.auto.bl.Task.QI_BING_XUN_BAO;
import static com.peace.auto.bl.Task.QI_BING_XUN_BAO_PREPARE;

/**
 * Created by mind on 3/21/16.
 */
@Slf4j
@Data
public class Status {

    private static final Random ROOM_NO_RANDOM = new Random();
    private static final String LOG_FILE = "./log_map.bin";
    private static final List<String> USERS = Arrays.asList(
            "peace",
            "peace0ph001",
            "peace0ph002",
            "peace0ph004",
            "peace0ph006",
            "peace0ph007",
            "peace0ph008"
    );

//    private static final List<LocalTime> XUN_BAO_PREPARE = Arrays.asList(LocalTime.of(11, 25), LocalTime.of(13, 50), LocalTime.of(21, 25), LocalTime.of(23, 50));
//    private static final List<LocalTime> QI_BING_XUN_BAO = Arrays.asList(LocalTime.of(11, 30), LocalTime.of(13, 53, 30), LocalTime.of(21, 30), LocalTime.of(23, 53, 30));

    private static final List<LocalTime> XUN_BAO_PREPARE_TIME = Arrays.asList(LocalTime.of(13, 43), LocalTime.of(21, 20), LocalTime.of(23, 43));
    private static final List<LocalTime> QI_BING_XUN_BAO_TIME = Arrays.asList(LocalTime.of(13, 53, 30), LocalTime.of(21, 30), LocalTime.of(23, 53, 30));

    private String currentUser;
    private String wantUser;
    private Map<String, List<DoLog>> logMap = new HashMap<>();

    public Status() {
        loadObjects();
    }

    public static int getUserCount() {
        return USERS.size();
    }

    public boolean isPeace() {
        return peaceName().equals(currentUser);
    }

    public String peaceName() {
        return "peace";
    }

    public int getRoomNo() {
        return ROOM_NO_RANDOM.nextInt(36) + 12 + 1;
    }

    public String getNextLoginName() {
        if (currentUser == null && wantUser == null) {
            return USERS.get(0);
        }

        String user = currentUser == null ? wantUser : currentUser;

        int index = USERS.indexOf(user);
        if (-1 == index) {
            return USERS.get(0);
        }

        return USERS.get((index + 1) % USERS.size());
    }

    public List<TaskItem> getUserTasks() {
        return getUserTasks(LocalDateTime.now());
    }

    public List<TaskItem> getUserTasks(LocalDateTime dateTime) {
        ArrayList<Task> tasks = Lists.newArrayList(Task.values());
        List<TaskItem> taskItems = new ArrayList<>();
        LocalDateTime localDateTime = dateTime.minusMinutes(5);

        USERS.forEach(u -> {
            tasks.forEach(t -> {
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

                if (t == Task.SHI_CHANG) {
                    return;
                }

                if (t == Task.SHENG_HUO) {
                    if (dateTime.toLocalTime().isBefore(LocalTime.of(11, 30))) {
                        executableTime = localDateTime.withHour(11).withMinute(30);
                    } else if (dateTime.toLocalTime().isBefore(LocalTime.of(20, 30)) && dateTime.toLocalTime().isAfter(LocalTime.of(14, 0))) {
                        executableTime = localDateTime.withHour(20).withMinute(30);
                    }
                }

                taskItems.add(new TaskItem(u, t, executableTime));
            });

            if (peaceName().equals(u)) {
                long finishCount = todayFinishCount(QI_BING_XUN_BAO_PREPARE, u);
                if (finishCount < 3) {
                    taskItems.add(new TaskItem(u, QI_BING_XUN_BAO_PREPARE, dateTime.with(XUN_BAO_PREPARE_TIME.get((int) finishCount))));
                }

                finishCount = todayFinishCount(QI_BING_XUN_BAO, u);
                if (finishCount < 3) {
                    taskItems.add(new TaskItem(u, QI_BING_XUN_BAO, dateTime.with(QI_BING_XUN_BAO_TIME.get((int) finishCount))));
                }
            }
        });

        List<TaskItem> sortedTasks = taskItems.stream().sorted((x, y) -> {
            if (x.getTask() == QI_BING_XUN_BAO_PREPARE || x.getTask() == QI_BING_XUN_BAO) {
                return x.getExecutableTime().compareTo(dateTime.plusMinutes(20));
            }

            if (y.getTask() == QI_BING_XUN_BAO_PREPARE || y.getTask() == QI_BING_XUN_BAO) {
                return y.getExecutableTime().compareTo(dateTime.plusMinutes(20)) * -1;
            }
            return x.getExecutableTime().compareTo(y.getExecutableTime());
        }).collect(Collectors.toList());
        if (sortedTasks == null) {
            return null;
        }

        return sortedTasks;
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
            } catch (Exception e) {
                log.info("{}", e);
            }
            return null;
        }).filter(Objects::nonNull).filter(x -> x.CanDo(this, userName)).collect(Collectors.toList());
    }

    public boolean changeUser(int num) {
        this.currentUser = USERS.get(num - 1);
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

    public long todayFinishCount(Task task, String userName) {
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

