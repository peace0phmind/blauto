package com.peace.auto.bl;

import org.sikuli.script.*;

/**
 * Created by mind on 4/30/16.
 */
public class ShenQi implements IDo {

    private static final String baseDir = Common.BASE_DIR + "shenqi/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (!status.canDo(Task.SHEN_QI)) {
            return false;
        }

        IRobot robot = region.getScreen().getRobot();
        Match match = region.exists(baseDir + "shengyu.png");
        if (match != null) {
            Location center = match.getCenter();
            robot.smoothMove(center, center.above(300), 1000);
            Match shenqi = region.exists(baseDir + "shenqi.png");
            if (shenqi != null) {
                shenqi.click();

                Match zhaohuan = region.exists(baseDir + "zhaohuan.png");
                if (zhaohuan != null) {
                    zhaohuan.click();

                    Match mianfeizhaohuan = region.exists(baseDir + "mianfeizhaohuan.png");
                    if (mianfeizhaohuan != null) {
                        mianfeizhaohuan.click();
                        status.Done(Task.SHEN_QI);

                        Thread.sleep(500L);
                        region.click(Common.QUE_DING);
                    }
                }

                Thread.sleep(500L);
                region.click(Common.CLOSE);
                Thread.sleep(500L);
            }

            robot.smoothMove(center, center.below(300), 1000);
        }

        return true;
    }
}
