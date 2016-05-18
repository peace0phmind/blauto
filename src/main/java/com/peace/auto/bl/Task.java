package com.peace.auto.bl;

import lombok.Getter;

/**
 * Created by mind on 3/20/16.
 */
@Getter
public enum Task {

//    DENG_LU(0, 0, 5 * 60),

    QUN_YING_HUI(2),

    NONG_CHANG_ZHONG_ZHI(2, 2, 8 * 60 * 60),

    NONG_CHANG_SHOU_HUO(-1),

    NONG_CHANG_TOU_CAI(1),

    TIAN_SHEN_QI_DAO(0, 0, 12 * 60 * 60),

    TIAN_SHEN_YUAN_GU(0, 0, 1 * 60 * 60),

    SHENG_HUO(3, 3, 15 * 60),

    YUE_KA(1),

    SHOU_GU_JIA_GONG(6),

    SHOU_GU_SHOU_LIE(0, 0, 8 * 60 * 60),

    XUN_BAO(1),

    LIE_CHANG_ZHENG_SHOU(1),

    LIE_CHANG_DA_GUAI(1, -1),

    MEI_RI_JIANG_LI(2),

    HAO_YOU(1),

    CHU_ZHENG_YE_GUAI(1, 1, 8 * 2 * 6 * 60),

    JING_JI_CHANG(10, 20, 10 * 60),

    LIAN_BING_CHANG(3, 3, 10 * 60),

    SHI_CHANG(3, -1),

    //    SHI_LIAN_DONG(2, 4, 45 * 60),
    SHI_LIAN_DONG(2, -1, 45 * 60),

    SHEN_SHOU_WU(1),

    LIAN_MENG(1),

    YING_HUN(1),

    RONG_LIAN(1),

    SHENG_LING_QUAN_MIAN_FEI(1),

    SHENG_LING_QUAN_XIU_LIAN(1, 1, 24 * 60 * 60),

    SHENG_LING_QUAN_XI_LIAN(0, 0, 1 * 60 * 60),

    HAI_DI_SHI_JIE(1, 3),

    SHEN_QI(1),

    SHEN_XIANG_SHENG_JI(3, 3, 8 * 60 * 60);

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
