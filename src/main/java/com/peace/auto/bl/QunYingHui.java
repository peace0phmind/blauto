package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/9/16.
 */
public class QunYingHui implements IDo {
    String baseDir = Common.BASE_DIR + "qunyinghui/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (!status.canDo(Task.QUN_YING_HUI)) {
            return false;
        }

        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match tiaozhan = region.exists(baseDir + "tiaozhan.png");
        if (tiaozhan != null) {
            tiaozhan.click();

            Match qunyinghuianniu = region.exists(baseDir + "longhuqunyinghui.png", 20);
            if (qunyinghuianniu != null) {
                qunyinghuianniu.click();

                Thread.sleep(3000L);

                Match qunyinhui = region.exists(baseDir + "qunyinghui", 20);
                if (qunyinhui != null) {
                    qunyinhui.click();

                    Match canzhan = region.exists(baseDir + "canzhan.png", 20);
                    if (canzhan != null) {
                        canzhan.click();

                        Thread.sleep(6000L);

                        status.Done(Task.QUN_YING_HUI);

                        Match jiangli = region.exists(baseDir + "jiangli.png");
                        if (jiangli != null) {
                            jiangli.click();

                            Match lingqu = region.exists(baseDir + "lingqu.png");
                            if (lingqu != null && isButtonEnable(lingqu)) {
                                lingqu.click();
                            }
                        }
                    } else {
                        // 找不到参战,则默认认为已经执行过群英会
                        status.Done(Task.QUN_YING_HUI);
                    }

                    Thread.sleep(1000L);
                    region.click(Common.CLOSE);
                }

                Thread.sleep(1000L);
                region.click(Common.CLOSE);
            }

        }

        return true;
    }
}
