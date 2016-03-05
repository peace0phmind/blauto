package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class JingJiChang {
    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "jingjichang/";

        try {
            screen.click(Common.RI_CHANG);

            Match jingjichang = screen.exists(baseDir + "jingjichang.png", 30);
            if (jingjichang != null) {
                jingjichang.click();

                Match injingjichang = screen.exists(baseDir + "injingjichang.png", 30);
                if (injingjichang != null) {
                    // 领取奖励
                    Match lingqujiangli = screen.exists(baseDir + "lingqujiangli.png", 3);
                    if (lingqujiangli != null) {
                        lingqujiangli.click();
                    }

                    // 挑战
                    screen.click(baseDir + "tiaozhan.png");
                    Match queding = screen.exists(baseDir + "queding.png", 3);
                    if (queding != null) {
                        queding.click();

                        screen.exists(baseDir + "")
                    }

                }
            }

            screen.click(Common.CLOSE);
            Thread.sleep(500L);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
