package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/7/16.
 */
public class LieChang implements  IDo{

    String baseDir = Common.BASE_DIR + "liechang/";

    public void Do(Region region) throws FindFailed, InterruptedException {

        Match liechang = region.exists(baseDir + "liechang.png");
        if (liechang != null) {
            liechang.click();

            Thread.sleep(1000L);

            Match zhengshou = region.exists(baseDir + "zhengshou.png");
            if (zhengshou != null && isButtonEnable(zhengshou, 10, 10)) {
                zhengshou.click();
            }

            region.click(baseDir + "huicheng.png");
        }
    }
}
