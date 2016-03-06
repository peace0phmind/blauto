package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/6/16.
 */
public class YingHun implements IDo {
    String baseDir = Common.BASE_DIR + "yinghun/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        region.click(Common.MENU);

        Match yinghun = region.exists(baseDir + "yinghun.png", 3);
        if (yinghun != null) {
            yinghun.click();

            Thread.sleep(1000L);

            Match zhaohuan = region.exists(baseDir + "zhaohuanyinghun.png", 10);
            if (zhaohuan != null) {
                zhaohuan.click();

                Match meiri = region.exists(baseDir + "meirilingqu.png", 10);
                while (meiri != null && isButtonEnable(meiri)) {
                    meiri.click();
                }
            }
        }

        region.click(Common.CLOSE);
        Thread.sleep(500L);
        region.click(Common.CLOSE);
    }
}
