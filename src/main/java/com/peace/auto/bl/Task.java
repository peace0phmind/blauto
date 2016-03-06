package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class Task implements IDo {
    String baseDir = Common.BASE_DIR + "task/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        // 收集金币
        Match jinbi = region.exists(baseDir + "jinbi.png", 0.5);
        if (jinbi != null) {
            jinbi.click();
        }

        // 收集礼包
        Match libao = region.exists(baseDir + "libao.png", 0.5);
        if (libao != null && libao.getScore() > 0.95) {
            libao.click();

            Match lingqu = region.exists(baseDir + "lingqu.png", 3);
            if (lingqu != null) {
                lingqu.click();
            }

            Match nolibao = region.exists(baseDir + "nolibao.png", 0.5);
            if (nolibao != null) {
                region.click(Common.QUE_DING);
            }
        }

        // 领取任务
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
                        }
                    }

                    renwulingqu = region.exists(baseDir + "renwulingqu.png", 0.5);
                }
            }

            region.click(Common.CLOSE);
        }
    }
}