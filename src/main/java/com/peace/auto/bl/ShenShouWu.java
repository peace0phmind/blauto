package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class ShenShouWu {
    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "shenshouwu/";

        try {
            Match shenshouwu = screen.exists(baseDir + "shenshouwu.png", 0.5);
            if (shenshouwu != null) {
                shenshouwu.doubleClick();
                Thread.sleep(2000L);
            }

            screen.click(baseDir + "weishi.png");

            Thread.sleep(500L);

            Match weishi = screen.find(baseDir + "chujishicai.png").below().find(baseDir + "doweishi.png");
            if (weishi != null) {
                for (int i = 0; i < 20; i++) {
                    weishi.click();

                    Match noweishi = screen.exists(baseDir + "noweishi.png", 0.5);
                    if (noweishi != null) {
                        screen.click(Common.QUE_DING);
                        break;
                    }
                }
            }

            screen.click(Common.CLOSE);
            Thread.sleep(500L);

        } catch (InterruptedException e) {
            log.error("{}", e);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        }
    }
}
