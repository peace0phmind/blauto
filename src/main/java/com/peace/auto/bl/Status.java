package com.peace.auto.bl;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mind on 3/21/16.
 */
@Slf4j
@Data
public class Status {

    private static final String LOG_FILE = "./log_map.bin";

    private static final Map<Integer, String> users = new HashMap<Integer, String>() {{
        put(1, "peace");
        put(2, "peace0ph001");
        put(3, "peace0ph002");
        put(4, "peace0ph004");
        put(5, "peace0ph006");
        put(6, "peace0ph007");
        put(7, "peace0ph008");
    }};

    private String currentUser;
    private String wantUser;
    private Map<String, List<DoLog>> logMap = new HashMap<>();
    private LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
    private LocalDateTime today = LocalDate.now().atStartOfDay();
    private int loginTimes = 0;

    public Status() {
        loadObjects();
    }

    public String getNextLoginName() {
        if (loginTimes == 0) {
            wantUser = "peace";
        }
        log.info("loginTimes: {}", loginTimes);

        ArrayList<Task> tasks = Lists.newArrayList(Task.values());

        Map<String, List<Task>> collect = users.values().stream().collect(Collectors.toMap((u) -> u,
                (u) -> tasks.stream().filter(t -> canDo(t, u)).collect(Collectors.toList())));

        log.info("{}", collect);

        return users.get(loginTimes++ % users.size() + 1);
    }

    public boolean changeUser(int num) {
        this.currentUser = users.get(num);
        log.info("num: {}, currentUser:{}, wantUser:{}", num, currentUser, wantUser);
        return currentUser.equals(wantUser);
    }

    public void Done(Task task) {
        Done(task, LocalDateTime.now().plusSeconds(task.getFinishSecond()));
    }

    public void Done(Task task, LocalDateTime finishTime) {
        List<DoLog> userLogs = getLogs(currentUser);
        userLogs.add(new DoLog(LocalDateTime.now(), finishTime, task));
        userLogs.removeIf(x -> x.getExecuteTime().isBefore(threeDaysAgo));
        saveObjects();
    }

    private LocalDateTime getLastExecuteTime(Task task, String userName) {
        Optional<DoLog> lastTime = getLogs(userName).stream().filter(x -> x.getTask() == task).sorted((x, y) -> y.getExecuteTime().compareTo(x.getExecuteTime())).findFirst();
        if (lastTime.isPresent()) {
            return lastTime.get().getExecuteTime();
        } else {
            return null;
        }
    }

    public LocalDateTime getLastFinishTime(Task task) {
        return getLastFinishTime(task, currentUser);
    }

    private LocalDateTime getLastFinishTime(Task task, String userName) {
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
        return getLogs(userName).stream().filter(x -> x.getTask() == task && x.getExecuteTime().isAfter(today)).count();
    }

    public boolean isMaster() {
        return isMaster(currentUser);
    }

    private boolean isMaster(String userName) {
        return "peace".equals(userName);
    }

    public boolean canDo(Task task) {
        return canDo(task, currentUser);
    }

    private boolean canDo(Task task, String userName) {
        int dayLimit = isMaster(userName) ? task.getMasterTimesPerDay() : task.getTimesPerDay();

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
