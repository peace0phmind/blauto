package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class LianMeng implements IDo {
    String baseDir = Common.BASE_DIR + "lianmeng/";

    public boolean Done(Region region) throws FindFailed, InterruptedException {
        if (isTodayFirstFinished()) {
            return false;
        }

        region.click(Common.MENU);

        Match lianmeng = region.exists(baseDir + "lianmeng.png", 3);
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

                // 领取奖励
                Match lingqujiangli = region.exists(baseDir + "lingqujiangli.png");
                if (lingqujiangli != null) {
                    lingqujiangli.click();
                    Thread.sleep(500L);
                }

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
                // 每天领取一次福利,并且进行一次捐赠
                if (lingqu != null && isButtonEnable(lingqu, 5, 5)) {
                    lingqu.click();

                    Match juanxian = region.exists(baseDir + "juanxian.png");
                    if (juanxian != null) {
                        juanxian.click();

                        Match renli = region.exists(baseDir + "renliinput.png");
                        if (renli != null) {
                            renli.type("1");

                            region.click(baseDir + "juanxiananniu.png");
                            Thread.sleep(500L);
                        }
                    }
                }
            }
        }

        Thread.sleep(2000L);

        region.click(Common.CLOSE);
        Thread.sleep(500L);

        region.click(Common.MENU1);

        return true;
    }
}
