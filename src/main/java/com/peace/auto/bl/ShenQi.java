package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.util.Arrays;

/**
 * Created by mind on 4/30/16.
 */
@Slf4j
public class ShenQi implements IDo {

    private static final String baseDir = Common.BASE_DIR + "shenqi/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (!status.canDo(Task.SHEN_QI) && !status.canDo(Task.SHEN_XIANG_SHENG_JI)) {
            return false;
        }

        Match shengyu = region.exists(new Pattern(baseDir + "shengyu.png").similar(0.9f));
        if (shengyu != null) {
            move(shengyu, shengyu.getCenter().above(300), 1000);


            // 神器召唤
            if (status.canDo(Task.SHEN_QI)) {
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
                }
            }

            // 神像升级
            if (status.canDo(Task.SHEN_XIANG_SHENG_JI)) {
                Match shengji = region.exists(baseDir + "shengji.png");
                if (shengji != null) {
                    shengji.click();

                    boolean bjb = false;
                    for (String shenxiang : Arrays.asList("jianling.png", "moling.png", "paoling.png", "yongling.png")) {
                        Match sx = region.exists(baseDir + shenxiang);
                        if (sx != null) {
                            sx.click();
                            Thread.sleep(500L);

                            Match jb = region.exists(baseDir + "mianfeijibai.png");
                            if (jb != null) {
                                jb.click();
                                log.info("祭拜 : {}", shenxiang);
                                Thread.sleep(500L);
                                bjb = true;
                            }
                        }
                    }

                    if (bjb) {
                        status.Done(Task.SHEN_XIANG_SHENG_JI);
                    }
                }
            }

            Thread.sleep(500L);
            region.click(Common.CLOSE);
            Thread.sleep(500L);

            shengyu = region.exists(new Pattern(baseDir + "shengyu.png").similar(0.9f));
            move(shengyu, shengyu.getCenter().below(300), 1000);
        }

        return true;
    }
}
