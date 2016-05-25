package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.util.Arrays;
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
            12,
            3,
            6,
            5,
            8,
            4,
            7,
            2,
            9);

    String baseDir = Common.BASE_DIR + "building/";

    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.BUILDING, userName);
    }

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.doubleClick(baseDir + "building.png");

        Thread.sleep(1000L);

        Match inbuluo = region.exists(baseDir + "buluodating.png", 10);

        if (inbuluo != null) {
            // 购买队列 - 小号没人力了
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
                List<Match> list = Lists.newArrayList(region.findAll(baseDir + "shengji.png"));

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

            status.Done(Task.BUILDING);
        }

        region.click(Common.CLOSE);

        return true;
    }
}
