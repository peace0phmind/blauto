package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class XunBao {

    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "xunbao/";

        try {
            screen.doubleClick(baseDir + "xunbao.png");

            Match inbaoshiwu = screen.exists(baseDir + "inbaoshiwu.png", 10);
            if (inbaoshiwu == null) {
                return;
            }

            for (int i = 0; i < 100; i++) {
                Iterator<Match> all = screen.findAll(baseDir + "xunbaobutton.png");
                List<Match> list = new ArrayList<>();
                while (all.hasNext()) {
                    Match next = all.next();
                    list.add(next);
                }

                Optional<Match> lastButton = list.stream().sorted((a, b) -> b.x - a.x).findFirst();

                if (lastButton.isPresent()) {
                    Match match = lastButton.get();
                    match.click();
                }

                Match exists = screen.exists(baseDir + "xunbaoend.png", 0.1);
                if (exists != null) {
                    exists.below().click(Common.QUE_DING);
                    break;
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
