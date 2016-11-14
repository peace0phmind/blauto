package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
        if (super.canDo(status, userName)
                && (status.canDo(Task.CHU_ZHENG_YE_GUAI, userName)
                || canChuZhengDiDui(status, userName)
                || status.canDo(Task.CHU_ZHENG_DI_DUI_CHECK, userName))) {
            return true;
        }

        return false;
    }

    private boolean canChuZhengDiDui(Status status, String userName) {
        LocalDateTime now = LocalDateTime.now();
        if (status.canDo(Task.CHU_ZHENG_DI_DUI, userName)) {
            LocalDateTime fightEnd = status.getLastFinishTime(Task.CHU_ZHENG_DI_DUI_CAN_FIGHT, userName);
            if (fightEnd != null && now.isBefore(fightEnd) && now.isAfter(fightEnd.minusDays(2))) {
                return true;
            }
        }

        return false;
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

                if (status.canDo(Task.CHU_ZHENG_YE_GUAI)) {
                    return chuZhengYeGuai(region, status, userZhanLi);
                }

                if (canChuZhengDiDui(status, status.getCurrentUser()) || status.canDo(Task.CHU_ZHENG_DI_DUI_CHECK)) {
                    return chuZhengDiDui(region, status);
                }

                Thread.sleep(1000L);
                region.click(Common.CLOSE);
            }

            return true;
        }

        return false;
    }

    private boolean chuZhengDiDui(Region region, Status status) throws InterruptedException, FindFailed {
        Match diduishili = region.exists(baseDir + "diduishili.png");
        if (diduishili != null) {
            diduishili.click();
            Thread.sleep(6000L);

            Match shoucangdebuluo = region.exists(baseDir + "shoucangdebuluo.png");
            if (shoucangdebuluo != null) {
                shoucangdebuluo.click();
                Thread.sleep(3000L);

                if (status.canDo(Task.CHU_ZHENG_DI_DUI_CHECK)) {
                    Match zhengchang = region.exists(baseDir + "zhengchang.png");

                    if (zhengchang != null) {
                        ArrayList<Match> zhengchangs = Lists.newArrayList(region.findAll(baseDir + "zhengchang.png"));
                        if (zhengchangs.size() != 6) {
                            status.Done(Task.CHU_ZHENG_DI_DUI_CHECK);
                        } else {
                            for (Match zc : zhengchangs) {
                                zc.left(800).click();
                                Match xuanzhan = region.exists(baseDir + "xuanzhan.png", 10);
                                if (xuanzhan != null) {
                                    xuanzhan.click();

                                    Match xuanzhanok = region.exists(baseDir + "xuanzhanchenggong.png");
                                    if (xuanzhanok != null) {
                                        region.click(Common.QUE_DING);
                                    }

                                    clickInside(region, Common.CLOSE);
                                    Thread.sleep(1000L);
                                }
                            }

                            LocalDateTime fightEnd = LocalDateTime.now().plusHours(1).plusDays(2);
                            status.Done(Task.CHU_ZHENG_DI_DUI_CHECK, fightEnd);
                            status.Done(Task.CHU_ZHENG_DI_DUI_CAN_FIGHT, fightEnd);
                        }
                    }
                }

                // 可以出征敌对
                if (canChuZhengDiDui(status, status.getCurrentUser())) {

                    ArrayList<Match> kezhandous = Lists.newArrayList(region.findAll(baseDir + "kezhandou.png"));
                    List<Match> matches = kezhandous.stream().sorted((x, y) -> x.getY() - y.getY()).collect(Collectors.toList());
                    Match kezhandou = matches.get((int) (status.todayFinishCount(Task.CHU_ZHENG_DI_DUI) % matches.size()));

                    log.info("Matches: {}", matches);

                    kezhandou.click();

                    Thread.sleep(1000L);
                    kezhandou.click();

                    Thread.sleep(3000L);

                    Match chuzhen = region.exists(baseDir + "chuzhenganniu.png", 10);
                    log.info("{}", chuzhen);
                    if (chuzhen != null) {
                        chuzhen.click();

                        Thread.sleep(1000L);

                        Match xuanzechuzhenduixiang = region.exists(baseDir + "xuanzechuzhenduixiang.png");
                        log.info("{}", xuanzechuzhenduixiang);
                        if (xuanzechuzhenduixiang != null) {
                            log.info("Xuan ze chu zhen dui xiang.");
                            region.click(Common.QUE_DING);
                            kezhandou.click();
                            Thread.sleep(1000L);
                            chuzhen.click();
                            Thread.sleep(1000L);
                        }

                        Match quanbubuman = region.exists(baseDir + "quanbubuman.png");
                        if (quanbubuman != null) {
                            quanbubuman.click();

                            Thread.sleep(1000L);

                            region.click(baseDir + "quedingchuzheng.png");
                            status.Done(Task.CHU_ZHENG_DI_DUI);
                        }
                    }
                }

                Thread.sleep(3000L);
                region.click(Common.CLOSE);
                return true;
            }
        }
        return false;
    }

    private boolean chuZhengYeGuai(Region region, Status status, int userZhanLi) throws FindFailed, InterruptedException {
        Match chuzhenganniu = region.exists(baseDir + "chuzhenganniu.png");
        if (chuzhenganniu != null) {
            String diren = direns.get(LocalDate.now().getDayOfYear() % 4);

            region.click(baseDir + diren);
            Thread.sleep(1000L);

            // 选择大于战力的第二个
            if (status.getCurrentUser().equals("peace0ph003")) {
                while (true) {
                    Iterator<Match> all = region.findAll(new Pattern(baseDir + "yingxiongjingyanjinbi.png").similar(0.85f));
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
                        Thread.sleep(1000L);
                        sortedList.get(2).click();
                        Thread.sleep(1000L);
                        sortedList.get(2).click();
                        break;
                    }
                }
            } else {
                Pattern duishoupng = new Pattern(baseDir + "duishou.png").similar(0.95f);
                for (int i = 0; i < 10; i++) {
                    Match duishou = region.exists(duishoupng);
                    if (duishou == null) {
                        Iterator<Match> all = region.findAll(new Pattern(baseDir + "yingxiongjingyanjinbi.png").similar(0.85f));
                        ArrayList<Match> matches = Lists.newArrayList(all);
                        if (matches.size() < 3) {
                            log.error("list size is {}.", matches.size());
                            return false;
                        }

                        Match match = matches.stream().sorted((a, b) -> b.y - a.y).findFirst().get();
                        move(match, match.getCenter().above(280), 1000);
                        Thread.sleep(500L);
                    } else {
                        Iterator<Match> allduishou = region.findAll(duishoupng);
                        ArrayList<Match> duishous = Lists.newArrayList(allduishou);
                        Match first = duishous.stream().sorted((a, b) -> a.y - b.y).findFirst().get();
                        first.above(20).click();
                        break;
                    }
                }
            }

            Thread.sleep(1000L);
            chuzhenganniu.click();
            Thread.sleep(3000L);

            Match zidongbubing = region.exists(baseDir + "zidongbubing.png");
            if (zidongbubing != null) {
                zidongbubing.click();
                Thread.sleep(1000L);

                for (int i = 0; i < 5; i++) {
                    region.click(baseDir + "tianjiacishu.png");
                    Thread.sleep(500L);
                }

                region.click(baseDir + "quanbubuman.png");

                Thread.sleep(500L);

                region.click(baseDir + "quedingchuzheng.png");

                Match huolibuzu = region.exists(new Pattern(baseDir + "huolibuzu.png").similar(0.9f));
                log.info("{}", huolibuzu);
                if (huolibuzu != null) {
                    Thread.sleep(1000L);
                    status.Done(Task.CHU_ZHENG_YE_GUAI, LocalDateTime.now());
                    region.click(Common.QUE_DING);
                } else {
                    status.Done(Task.CHU_ZHENG_YE_GUAI);
                }
            }

            Thread.sleep(1000L);
            // 出征成功, 点击close
            region.click(Common.CLOSE);
        }

        return true;
    }
}
