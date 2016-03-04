package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class ShouGuFang {
    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "shougufang/";

        try {
            screen.doubleClick(baseDir + "shougufang.png");
            Thread.sleep(2000L);

            //
            Match shougufang = screen.exists(baseDir + "shougufang.png", 0.5);
            if (shougufang != null) {
                screen.doubleClick(baseDir + "shougufang.png");
                Thread.sleep(2000L);
            }

            // 兽骨加工
            Match shouhuogupian = screen.exists(baseDir + "shouhuogupian.png", 0.5);
            if (shouhuogupian != null) {
                // 进行幸运翻倍
                Match xinyunfanbei = screen.exists(baseDir + "xinyunfanbei.png", 0.5);
                if (xinyunfanbei != null) {
                    for (int i = 0; i < 3; i++) {
                        xinyunfanbei.click();

                        Match noxinyunfanbei = screen.exists(baseDir + "noxinyunfanbei.png", 0.5);
                        if (noxinyunfanbei != null) {
                            screen.click(Common.QUE_DING);
                            break;
                        }
                    }
                }
            }

            // 兽骨狩猎
            screen.click(baseDir + "shougushoulie.png");

            Match shuaxin = screen.exists(baseDir + "shuaxin.png", 0.5);
            if (shuaxin != null) {
                // 需要进行狩猎
                for (int i = 0; i < 3; i++) {
                    Match daliang = screen.exists(baseDir + "daliangniaolong.png", 0.5);
                    if (daliang != null) {
                        log.info("{}", daliang);
                        daliang.below().click(baseDir + "kaishishoulie.png");
                        break;
                    }

                    shuaxin.click();
                    Thread.sleep(2000L);
                }
            }

            screen.click(Common.CLOSE);
            Thread.sleep(500L);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
