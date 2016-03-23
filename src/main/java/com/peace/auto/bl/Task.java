package com.peace.auto.bl;

import lombok.Getter;

/**
 * Created by mind on 3/20/16.
 */
@Getter
public enum Task {

    SHEN_SHOU_WU_WEI_SHI(1),

    CHU_ZHENG_YE_GUAI(1),

    JING_JI_CHANG(10, 10 * 60, 20),;

    private int timesPerDay;
    private long finishSecond;
    private int masterTimesPerDay;

    Task(int timesPerDay) {
        init(timesPerDay, 0, timesPerDay);
    }

    Task(int timesPerDay, int finishSecond) {
        init(timesPerDay, finishSecond, timesPerDay);
    }

    Task(int timesPerDay, int finishSecond, int masterTimesPerDay) {
        init(timesPerDay, finishSecond, masterTimesPerDay);
    }

    private void init(int timesPerDay, int finishSecond, int masterTimesPerDay) {
        this.timesPerDay = timesPerDay;
        this.finishSecond = finishSecond;
        this.masterTimesPerDay = masterTimesPerDay;
    }
}
