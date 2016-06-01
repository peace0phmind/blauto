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

    SHEN_SHOU_WU(ShenShouWu.class, 1),

    LIAN_BING_CHANG(LianBingChang.class, 3, 3, 10 * 60),
    SHI_LIAN_DONG(ShiLianDong.class, 2, 4, 45 * 60),
    CHU_ZHENG_YE_GUAI(ChuZheng.class, 1, 1, 8 * 2 * 6 * 60),

    LIE_CHANG_ZHENG_SHOU(LieChang.class, 1),
    LIE_CHANG_DA_GUAI(LieChang.class, 1, -1, 24 * 60 * 60),

    SHOU_GU_JIA_GONG(ShouGuFang.class, 4),
    SHOU_GU_SHOU_LIE(ShouGuFang.class, 0, 0, 8 * 60 * 60),

    XUN_BAO(XunBao.class, 1),
    BUILDING(Building.class, 0, 0, 10 * 60),

    NONG_CHANG_ZHONG_ZHI(NongChang.class, 2, 2, 8 * 60 * 60),
    NONG_CHANG_TOU_CAI(NongChang.class, 1),

    SHENG_YU(ShengYu.class, 0, 0, 10 * 60),

    TIAN_SHEN_QI_DAO(TianSheng.class, 0, 0, 12 * 60 * 60),
    TIAN_SHEN_YUAN_GU(TianSheng.class, 0, 0, 2 * 60 * 60),
    TIAN_SHEN_LUAN_DOU(TianSheng.class, 0, 0, 4 * 60 * 60),

    SHI_CHANG(ShiChang.class, 3, -1),
    SHI_CHANG_CHECK(ShiChang.class, 0, -1, 2 * 60 * 60),

    LIAN_MENG(LianMeng.class, 1),
    LIAN_MENG_GONG_FENG(LianMeng.class, 1),
    LIAN_MENG_NAN_MAN(LianMeng.class, 1),
    LIAN_MENG_FU_LI(LianMeng.class, 1),
    LIAN_MENG_LIAN_MENG_ZHAN(LianMeng.class, 1),
    HAO_YOU(HaoYou.class, 1),

    YING_HUN(YingHun.class, 1),
    RONG_LIAN(YingHun.class, 1),

    JING_JI_CHANG(JingJiChang.class, 10, 20, 10 * 60),
    JING_JI_CHANG_LING_QU(JingJiChang.class, 1),

    SHENG_LING_QUAN_MIAN_FEI(ShengLingQuan.class, 1),
    SHENG_LING_QUAN_XIU_LIAN(ShengLingQuan.class, 1, 1, 24 * 60 * 60),
    SHENG_LING_QUAN_XI_LIAN(ShengLingQuan.class, 0, 0, 2 * 60 * 60),


    YUE_KA(RenWu.class, 1),
    LI_BAO(RenWu.class, 5, 5, 30 * 60),
    SHOU_JI_JIN_BI(RenWu.class, 0, 0, 3 * 60 * 60),
    LIN_QU_REN_WU(RenWu.class, 0),

    MEI_RI_JIANG_LI(JiangLi.class, 2),
    HUO_YUE_DU(JiangLi.class, 0),

    HAI_DI_SHI_JIE(HaiDiShiJie.class, 1, 3),

    SHENG_HUO(ShengHuo.class, 3, 3, 15 * 60),

    QUN_YING_HUI(QunYingHui.class, -1),
    QUN_YING_HUI_BAO_MING(QunYingHui.class, 1),
    QUN_YING_HUI_LING_JIANG(QunYingHui.class, 2),

    SHEN_QI(ShenQi.class, 1),
    SHEN_XIANG_SHENG_JI(ShenQi.class, 3, 3, 8 * 60 * 60);

    /**
     * vip user and task
     */
    private static Map<String, List<Task>> vipUser = new HashMap<String, List<Task>>() {{
        put("peace", Arrays.asList(JING_JI_CHANG, SHI_CHANG, SHI_LIAN_DONG, HAI_DI_SHI_JIE, LIE_CHANG_DA_GUAI, TIAN_SHEN_LUAN_DOU));
        put("peace0ph001", Arrays.asList(SHI_LIAN_DONG, HAI_DI_SHI_JIE, TIAN_SHEN_LUAN_DOU));
    }};
    private int timesPerDay;
    private long finishSecond;
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

    public int getDayLimit(String userName) {
        List<Task> tasks = vipUser.get(userName);
        if (tasks != null && tasks.size() > 0) {
            if (tasks.contains(this)) {
                return masterTimesPerDay;
            }
        }

        return timesPerDay;
    }

    private void init(Class<? extends IDo> iDoClass, int timesPerDay, int masterTimesPerDay, int finishSecond) {
        this.iDoClass = iDoClass;
        this.timesPerDay = timesPerDay;
        this.finishSecond = finishSecond;
        this.masterTimesPerDay = masterTimesPerDay;
    }
}
