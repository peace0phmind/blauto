package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/12/16.
 */
abstract public class ZhanBao {

    private String baseDir = Common.BASE_DIR + "zhanbao/";

    public boolean canFight(Region region) throws FindFailed, InterruptedException {
        boolean ret = false;
        // 先查看是否有战报
        region.click(Common.MENU);

        Match zhanbao = region.exists(new Pattern(baseDir + "zhanbao.png").similar(0.95f));
        if (zhanbao != null) {
            zhanbao.click();

            Match kongzhanbao = region.exists(baseDir + "kongzhanbao.png", 6);
            if (kongzhanbao != null && kongzhanbao.getScore() > 0.95) {
                ret = true;
            }

            region.click(Common.CLOSE);
        }

        return ret;
    }
}