package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/12/16.
 */
public class ShiLianDong extends ZhanBao implements IDo {
    String baseDir = Common.BASE_DIR + "shiliandong/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (!status.canDo(Task.SHI_LIAN_DONG)) {
            return false;
        }

        if (canFight(region, status)) {
            region.click(Common.RI_CHANG);

            Thread.sleep(3000L);

            Match shiliandong = region.exists(baseDir + "shiliandong.png", 10);
            if (shiliandong != null && shiliandong.getScore() > 0.95) {
                shiliandong.click();

                Thread.sleep(3000L);

                // 有刷新则先刷新
                Match shuaxin = region.exists(baseDir + "shuaxindongxue.png");
                if (shuaxin != null) {
                    shuaxin.click();
                    Thread.sleep(500L);

                    region.click(Common.QUE_DING);
                    Thread.sleep(1000L);
                }

                // 进入洞穴
                Match jingrudongxue = region.exists(baseDir + "jingrudongxue.png");
                if (jingrudongxue != null) {
                    jingrudongxue.click();

                    Match wancheng = region.exists(baseDir + "jihuiwancheng.png");
                    if (wancheng != null) {
                        region.click(Common.QU_XIAO);
                    } else {
                        // 进入成功
                        Match zidongzhandou = region.exists(baseDir + "zidongzhandou.png");
                        if (zidongzhandou != null) {
                            zidongzhandou.click();

                            Thread.sleep(500L);

                            region.click(baseDir + "zidongbubing.png");
                            region.click(Common.QUE_DING);

                            Thread.sleep(500L);
                            region.click(baseDir + "quanbubuman.png");
                            region.click(baseDir + "queding.png");

                            status.Done(Task.SHI_LIAN_DONG);
                        }
                    }
                }

                Thread.sleep(2000L);
                region.click(Common.CLOSE);
            }

            Thread.sleep(2000L);
            region.click(Common.CLOSE);

            return true;
        }

        return false;
    }
}
