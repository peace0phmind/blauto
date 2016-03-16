package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.Iterator;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class HaoYou implements IDo {
    String baseDir = Common.BASE_DIR + "haoyou/";

    public boolean Done(Region region) throws FindFailed, InterruptedException {
        if (isTodayFirstFinished()) {
            return false;
        }

        region.click(Common.MENU);

        Match haoyou = region.exists(baseDir + "haoyou.png", 3);
        if (haoyou != null) {
            haoyou.click();

            Match aixing = region.exists(baseDir + "songaixing.png", 3);
            if (aixing != null && aixing.getScore() > 0.95) {
                Iterator<Match> all = region.findAll(baseDir + "songaixing.png");
                while (all.hasNext()) {
                    all.next().click();
                    Thread.sleep(500L);
                    region.click(baseDir + "xiaciba.png");
                    Thread.sleep(500L);
                }
            }
        }

        region.click(Common.CLOSE);
        Thread.sleep(500L);
        region.click(Common.MENU1);

        return true;
    }
}
