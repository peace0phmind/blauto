package com.peace.auto.bl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mind on 3/9/16.
 */
@Slf4j
public class ChuZheng extends ZhanBao implements IDo {
    String baseDir = Common.BASE_DIR + "chuzheng/";

    List<String> direns = Arrays.asList(
            "anfenglieshou.png",
            "xieyuemanbing.png",
            "eleipaoshou.png",
            "heiwuyaowu.png"
    );

    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.CHU_ZHENG_YE_GUAI, userName)
                && !super.canDo(status, userName)) {
            return false;
        }

        return true;
    }

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (canFight(region, status)) {
            // 获取战力
            int userZhanLi = 0;
            Match zhanli = region.exists(baseDir + "zhanli.png");
            if (zhanli != null) {
                Region right = zhanli.right(80);
                userZhanLi = getNumber(right);
            }

            if (userZhanLi < 20000) {
                // 获取战力失败
                return false;
            }

            region.click(Common.MENU);

            Match chuzheng = region.exists(baseDir + "chuzheng.png");
            if (chuzheng != null) {
                chuzheng.click();
                Thread.sleep(3000L);

                Match chuzhenganniu = region.exists(baseDir + "chuzhenganniu.png");
                if (chuzhenganniu != null) {
                    String diren = direns.get(LocalDate.now().getDayOfYear() % 4);

                    region.click(baseDir + diren);
                    Thread.sleep(1000L);

                    // 选择大于战力的第二个
                    while (true) {
                        Iterator<Match> all = region.findAll(new Pattern(baseDir + "yingxiongjingyanjinbi.png").similar(0.90f));
                        ArrayList<Match> matches = Lists.newArrayList(all);
                        if (matches.size() < 3) {
                            log.error("list size is {}.", matches.size());
                            return false;
                        }

                        List<Match> sortedList = matches.stream().sorted((a, b) -> b.y - a.y).collect(Collectors.toList());

                        Match match = sortedList.get(0);
                        Region left = match.left(60);

                        int cankaozhanli = getNumber(left);
                        log.info("{}, {}, {}", status.getCurrentUser(), userZhanLi, cankaozhanli);
                        if (cankaozhanli < userZhanLi) {
                            move(match, match.getCenter().above(51), 1000);
                            Thread.sleep(500L);
                        } else {
                            sortedList.get(2).click();
                            break;
                        }
                    }

                    Thread.sleep(1000L);

                    chuzhenganniu.click();

                    Match huolibuzu = region.exists(baseDir + "huolibuzu.png");
                    if (huolibuzu != null) {
                        region.click(Common.QUE_DING);
                        status.Done(Task.CHU_ZHENG_YE_GUAI);
                    }

                    Match zidongbubing = region.exists(baseDir + "zidongbubing.png");
                    if (zidongbubing != null) {
                        zidongbubing.click();
                        Thread.sleep(500L);

                        for (int i = 0; i < 5; i++) {
                            region.click(baseDir + "tianjiacishu.png");
                            Thread.sleep(500L);
                        }

                        region.click(baseDir + "quanbubuman.png");

                        Thread.sleep(500L);

                        region.click(baseDir + "quedingchuzheng.png");

                        status.Done(Task.CHU_ZHENG_YE_GUAI);
                    }

                    Thread.sleep(1000L);
                    // 出征成功, 点击close
                    region.click(Common.CLOSE);
                }
            }

            return true;
        }

        return false;
    }
}
