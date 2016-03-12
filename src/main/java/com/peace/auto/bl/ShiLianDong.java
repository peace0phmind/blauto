package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/12/16.
 */
public class ShiLianDong extends ZhanBao implements IDo {
    String baseDir = Common.BASE_DIR + "shiliandong/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        if (canFight(region)) {

        }
    }
}
