package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class Building {

    private static List<Integer> buildingIds = Arrays.asList(
            1,
            11,
            10,
            3,
            5,
            12,
            6,
            4,
            8,
            7,
            2,
            9);

    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "building/";

        try {
            screen.doubleClick(baseDir + "building.png");

            Match inbuluo = screen.exists(baseDir + "buluodating.png", 3);

            if (inbuluo != null) {
                Iterator<Match> all = screen.findAll(baseDir + "shengji.png");
                List<Match> list = new ArrayList<>();
                while (all.hasNext()) {
                    list.add(all.next());
                }

                List<Match> sorted = list.stream().sorted((x, y) -> x.getX() - y.getX()).sorted((x, y) -> x.getY() - y.getY()).collect(Collectors.toList());
                buildLoop:
                for (Integer bid : buildingIds) {
                    Match match = sorted.get(bid - 1);
                    for (int i = 0; i < 5; i++) {
                        match.click();

                        Match end = screen.exists(baseDir + "end.png", 0.5);
                        if (end != null) {
                            screen.click(Common.QUE_DING);
                            break buildLoop;
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
