package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/20/16.
 */
public enum Tasks {

    SHEN_SHOU_WU(new ShenShouWu(), 1),

    LIAN_BING_CHANG(new LianBingChang(), 3),
    SHI_LIAN_DONG(new ShiLianDong(), 2),
    CHU_ZHENG(new ChuZheng(), 1),

    SHOU_GU_FANG(new ShouGuFang(), 3),

    ;



    private IDo iDo;
    private int timesPerDay;

    Tasks(IDo iDo, int timesPerDay) {
        this.timesPerDay = timesPerDay;
        this.iDo = iDo;
    }

    boolean Done(Region region, int times) throws InterruptedException, FindFailed {
        if (times > timesPerDay) {
            return false;
        }

        return iDo.Done(region);
    }
}
