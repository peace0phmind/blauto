package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class ShengLingQuan implements IDo {
    String baseDir = Common.BASE_DIR + "shenglingquan/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (!status.canDo(Task.SHENG_LING_QUAN_XIU_LIAN) && !status.canDo(Task.SHENG_LING_QUAN_XI_LIAN)) {
            return false;
        }

        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match shenglingquan = region.exists(baseDir + "shenglingquan.png", 30);
        if (shenglingquan != null) {
            shenglingquan.click();

            // 神灵泉
            if (status.canDo(Task.SHENG_LING_QUAN_XI_LIAN)) {
                putongxilian(region);

                if (status.canDo(Task.SHENG_LING_QUAN_MIAN_FEI)) {
                    region.click(baseDir + "dingjishenshui.png");

                    Match zhixingmianfeicaozuo = region.exists(baseDir + "zhixingmianfeicaozuo.png", 6);
                    if (zhixingmianfeicaozuo != null) {
                        Thread.sleep(2000L);
                        Match queding = region.exists(Common.QUE_DING, 10);
                        if (queding != null) {
                            Thread.sleep(2000L);
                            region.click(Common.QUE_DING);
                            status.Done(Task.SHENG_LING_QUAN_MIAN_FEI);
                        }
                        
                        putongxilian(region);
                    }
                }

                status.Done(Task.SHENG_LING_QUAN_XI_LIAN);
            }

            // 高级修炼
            if (status.canDo(Task.SHENG_LING_QUAN_XIU_LIAN)) {
                Match xiulian = region.exists(baseDir + "xiulian.png", 3);
                if (xiulian != null) {
                    xiulian.click();

                    Match kaishixiulian = region.exists(baseDir + "kaishixiulian.png", 3);
                    if (kaishixiulian != null && kaishixiulian.getScore() > 0.95) {
                        Thread.sleep(1000L);
                        region.click(baseDir + "gaojixiulian.png");
                        Thread.sleep(1000L);
                        region.click(baseDir + "kaishixiulian.png");

                        Match goumai = region.exists(baseDir + "goumai.png", 3);
                        if (goumai != null) {
                            region.click(Common.QUE_DING);
                            status.Done(Task.SHENG_LING_QUAN_XIU_LIAN);
                        }
                    }
                }
            }

            region.click(Common.CLOSE);
            Thread.sleep(500L);
        }

        region.click(Common.CLOSE);

        return true;
    }

    private void putongxilian(Region region) throws FindFailed, InterruptedException {
        Match shengpin = region.exists(baseDir + "shengpin");
        while (shengpin != null && shengpin.getScore() > 0.95) {
            region.click(baseDir + "putongxiulian.png");
            Match end = region.exists(baseDir + "xilianend.png", 1);
            if (end != null) {
                Thread.sleep(1000L);
                region.click(Common.QUE_DING);
                break;
            }
            shengpin = region.exists(baseDir + "shengpin", 3);
        }
    }
}
