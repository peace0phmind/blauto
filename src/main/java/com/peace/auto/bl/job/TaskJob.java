package com.peace.auto.bl.job;

import java.time.LocalTime;

/**
 * Created by mind on 5/25/16.
 */
public interface TaskJob {
    void execute();

    default boolean isValidTime(LocalTime begin, LocalTime end) {
        LocalTime now = LocalTime.now();
        return now.isAfter(begin) && now.isBefore(end);
    }
}
