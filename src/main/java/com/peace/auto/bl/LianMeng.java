package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class LianMeng {

    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "lianmeng/";

        try {
            screen.click(Common.MENU);

            Match lianmeng = screen.exists(baseDir + "lianmeng.png", 1);
            if (lianmeng != null) {
                lianmeng.click();

                // 供奉
                Match lianmenggongfeng = screen.exists(baseDir + "lianmenggongfeng.png", 30);
                if (lianmenggongfeng != null) {
                    lianmenggongfeng.click();

                    Match shuijin = screen.exists(baseDir + "shuijin.png", 3);
                    if (shuijin != null) {
                        shuijin.below().click(baseDir + "gongfeng.png");
                        screen.click(Common.CLOSE);
                    }
                }

                // 南蛮
                Match nanman = screen.exists(baseDir + "nanman.png", 3);
                if (nanman != null) {
                    nanman.click();

                    Match baoming = screen.exists(baseDir + "baoming.png", 3);
                    if (baoming != null) {
                        baoming.click();
                    }

                    screen.click(Common.CLOSE);
                }

                // 福利
                Match fuli = screen.exists(baseDir + "fuli.png", 3);
                if (fuli != null) {
                    fuli.click();

                    Match lingqu = screen.exists(baseDir + "lingqu.png", 3);
                    if (lingqu != null) {
                        lingqu.click();
                    }
                }
            }

            screen.click(Common.CLOSE);
            Thread.sleep(500L);

            screen.click(Common.MENU1);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
