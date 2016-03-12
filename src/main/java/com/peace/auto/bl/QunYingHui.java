package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/9/16.
 */
public class QunYingHui implements IDo {
    String baseDir = Common.BASE_DIR + "qunyinghui/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match tiaozhan = region.exists(baseDir + "tiaozhan.png");
        if (tiaozhan != null) {
            tiaozhan.click();

        }
    }
}
