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

    public void Do(Region region)throws FindFailed, InterruptedException {
        String baseDir = Common.BASE_DIR + "haoyou/";

        try {
            region.click(Common.MENU);
            Match haoyou = region.exists(baseDir + "haoyou.png", 1);

            if (haoyou != null) {
                haoyou.click();

                Thread.sleep(3000L);

                Match aixing = region.exists(baseDir + "songaixing.png", 3);
                if (aixing != null) {
                    Iterator<Match> all = region.findAll(baseDir + "songaixing.png");
                    while (all.hasNext()) {
                        all.next().click();
                        region.click(baseDir + "xiaciba.png");
                    }
                }
            }

        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        } finally {
            try {
                region.click(Common.CLOSE);
                Thread.sleep(500L);

                region.click(Common.MENU1);
            } catch (FindFailed findFailed) {
                log.error("{}", findFailed);
            } catch (InterruptedException e) {
                log.error("{}", e);
            }
        }
    }
}
