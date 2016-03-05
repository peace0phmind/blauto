package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class ShengYu {

    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "shengyu/";

        try {
            screen.doubleClick(baseDir + "shengyu.png");

            Match inshengyu = screen.exists(baseDir + "inshengyu.png", 3);
            if (inshengyu != null) {
                Iterator<Match> all = screen.findAll(baseDir + "shengji.png");
                List<Match> list = new ArrayList<>();
                while (all.hasNext()) {
                    list.add(all.next());
                }

                List<Match> sorted = list.stream().sorted((x, y) -> x.getX() - y.getX()).sorted((x, y) -> x.getY() - y.getY()).collect(Collectors.toList());

                shengyuLoop:
                for (Match shengji : sorted) {
                    for (int i = 0; i < 5; i++) {
                        shengji.click();

                        Match end = screen.exists(baseDir + "end.png", 0.5);
                        if (end != null) {
                            screen.click(Common.QUE_DING);
                            break shengyuLoop;
                        }
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
