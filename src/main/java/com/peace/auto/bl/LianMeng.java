package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class LianMeng {

    static public void Do(Region region) {
        String baseDir = Common.BASE_DIR + "lianmeng/";

        try {
            region.click(Common.MENU);

            Match lianmeng = region.exists(baseDir + "lianmeng.png", 1);
            if (lianmeng != null) {
                lianmeng.click();

                // 供奉
                Match lianmenggongfeng = region.exists(baseDir + "lianmenggongfeng.png", 30);
                if (lianmenggongfeng != null) {
                    lianmenggongfeng.click();

                    Match shuijin = region.exists(baseDir + "shuijin.png", 3);
                    if (shuijin != null) {
                        shuijin.below().click(baseDir + "gongfeng.png");
                        region.click(Common.CLOSE);
                    }
                }

                // 南蛮
                Match nanman = region.exists(baseDir + "nanman.png", 3);
                if (nanman != null) {
                    nanman.click();

                    Match baoming = region.exists(baseDir + "baoming.png", 3);
                    if (baoming != null) {
                        baoming.click();
                    }

                    region.click(Common.CLOSE);
                }

                // 福利
                Match fuli = region.exists(baseDir + "fuli.png", 3);
                if (fuli != null) {
                    fuli.click();

                    Match lingqu = region.exists(baseDir + "lingqu.png", 3);
                    if (lingqu != null) {
                        lingqu.click();
                    }
                }
            }

            Thread.sleep(2000L);

            region.click(Common.CLOSE);
            Thread.sleep(500L);

            region.click(Common.MENU1);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
