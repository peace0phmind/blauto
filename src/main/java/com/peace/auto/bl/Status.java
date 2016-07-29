package com.peace.auto.bl;

import com.google.common.collect.Lists;
import com.peace.auto.bl.task.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.peace.auto.bl.Task.*;

/**
 * Created by mind on 3/21/16.
 */
@Slf4j
@Data
public class Status {

    private static final Random ROOM_NO_RANDOM = new Random();

    private static final List<String> USERS = Arrays.asList(
            "peace",
            "peace0ph001",
            "peace0ph002",
            "peace0ph004",
            "peace0ph006",
            "peace0ph007",
            "peace0ph008",
            "peace0ph003"
    );

    private static final List<LocalTime> QI_BING_XUN_BAO_TIME = Arrays.asList(LocalTime.of(11, 30, 0), LocalTime.of(13, 53, 30), LocalTime.of(21, 30), LocalTime.of(23, 53, 30));

    private static int XUN_BAO_PREPARE_MINUTES = 15;

    private String currentUser;
    private String wantUser;

    private Connection conn;

    public Status() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blauto", "blauto", "blauto");
            log.info("Connection is auto commit: {}", conn.getAutoCommit());
        } catch (Exception e) {
            log.error("{}", e);
        }
    }

    public static int getUserCount() {
        return USERS.size();
    }

    public static LocalDateTime nextCheck() {
        return LocalDateTime.now().plusMinutes(30);
    }

    public static LocalDateTime nextRefresh() {
        LocalDateTime next = LocalDateTime.now().withMinute(0).withSecond(5).withNano(0);
        return next.plusHours(next.getHour() % 2 == 0 ? 2 : 1);
    }

    public static LocalDateTime nextDayCheck() {
        return LocalDateTime.now().plusDays(1).withHour(0).withMinute(20).withSecond(0);
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
        LocalDateTime localDateTime = dateTime.withHour(0).withMinute(20).withSecond(0);

        USERS.forEach(u -> {
            tasks.forEach(t -> {
                // 忽略活跃度和领取任务的任务计算
                // 忽略市场和农场偷菜的任务计算
                switch (t) {
                    case HUO_YUE_DU:
                    case LIN_QU_REN_WU:
                    case SHI_CHANG:
                    case NONG_CHANG_TOU_CAI:
                    case BUILDING_DUI_LIE:
                        return;
                    default:
                        break;
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
                if (t.getFinishSecond(todayFinishCount(t, u)) > 0) {
                    LocalDateTime lastFinishTime = getLastFinishTime(t, u);
                    if (lastFinishTime != null) {
                        executableTime = lastFinishTime;
                    }
                }

                if (t == Task.SHENG_HUO) {
                    LocalTime localTime = dateTime.toLocalTime();
                    if (!((localTime.isAfter(LocalTime.of(11, 30)) && localTime.isBefore(LocalTime.of(13, 59)))
                            || (localTime.isAfter(LocalTime.of(20, 30)) && localTime.isBefore(LocalTime.of(22, 59))))) {
                        return;
                    }

                    if (dateTime.toLocalTime().isBefore(LocalTime.of(11, 30))) {
                        executableTime = localDateTime.withHour(11).withMinute(30);
                    } else if (dateTime.toLocalTime().isBefore(LocalTime.of(20, 30)) && dateTime.toLocalTime().isAfter(LocalTime.of(14, 0))) {
                        executableTime = localDateTime.withHour(20).withMinute(30);
                    }
                }

                if (t == Task.NONG_CHANG_SHOU_CAI) {
                    executableTime = getLastFinishTime(Task.NONG_CHANG_ZHONG_ZHI, u);
                    if (executableTime == null) {
                        return;
                    }
                }

                if (t == CHU_ZHENG_YE_GUAI || t == LIAN_BING_CHANG || t == SHI_LIAN_DONG || t == CHU_ZHENG_DI_DUI) {
                    Optional<LocalDateTime> first = Arrays.asList(CHU_ZHENG_YE_GUAI,
                            Task.LIAN_BING_CHANG,
                            Task.SHI_LIAN_DONG,
                            Task.CHU_ZHENG_DI_DUI).stream().map(x -> getLastFinishTime(x, u))
                            .filter(Objects::nonNull)
                            .sorted((a, b) -> b.compareTo(a)).findFirst();
                    if (first.isPresent()) {
                        executableTime = first.get();
                    }
                }

                if (t == Task.QI_BING_LING_TU) {
                    if (dateTime.toLocalTime().isBefore(LocalTime.of(12, 0))) {
                        executableTime = dateTime.with(LocalTime.of(5, 0));
                    } else {
                        return;
                    }
                }

                if (t == QI_BING_XUN_BAO) {
//                    int coundIndex = todayFinishCount(QI_BING_XUN_BAO, u);
//                    executableTime = dateTime.with(QI_BING_XUN_BAO_TIME.get(coundIndex));
                    Optional<LocalTime> first = QI_BING_XUN_BAO_TIME.stream().filter(x -> x.isAfter(dateTime.toLocalTime())).findFirst();
                    if (first.isPresent()) {
                        executableTime = dateTime.with(first.get());
                    } else {
                        return;
                    }
                }

                if (t == QI_BING_DUO_BAO) {
                    if (!(dateTime.toLocalTime().isAfter(LocalTime.of(11, 40)) && dateTime.toLocalTime().isBefore(LocalTime.of(13, 55)))) {
                        return;
                    }
                    if (todayFinishCount(Task.QI_BING_DUO_BAO, u) == 0) {
                        executableTime = localDateTime.withHour(13).withMinute(10);
                    }
                }

                if (t == CHU_ZHENG_DI_DUI) {
                    LocalDateTime fightEnd = getLastFinishTime(Task.CHU_ZHENG_DI_DUI_CAN_FIGHT, u);
                    if (!(fightEnd != null && dateTime.isBefore(fightEnd) && dateTime.isAfter(fightEnd.minusDays(2)))) {
                        return;
                    }
                }

                if (t == EXCHANGE_CODE) {
                    List<ExchangeCode> exchangeCodes = getExchangeableCodes(dateTime, u);
                    if (exchangeCodes.size() == 0) {
                        return;
                    }
                    Optional<ExchangeCode> first = exchangeCodes.stream().sorted((x, y) -> x.getBeginTime().compareTo(y.getBeginTime())).findFirst();
                    if (first.isPresent()) {
                        executableTime = first.get().getBeginTime();
                    }
                }

                if (executableTime == null) {
                    log.info("executableTime is null: {}, {}", u, t);
                }
                taskItems.add(new TaskItem(u, t, executableTime));
            });
        });

        return taskItems.stream().sorted((x, y) -> {
            if ((x.getTask() != QI_BING_XUN_BAO && y.getTask() != QI_BING_XUN_BAO)
                    || ((x.getTask() == QI_BING_XUN_BAO && y.getTask() == QI_BING_XUN_BAO))) {
                return x.getExecutableTime().compareTo(y.getExecutableTime());
            } else {
                if (x.getTask() == QI_BING_XUN_BAO) {
                    LocalDateTime xunBaoPrepare = x.getExecutableTime().minusMinutes(XUN_BAO_PREPARE_MINUTES);
                    if (xunBaoPrepare.isBefore(dateTime)) {
                        return -1;
                    } else {
                        return xunBaoPrepare.compareTo(y.getExecutableTime());
                    }
                } else {
                    LocalDateTime xunBaoPrepare = y.getExecutableTime().minusMinutes(XUN_BAO_PREPARE_MINUTES);
                    if (xunBaoPrepare.isBefore(dateTime)) {
                        return 1;
                    } else {
                        return x.getExecutableTime().compareTo(xunBaoPrepare);
                    }
                }
            }
        }).collect(Collectors.toList());
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
        Done(task, LocalDateTime.now().plusSeconds(task.getFinishSecond(todayFinishCount(task, currentUser))));
    }

    public void Done(Task task, LocalDateTime finishTime) {
        Done(task, finishTime, currentUser);
    }

    public void Done(Task task, LocalDateTime finishTime, String userName) {
        String insertSql = "INSERT INTO do_log(user_name, task_type, execute_time, finish_time) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            int i = 1;
            stmt.setString(i++, userName);
            stmt.setString(i++, task.toString());
            stmt.setTimestamp(i++, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(i++, Timestamp.valueOf(finishTime));

            stmt.execute();
        } catch (SQLException e) {
            log.error("{}", e);
        }
    }

    public void CodeExchanged(int codeId, String userName) {
        String insertSql = "INSERT INTO exchange_code_log(code_id, user_name, execute_time) VALUES(?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            int i = 1;
            stmt.setInt(i++, codeId);
            stmt.setString(i++, userName);
            stmt.setTimestamp(i++, Timestamp.valueOf(LocalDateTime.now()));

            stmt.execute();
        } catch (SQLException e) {
            log.error("{}", e);
        }
    }

    public List<ExchangeCode> getExchangeableCodes(LocalDateTime dateTime, String userName) {
        String selectSql = "SELECT ec.id, ec.code, ec.begin_time, ec.end_time, ecl.execute_time FROM exchange_code ec " +
                "LEFT JOIN exchange_code_log ecl ON ec.id = ecl.code_id AND ecl.user_name = ? " +
                "WHERE ? < ec.end_time AND ecl.execute_time IS NULL";

        List<ExchangeCode> ret = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            int i = 1;
            stmt.setString(i++, userName);
            stmt.setTimestamp(i++, Timestamp.valueOf(dateTime));

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    ret.add(new ExchangeCode(
                            resultSet.getInt("id"),
                            resultSet.getString("code"),
                            getLocalDateTime(resultSet.getTimestamp("begin_time")),
                            getLocalDateTime(resultSet.getTimestamp("end_time")),
                            getLocalDateTime(resultSet.getTimestamp("execute_time"))
                    ));
                }
            }
        } catch (SQLException e) {
            log.error("{}", e);
        }

        return ret;
    }

    public LocalDateTime getLastFinishTime(Task task) {
        return getLastFinishTime(task, currentUser);
    }

    public LocalDateTime getLastFinishTime(Task task, String userName) {
        String sql = "SELECT MAX(finish_time) FROM do_log WHERE user_name = ? AND task_type = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, userName);
            stmt.setString(i++, task.toString());


            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp timestamp = resultSet.getTimestamp(1);
                    if (timestamp != null) {
                        return timestamp.toLocalDateTime();
                    }
                }
            }
        } catch (SQLException e) {
            log.error("{}", e);
        }

        return null;
    }

    public long todayFinishCount(Task task) {
        return todayFinishCount(task, currentUser);
    }

    public int todayFinishCount(Task task, String userName) {
        String sql = "SELECT COUNT(1) FROM do_log WHERE user_name = ? AND task_type = ? AND execute_time > ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, userName);
            stmt.setString(i++, task.toString());
            stmt.setTimestamp(i++, Timestamp.valueOf(LocalDate.now().atStartOfDay()));

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            log.error("{}", e);
        }

        return 0;
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

        if (task.getFinishSecond(todayFinishCount(task, userName)) != 0) {
            LocalDateTime lastFinishTime = getLastFinishTime(task, userName);
            if (lastFinishTime == null) {
                return true;
            } else {
                return LocalDateTime.now().isAfter(lastFinishTime);
            }
        }

        return true;
    }

    private LocalDateTime getLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }
}
