package com.peace.auto.bl;

import lombok.Getter;

/**
 * Created by mind on 3/20/16.
 */
@Getter
public enum Task {

    TIAN_SHEN_QI_DAO(0, 0, 1 * 60 * 60),

    TIAN_SHEN_YUAN_GU(0, 0, 1 * 60 * 60),

    SHENG_HUO(3, -1),

    YUE_KA(1),

    SHOU_GU_JIA_GONG(3),

    SHOU_GU_SHOU_LIE(0, 0, 8 * 60 * 60),

    XUN_BAO(1, -1),

    LIE_CHANG_ZHENG_SHOU(1),

    LIE_CHANG_DA_GUAI(1, -1),

    MEI_RI_JIANG_LI(1),

    HAO_YOU(1),

    CHU_ZHENG_YE_GUAI(1, -1, 8 * 2 * 6 * 60),

    JING_JI_CHANG(10, 20, 10 * 60),

    LIAN_BING_CHANG(3, -1, 10 * 60),

    SHI_CHANG(3, -1),

    SHI_LIAN_DONG(2, -1, 20 * 60),

    SHEN_SHOU_WU(1),

    LIAN_MENG(1),

    YING_HUN(1),

    SHENG_LING_QUAN(0, -1),

    SHENG_LING_QUAN_MIAN_FEI(1),

    SHENG_LING_QUAN_XIU_LIAN(1, 1, 24 * 60 * 60),

    SHENG_LING_QUAN_XI_LIAN(0, 0, 2 * 60 * 60);

    private int timesPerDay;
    private long finishSecond;
    private int masterTimesPerDay;

    Task(int timesPerDay) {
        init(timesPerDay, timesPerDay, 0);
    }

    Task(int timesPerDay, int masterTimesPerDay) {
        init(timesPerDay, masterTimesPerDay, 0);
    }

    Task(int timesPerDay, int masterTimesPerDay, int finishSecond) {
        init(timesPerDay, masterTimesPerDay, finishSecond);
    }

    private void init(int timesPerDay, int masterTimesPerDay, int finishSecond) {
        this.timesPerDay = timesPerDay;
        this.finishSecond = finishSecond;
        this.masterTimesPerDay = masterTimesPerDay;
    }
}
