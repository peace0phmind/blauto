package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class ShenShouWu implements IDo {
    String baseDir = Common.BASE_DIR + "shenshouwu/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (isTodayFirstFinished()) {
            return false;
        }

        Match shenshouwu = region.exists(baseDir + "shenshouwu.png");

        if (shenshouwu != null) {
            shenshouwu.doubleClick();
        }

        Match inweishi = region.exists(baseDir + "weishi.png", 10);
        if (inweishi != null) {
            inweishi.click();

            Match weishi = region.find(baseDir + "chujishicai.png").below().find(baseDir + "doweishi.png");
            if (weishi != null) {
                for (int i = 0; i < 20; i++) {
                    weishi.click();

                    Match noweishi = region.exists(baseDir + "noweishi.png", 0.5);
                    if (noweishi != null) {
                        region.click(Common.QUE_DING);
                        break;
                    }
                }
            }
        }

        region.click(Common.CLOSE);

        return true;
    }
}
