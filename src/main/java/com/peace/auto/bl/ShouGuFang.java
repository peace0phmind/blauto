package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class ShouGuFang {
    static public void Do(Region region) {
        String baseDir = Common.BASE_DIR + "shougufang/";

        try {
            region.doubleClick(baseDir + "shougufang.png");
            Thread.sleep(2000L);

            //
            Match shougufang = region.exists(baseDir + "shougufang.png", 0.5);
            if (shougufang != null) {
                region.doubleClick(baseDir + "shougufang.png");
                Thread.sleep(2000L);
            }

            // 兽骨加工
            Match shouhuogupian = region.exists(baseDir + "shouhuogupian.png", 0.5);
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
            }

            // 兽骨狩猎
            region.click(baseDir + "shougushoulie.png");

            Match shuaxin = region.exists(baseDir + "shuaxin.png", 0.5);
            if (shuaxin != null) {
                // 需要进行狩猎
                for (int i = 0; i < 3; i++) {
                    Match daliang = region.exists(baseDir + "daliangniaolong.png", 0.5);
                    if (daliang != null) {
                        log.info("{}", daliang);
                        daliang.below().click(baseDir + "kaishishoulie.png");
                        break;
                    }

                    shuaxin.click();
                    Thread.sleep(2000L);
                }
            }

            region.click(Common.CLOSE);
            Thread.sleep(500L);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
