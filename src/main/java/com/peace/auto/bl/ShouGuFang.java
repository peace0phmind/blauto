package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class ShouGuFang implements IDo {
    private String baseDir = Common.BASE_DIR + "shougufang/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        // 进入兽骨坊
        region.doubleClick(baseDir + "shougufang.png");
        Match inshougufang = region.exists(baseDir + "inshougufang.png", 10);
        if (inshougufang == null) {

            Match shougufang = region.exists(baseDir + "shougufang.png", 0.5);
            if (shougufang != null) {
                region.doubleClick(baseDir + "shougufang.png");
                inshougufang = region.exists(baseDir + "inshougufang.png", 10);
                if (inshougufang == null) {
                    return;
                }
            }
        }

        // 兽骨加工
        shougujiagong(region);

        // 兽骨狩猎
        region.click(baseDir + "shougushoulie.png");

        Match shuaxin = region.exists(baseDir + "shuaxin.png", 0.5);
        if (shuaxin != null) {
            // 需要进行狩猎
            shoulie:
            for (int i = 0; i < 20; i++) {
                Match daliang = region.exists(baseDir + "daliangniaolong.png");
                if (daliang != null) {
                    Iterator<Match> all = region.findAll(baseDir + "daliangniaolong.png");
                    while (all.hasNext()) {
                        Match liewu = all.next();
                        if (liewu.getScore() > 0.95) {
                            Color pixelColor = getPixelColor(liewu, 54, 5);
                            // [r=211,g=83,b=218] 大量
                            // [r=216,g=202,b=153] 小量
                            // [r=73,g=55,b=35] 一群
                            if (pixelColor.getRed() > 200 && pixelColor.getGreen() < 90) {
                                liewu.below().click(baseDir + "kaishishoulie.png");
                                Thread.sleep(1000L);
                                break shoulie;
                            }
                        }
                    }
                }

                shuaxin.click();
                Thread.sleep(2000L);
            }
        }

        region.click(Common.CLOSE);
    }

    /**
     * 兽骨加工
     *
     * @param region
     * @throws FindFailed
     */
    private void shougujiagong(Region region) throws FindFailed {
        Match shouhuogupian = region.exists(baseDir + "shouhuogupian.png", 3);
        if (shouhuogupian != null) {
            // 进行幸运翻倍
            Match xinyunfanbei = region.exists(baseDir + "xinyunfanbei.png", 0.5);
            if (xinyunfanbei != null) {
                for (int i = 0; i < 3; i++) {
                    xinyunfanbei.click();

                    Match noxinyunfanbei = region.exists(baseDir + "noxinyunfanbei.png", 0.5);
                    if (noxinyunfanbei != null) {
                        region.click(Common.QUE_DING);
                        break;
                    }
                }
            }
        } else {
            Match jintigudao = region.exists(baseDir + "jintigudao.png", 3);
            if (jintigudao != null) {
                jintigudao.click();

                Match goumaijindao = region.exists(baseDir + "goumaijindao.png", 3);
                if (goumaijindao != null) {
                    region.click(Common.QU_XIAO);
                } else {
                    shougujiagong(region);
                }
            }
        }
    }
}
