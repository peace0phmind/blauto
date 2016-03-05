package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class Task {
    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "task/";

        try {
            // 收集金币
            Match jinbi = screen.exists(baseDir + "jinbi.png", 0.5);
            if (jinbi != null) {
                jinbi.doubleClick();
            }

            // 收集礼包
            Match libao = screen.exists(baseDir + "libao.png", 0.5);
            if (libao != null) {
                libao.doubleClick();
                Match lingqu = screen.exists(baseDir + "lingqu.png", 0.5);
                if (lingqu != null) {
                    lingqu.click();
                }

                Match nolibao = screen.exists(baseDir + "nolibao.png", 0.5);
                if (nolibao != null) {
                    screen.click(Common.QUE_DING);
                }
            }

            // 领取任务
            Match renwu = screen.exists(baseDir + "renwu.png", 0.5);
            if (renwu != null) {
                renwu.doubleClick();

                // wait 10 seconds, for check if in task list
                Match renwulingqu = screen.exists(baseDir + "renwulingqu.png", 3);

                if (renwulingqu != null) {
                    for (int i = 0; i < 20; i++) {

                        if (renwulingqu == null) {
                            break;
                        } else {
                            renwulingqu.click();

                            Match lingqu = screen.exists(baseDir + "lingqu.png", 0.5);
                            if (lingqu != null) {
                                lingqu.click();
                                Thread.sleep(500L);
                            }
                        }

                        renwulingqu = screen.exists(baseDir + "renwulingqu.png", 0.5);
                    }
                }

                screen.click(Common.CLOSE);

                Thread.sleep(500L);
            }
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
