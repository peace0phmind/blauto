package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

import java.util.Iterator;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class HaoYou {

    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "haoyou/";

        try {
            screen.click(Common.MENU);
            Match haoyou = screen.exists(baseDir + "haoyou.png", 1);

            if (haoyou != null) {
                haoyou.click();

                Match aixing = screen.exists(baseDir + "songaixing.png", 3);
                if (aixing != null) {
                    Iterator<Match> all = screen.findAll(baseDir + "songaixing.png");
                    while (all.hasNext()) {
                        all.next().click();
                        screen.click(baseDir + "xiaciba.png");
                    }
                }

                screen.click(Common.CLOSE);
                Thread.sleep(500L);

                screen.click(Common.MENU1);
            }

        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        }catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
