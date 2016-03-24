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
        // TODO add mianfei
        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match shenglingquan = region.exists(baseDir + "shenglingquan.png", 30);
        if (shenglingquan != null) {
            shenglingquan.click();

            // 神灵泉
            putongxilian(region);

//            if (isTodayFirstFinished()) {
//                // 当天免费
//
//                putongxilian(region);
//            }

            // 高级修炼
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
                    }
                }
            }

            region.click(Common.CLOSE);
            Thread.sleep(500L);
        }

        region.click(Common.CLOSE);

        return true;
    }

    private void putongxilian(Region region) throws FindFailed {
        Match shengpin = region.exists(baseDir + "shengpin");
        while (shengpin != null && shengpin.getScore() > 0.95) {
            region.click(baseDir + "putongxiulian.png");
            Match end = region.exists(baseDir + "xilianend.png", 1);
            if (end != null) {
                break;
            }
            shengpin = region.exists(baseDir + "shengpin", 3);
        }
    }
}
