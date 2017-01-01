package com.peace.auto.bl;

import com.peace.auto.bl.task.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mind on 3/20/16.
 */
@Slf4j
@Getter
public enum Task {

    SHEN_SHOU_WU(ShenShouWu.class, 11, 20),

    EXCHANGE_CODE(ExchangeCodeTask.class, Task.DO_INFINITY),

    CLEAR_MAIL(Mail.class, 3, 3, 6 * 60 * 60),

    TIAN_TI_ZHAN_DOU(TianTi.class, 10),
    TIAN_TI_LING_QU(TianTi.class, 1),

    LIAN_BING_CHANG(LianBingChang.class, 3, 3, 2 * 10),
    SHI_LIAN_DONG(ShiLianDong.class, 2, 4, 45 * 60),
    CHU_ZHENG_YE_GUAI(ChuZheng.class, 1, 1, 6 * 2 * 10),
    CHU_ZHENG_DI_DUI(ChuZheng.class, Task.NOT_TO_DO, -6, 2 * 20 * 60),
    CHU_ZHENG_DI_DUI_CHECK(ChuZheng.class, Task.NOT_TO_DO, Task.NOT_TO_DO, 30 * 60),
    CHU_ZHENG_DI_DUI_CAN_FIGHT(ChuZheng.class, Task.NOT_TO_DO, Task.NOT_TO_DO),  // a tag
    SONG_HUA(ChuZheng.class, 1, Task.NOT_TO_DO),

    LIE_CHANG_ZHENG_SHOU(LieChang.class, Task.NOT_TO_DO),
    LIE_CHANG_QIANG_ZHENG(LieChang.class, 3),
    LIE_CHANG_DA_GUAI(LieChang.class, Task.DO_INFINITY, Task.NOT_TO_DO, 24 * 60 * 60),

    SHOU_GU_JIA_GONG(ShouGuFang.class, 4),
    SHOU_GU_SHOU_LIE(ShouGuFang.class, Task.DO_INFINITY, Task.DO_INFINITY, 8 * 60 * 60),

    XUN_BAO(XunBao.class, 1, Task.NOT_TO_DO),
    //    BUILDING(Building.class,Task.DO_INFINITY,Task.DO_INFINITY, 30 * 60),
    BUILDING(Building.class, Task.NOT_TO_DO, Task.NOT_TO_DO, 30 * 60),
    //    BUILDING_DUI_LIE(Building.class,Task.DO_INFINITY,Task.DO_INFINITY),
    BUILDING_DUI_LIE(Building.class, Task.NOT_TO_DO, Task.NOT_TO_DO),

    NONG_CHANG_ZHONG_ZHI(NongChang.class, 2, 2, 4 * 60 * 60),
    NONG_CHANG_SHOU_CAI(NongChang.class, 2),
    NONG_CHANG_TOU_CAI(NongChang.class, 1),
    NONG_CHANG_TOU_CAI_CHECK(NongChang.class, Task.DO_INFINITY, Task.DO_INFINITY, 2 * 60 * 60),

    SHENG_YU(ShengYu.class, Task.DO_INFINITY, Task.DO_INFINITY, 30 * 60),

    TIAN_SHEN_QI_DAO(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 12 * 60 * 60),
    TIAN_SHEN_QI_DAO_GAO_JI(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 24 * 60 * 60),
    TIAN_SHEN_YUAN_GU(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 4 * 60 * 60),
    TIAN_SHEN_LUAN_DOU(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 4 * 60 * 60),

    TIAN_SHEN_HUO_QU_SHEN_HUN(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 8 * 60 * 60),
    TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 48 * 60 * 60),
    TIAN_SHEN_HUO_QU_SHEN_JI(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 8 * 60 * 60),
    TIAN_SHEN_HUO_QU_SHEN_JI_GAO_JI(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 48 * 60 * 60),
    TIAN_SHEN_HUN_JIE(TianSheng.class, Task.DO_INFINITY, Task.DO_INFINITY, 6 * 60 * 60),
    TIAN_SHEN_HUN_JIE_GOU_MAI(TianSheng.class, 2),

    SHI_CHANG(ShiChang.class, 3, Task.NOT_TO_DO),
    SHI_CHANG_CHECK(ShiChang.class, Task.DO_INFINITY, Task.NOT_TO_DO, 2 * 60 * 60),

    LIAN_MENG_GONG_FENG(LianMeng.class, 1),
    LIAN_MENG_NAN_MAN(LianMeng.class, -1, -1),
    LIAN_MENG_FU_LI(LianMeng.class, 1),
    LIAN_MENG_LIAN_MENG_ZHAN(LianMeng.class, -1, -1),
    HAO_YOU(HaoYou.class, 1),

    YING_HUN(YingHun.class, 1),
    RONG_LIAN(YingHun.class, 1, Task.NOT_TO_DO),

    JING_JI_CHANG(JingJiChang.class, 12, 20, 10 * 60),
    JING_JI_CHANG_LING_QU(JingJiChang.class, 1),

    SHENG_LING_QUAN_MIAN_FEI(ShengLingQuan.class, 1),
    SHENG_LING_QUAN_XIU_LIAN(ShengLingQuan.class, Task.DO_INFINITY, Task.DO_INFINITY, 24 * 60 * 60),
    SHENG_LING_QUAN_XI_LIAN(ShengLingQuan.class, Task.DO_INFINITY, Task.DO_INFINITY, 2 * 60 * 60),

    MEI_RI_JIANG_LI(JiangLi.class, 2),
    HUO_YUE_DU(JiangLi.class, Task.DO_INFINITY),
    ZHEN_QING_HUI_KUI(JiangLi.class, Task.DO_INFINITY, Task.DO_INFINITY, 1 * 60 * 60),

    HAI_DI_SHI_JIE(HaiDiShiJie.class, Task.NOT_TO_DO),
    //    HAI_DI_SHI_JIE_SAO_DANG(HaiDiShiJie.class, 1, 3),
    HAI_DI_SHI_JIE_SAO_DANG(HaiDiShiJie.class, Task.NOT_TO_DO, -2),
    HAI_DI_SHI_JIE_TIAO_ZHAN(HaiDiShiJie.class, Task.NOT_TO_DO),

    SHENG_HUO(ShengHuo.class, 3, 3, 15 * 60),

    QUN_YING_HUI_BAO_MING(QunYingHui.class, 1),
    QUN_YING_HUI_LING_JIANG(QunYingHui.class, 1),

    SHEN_QI(ShenQi.class, 1),
    SHEN_XIANG_SHENG_JI(ShenQi.class, Task.DO_INFINITY, Task.DO_INFINITY, 8 * 60 * 60),

    YUE_KA(RenWu.class, 1),
    LI_BAO(RenWu.class, 5, 5, Arrays.asList(30 * 60, 30 * 60, 60 * 60, 60 * 60)),
    SHOU_JI_JIN_BI(RenWu.class, Task.DO_INFINITY, Task.DO_INFINITY, 3 * 60 * 60),
    LIN_QU_REN_WU(RenWu.class, Task.DO_INFINITY),
    LIN_QU_BAO_XIANG(RenWu.class, 1),
    LIN_QU_REN_WU_UNKNOWN(RenWu.class, 1),

    CLEAN_BAG(Bag.class, Task.DO_INFINITY),

    QI_BING_XUN_BAO(DuoBao.class, 3, 3, 5 * 60),
    QI_BING_DUO_BAO(DuoBao.class, Task.NOT_TO_DO, 3, 10 * 60),
    //    QI_BING_DUO_BAO(DuoBao.class, Task.NOT_TO_DO, 3, 10 * 60),
    QI_BING_LING_TU(DuoBao.class, Task.NOT_TO_DO);
//    QI_BING_LING_TU(DuoBao.class, 1);

    private static final int NOT_TO_DO = -1;
    private static final int DO_INFINITY = 0;


    /**
     * vip user and task
     */
    private static Map<String, List<Task>> vipUser = new HashMap<String, List<Task>>() {{
        put(Status.peaceName(), Arrays.asList(CHU_ZHENG_DI_DUI, CHU_ZHENG_DI_DUI_CHECK, JING_JI_CHANG, SHI_LIAN_DONG,
                SHI_CHANG, SHI_CHANG_CHECK, SHEN_SHOU_WU,
                HAI_DI_SHI_JIE_SAO_DANG, LIE_CHANG_DA_GUAI, SONG_HUA,
                BUILDING_DUI_LIE, BUILDING,

//              QI_BING_DUO_BAO,
                LIAN_MENG_NAN_MAN, LIAN_MENG_FU_LI, LIAN_MENG_LIAN_MENG_ZHAN));
        put("peace0ph001", Arrays.asList(SHI_LIAN_DONG, HAI_DI_SHI_JIE_SAO_DANG, BUILDING_DUI_LIE, BUILDING,

//              QI_BING_DUO_BAO,
                LIAN_MENG_NAN_MAN, LIAN_MENG_FU_LI, LIAN_MENG_LIAN_MENG_ZHAN));
    }};

    private int timesPerDay;
    private List<Integer> finishSeconds;
    private int masterTimesPerDay;
    private Class<? extends IDo> iDoClass;

    Task(Class<? extends IDo> iDoClass, int timesPerDay) {
        init(iDoClass, timesPerDay, timesPerDay, 0);
    }


    Task(Class<? extends IDo> iDoClass, int timesPerDay, int masterTimesPerDay) {
        init(iDoClass, timesPerDay, masterTimesPerDay, 0);
    }

    Task(Class<? extends IDo> iDoClass, int timesPerDay, int masterTimesPerDay, int finishSecond) {
        init(iDoClass, timesPerDay, masterTimesPerDay, finishSecond);
    }

    Task(Class<? extends IDo> iDoClass, int timesPerDay, int masterTimesPerDay, List<Integer> finishSeconds) {
        init(iDoClass, timesPerDay, masterTimesPerDay, finishSeconds);
    }

    public int getDayLimit(String userName) {
        List<Task> tasks = vipUser.get(userName);
        if (tasks != null && tasks.size() > 0) {
            if (tasks.contains(this)) {
                return masterTimesPerDay;
            }
        }

        return timesPerDay;
    }

    public int getFinishSecond(int count) {
        if (finishSeconds.size() > 1 && count < finishSeconds.size()) {
            return finishSeconds.get(count);
        }

        return finishSeconds.get(0);
    }

    private void init(Class<? extends IDo> iDoClass, int timesPerDay, int masterTimesPerDay, int finishSecond) {
        init(iDoClass, timesPerDay, masterTimesPerDay, Arrays.asList(finishSecond));
    }

    private void init(Class<? extends IDo> iDoClass, int timesPerDay, int masterTimesPerDay, List<Integer> finishSeconds) {
        this.iDoClass = iDoClass;
        this.timesPerDay = timesPerDay;
        this.finishSeconds = finishSeconds;
        this.masterTimesPerDay = masterTimesPerDay;
    }
}
