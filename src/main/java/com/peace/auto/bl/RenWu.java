package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class RenWu implements IDo {
    String baseDir = Common.BASE_DIR + "task/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (status.canDo(Task.YUE_KA)) {
            Match yueka = region.exists(baseDir + "yueka.png");
            if (yueka != null) {
                yueka.click();
                Thread.sleep(1000L);

                region.click(baseDir + "lingquyueka.png");

                Thread.sleep(2000L);

                Match close = region.exists(Common.CLOSE, 20);
                if (close != null) {
                    close.click();
                }

                status.Done(Task.YUE_KA);
            }
        }

        // 收集金币
        if (status.canDo(Task.SHOU_JI_JIN_BI)) {
            Match jinbi = region.exists(baseDir + "jinbi.png", 0.5);
            if (jinbi != null) {
                jinbi.click();
                status.Done(Task.SHOU_JI_JIN_BI);
            }
        }

        // 收集礼包
        if (status.canDo(Task.LI_BAO)) {
            Match libao = region.exists(baseDir + "libao.png", 0.5);
            if (libao != null && libao.getScore() > 0.95) {
                libao.click();

                Match lingqu = region.exists(baseDir + "lingqu.png", 10);
                if (lingqu != null) {
                    Thread.sleep(2000L);
                    region.click(baseDir + "lingqu.png");
                    status.Done(Task.LI_BAO);
                }

                Match nolibao = region.exists(baseDir + "nolibao.png", 0.5);
                if (nolibao != null) {
                    region.click(Common.QUE_DING);
                }
            }
        }

        // 领取任务
        if (status.canDo(Task.LIN_QU_REN_WU)) {
            Match renwu = region.exists(baseDir + "renwu.png", 0.5);
            if (renwu != null) {
                renwu.click();

                Thread.sleep(2000L);

                // wait 10 seconds, for check if in task list
                Match renwulingqu = region.exists(baseDir + "renwulingqu.png", 3);

                if (renwulingqu != null) {
                    for (int i = 0; i < 20; i++) {

                        if (renwulingqu == null) {
                            break;
                        } else {
                            renwulingqu.click();

                            Match lingqu = region.exists(baseDir + "lingqu.png", 0.5);
                            if (lingqu != null) {
                                lingqu.click();
                                Thread.sleep(2000L);
                                status.Done(Task.LIN_QU_REN_WU);
                            }
                        }

                        renwulingqu = region.exists(baseDir + "renwulingqu.png", 0.5);
                    }
                }

                region.click(Common.CLOSE);
            }
        }

        return true;

    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.YUE_KA, userName)
                && !status.canDo(Task.LI_BAO, userName)
                && !status.canDo(Task.SHOU_JI_JIN_BI, userName)
                && !status.canDo(Task.LIN_QU_REN_WU, userName)) {
            return false;
        }

        return true;
    }
}