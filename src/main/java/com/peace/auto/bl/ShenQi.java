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
        Match bashou = region.exists(new Pattern(baseDir + "shengyu.png").similar(0.9f), 15);
        if (bashou != null) {
            move(bashou, bashou.getCenter().above(300), 1000);


            Match shenqi = region.exists(baseDir + "shenqi.png");
            if (shenqi != null) {
                shenqi.click();
                Thread.sleep(1000L);

                // 神器召唤
                if (status.canDo(Task.SHEN_QI)) {
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

                                // 免费倒计时不存在,则进行免费祭拜
                                Match daojishi = region.exists(baseDir + "mianfeidaojishi.png");
                                if (daojishi == null) {
                                    Match jb = region.exists(baseDir + "mianfeijibai.png");
                                    if (jb != null) {
                                        jb.click();
                                        log.info("祭拜 : {}", shenxiang);
                                        Thread.sleep(500L);
                                        bjb = true;
                                    }
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
            }

            bashou = region.exists(new Pattern(baseDir + "shengyu.png").similar(0.9f), 15);
            move(bashou, bashou.getCenter().below(300), 1000);
        }

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.SHEN_QI) && !status.canDo(Task.SHEN_XIANG_SHENG_JI)) {
            return false;
        }

        return true;
    }
}
