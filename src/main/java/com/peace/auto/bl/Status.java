package com.peace.auto.bl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by mind on 3/21/16.
 */
@Slf4j
public class Status {

    private static final String LOG_FILE = "./log_map.bin";

    private String currentUser;

    private Map<String, List<DoLog>> logMap = new HashMap<>();

    private LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

    private LocalDateTime today = LocalDate.now().atStartOfDay();

    public Status() {
        loadObjects();
    }

    public void changeUser(String currentUser) {
        this.currentUser = currentUser;
//        this.currentUserEnd = currentUser.substring(currentUser.length());
    }

    public void Done(Task task) {
        List<DoLog> userLogs = getLogs();
        userLogs.add(new DoLog(LocalDateTime.now(), LocalDateTime.now().plusSeconds(task.getFinishSecond()), task));
        userLogs.removeIf(x -> x.getExecuteTime().isBefore(threeDaysAgo));
        saveObjects();
    }

    public LocalDateTime getLastExecuteTime(Task task) {
        Optional<DoLog> lastTime = getLogs().stream().filter(x -> x.getTask() == task).sorted((x, y) -> y.getExecuteTime().compareTo(x.getExecuteTime())).findFirst();
        if (lastTime.isPresent()) {
            return lastTime.get().getExecuteTime();
        } else {
            return null;
        }
    }

    public LocalDateTime getLastFinishTime(Task task) {
        Optional<DoLog> lastFinishTime = getLogs().stream().filter(x -> x.getTask() == task).sorted((x, y) -> y.getFinishTime().compareTo(x.getFinishTime())).findFirst();
        if (lastFinishTime.isPresent()) {
            return lastFinishTime.get().getFinishTime();
        } else {
            return null;
        }
    }

    public long todayFinishCount(Task task) {
        return getLogs().stream().filter(x -> x.getTask() == task && x.getExecuteTime().isAfter(today)).count();
    }

    public boolean canDo(Task task) {
        if (task.getTimesPerDay() > 0) {
            int dayLimit = "l".equals(currentUser) ? task.getMasterTimesPerDay() : task.getTimesPerDay();

            if (todayFinishCount(task) >= dayLimit) {
                return false;
            }
        }

        if (task.getFinishSecond() != 0) {
            LocalDateTime lastFinishTime = getLastFinishTime(task);
            if (lastFinishTime == null) {
                return true;
            } else {
                return LocalDateTime.now().isAfter(lastFinishTime);
            }
        }

        return true;
    }

    private List<DoLog> getLogs() {
        List<DoLog> doLogs = logMap.get(currentUser);
        if (doLogs == null) {
            doLogs = new ArrayList<>();
            logMap.put(currentUser, doLogs);
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
