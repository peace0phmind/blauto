package com.peace.auto.bl;

import lombok.Getter;

/**
 * Created by mind on 3/20/16.
 */
@Getter
public enum Task {

    XUN_BAO(1),

    LIE_CHANG_ZHENG_SHOU(1),

    LIE_CHANG_DA_GUAI(1),

    MEI_RI_JIANG_LI(1),

    HAO_YOU(1),

    SHEN_SHOU_WU_WEI_SHI(1),

    CHU_ZHENG_YE_GUAI(1),

    JING_JI_CHANG(10, 10 * 60, 20),

    LIAN_BING_CHANG(3, 10 * 60),

    SHI_CHANG(3),

    SHI_LIAN_DONG(2, 20 * 60, 0),

    SHEN_SHOU_WU(1),

    LIAN_MENG(1),

    YING_HUN(1);

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
