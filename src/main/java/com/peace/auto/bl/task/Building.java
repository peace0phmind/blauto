package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            12,
            3,
            6,
            8,
            4,
            5,
            7,
            2,
            9);

    private static List<Integer> peaceBuildingIds = Arrays.asList(
            12,
            11,
            10,
            1,
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
        Match building = region.exists(baseDir + "building.png", 10);
        log.debug("{}", building);
        if (building != null) {
            building.click();

            Thread.sleep(1000L);

            Match inbuluo = region.exists(baseDir + "buluodating.png", 10);

            if (inbuluo != null) {
                if (status.canDo(Task.BUILDING_DUI_LIE)) {
                    Match kaiqixinduilie = region.exists(baseDir + "kaiqixinduilie.png", 3);
                    if (kaiqixinduilie != null) {
                        Iterator<Match> all = region.findAll(baseDir + "kaiqixinduilie.png");
                        while (all.hasNext()) {
                            kaiqixinduilie = all.next();

                            if (status.isPeace() || kaiqixinduilie.getX() < 400) {
                                kaiqixinduilie.click();
                                status.Done(Task.BUILDING_DUI_LIE);

                                Match goumaiduilie = region.exists(baseDir + "goumaiduilie.png", 3);
                                if (goumaiduilie != null) {
                                    region.click(Common.QUE_DING);
                                    Thread.sleep(3000L);
                                }
                            }
                        }
                    }
                }

                // 升级
                Match inbuluodating = region.exists(baseDir + "inbuluodating.png", 3);
                if (inbuluodating != null) {
                    List<Match> list = Lists.newArrayList(region.findAll(baseDir + "shengji.png"));

                    List<Match> sorted = list.stream().sorted((x, y) -> x.getX() - y.getX()).sorted((x, y) -> x.getY() - y.getY()).collect(Collectors.toList());
                    List<Integer> ids = status.isPeace() ? peaceBuildingIds : buildingIds;
                    buildLoop:
                    for (Integer bid : ids) {
                        Match match = sorted.get(bid - 1);

                        if (!isButtonEnable(match)) {
                            continue;
                        }

                        do {
                            match.click();

                            // 检查荣耀
                            checkRongYao(region);

                            Match end = region.exists(baseDir + "end.png", 0.5);
                            if (end != null) {
                                region.click(Common.QUE_DING);
                                break buildLoop;
                            }
                        } while (isButtonEnable(match));
                    }

                    status.Done(Task.BUILDING, getBuildingFinishTime(region));
                }
            }

            region.click(Common.CLOSE);
        }

        return true;
    }

    private LocalDateTime getBuildingFinishTime(Region region) throws FindFailed {
        Iterator<Match> all = region.findAll(baseDir + "shengjilengquezhong.png");
        ArrayList<Match> allTims = Lists.newArrayList(all);
//        allTims.stream().map(x -> x.below(18)).forEach(x -> x.saveScreenCapture(".", "time"));

        return allTims.stream().map(x -> {
            String sTime = getTime(x.below(18), 170);

            String[] splitTime = sTime.split(":");
            LocalDateTime time = Status.nextCheck();

            if (splitTime.length == 3) {
                time = LocalDateTime.now().plusHours(Integer.parseInt(splitTime[0].trim()))
                        .plusMinutes(Integer.parseInt(splitTime[1].trim()))
                        .plusSeconds(Integer.parseInt(splitTime[2].trim()));
            }
            return time;
        }).sorted((a, b) -> a.compareTo(b)).findFirst().get();
    }
}
