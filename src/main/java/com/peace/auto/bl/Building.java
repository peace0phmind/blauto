package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class Building implements IDo {

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

    String baseDir = Common.BASE_DIR + "building/";

    public void Do(Region region) throws FindFailed, InterruptedException {

        region.doubleClick(baseDir + "building.png");

        Thread.sleep(1000L);

        Match inbuluo = region.exists(baseDir + "buluodating.png", 10);

        if (inbuluo != null) {
            // 购买队列
            Match kaiqixinduilie = region.exists(baseDir + "kaiqixinduilie.png", 3);
            if (kaiqixinduilie != null) {
                if (kaiqixinduilie.getX() < 400) {
                    kaiqixinduilie.click();

                    Match goumaiduilie = region.exists(baseDir + "goumaiduilie.png", 3);
                    if (goumaiduilie != null) {
                        region.click(Common.QUE_DING);
                    }
                }
            }

            // 升级
            Match inbuluodating = region.exists(baseDir + "inbuluodating.png", 3);
            if (inbuluodating != null) {
                Iterator<Match> all = region.findAll(baseDir + "shengji.png");
                List<Match> list = new ArrayList<>();
                while (all.hasNext()) {
                    list.add(all.next());
                }

                List<Match> sorted = list.stream().sorted((x, y) -> x.getX() - y.getX()).sorted((x, y) -> x.getY() - y.getY()).collect(Collectors.toList());
                buildLoop:
                for (Integer bid : buildingIds) {
                    Match match = sorted.get(bid - 1);

                    if (!isButtonEnable(match)) {
                        continue;
                    }

                    do {
                        match.click();

                        Match end = region.exists(baseDir + "end.png", 0.5);
                        if (end != null) {
                            region.click(Common.QUE_DING);
                            break buildLoop;
                        }
                    } while (isButtonEnable(match));
                }
            }
        }

        region.click(Common.CLOSE);
    }
}
