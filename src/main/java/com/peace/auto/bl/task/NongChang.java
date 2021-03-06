package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class NongChang implements IDo {
    String baseDir = Common.BASE_DIR + "nongchang/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(baseDir + "nongchang.png");

        Match innongchang = region.exists(baseDir + "innongchang.png", 30);

        if (innongchang != null) {
            // 收获
            Match shouhuo = region.exists(baseDir + "shouhuo.png", 6);
            if (shouhuo != null) {
                shouhuo.click();

                if (status.todayFinishCount(Task.NONG_CHANG_ZHONG_ZHI) != 0) {
                    status.Done(Task.NONG_CHANG_SHOU_CAI);
                }
            }

            Thread.sleep(6000L);
            // 播种
            Match bozhong = region.exists(baseDir + "bozhong.png", 0.5);
            if (bozhong != null) {
                bozhong.click();

                Match bianhua = region.exists(baseDir + "bianhua.png");
                if (bianhua != null) {
                    bianhua.click();

                    Thread.sleep(500L);

                    Match zhongzhi = region.exists(baseDir + "mianfeizhongzhi.png");
                    if (zhongzhi != null) {
                        List<Match> zhongzhis = Lists.newArrayList(region.findAll(baseDir + "mianfeizhongzhi.png"));

                        Optional<Match> lastzhongzhi = zhongzhis.stream().sorted((x, y) -> y.getX() - x.getX()).findFirst();
                        if (lastzhongzhi.isPresent()) {
                            lastzhongzhi.get().click();
                            if (status.isPeace()) {
                                status.Done(Task.NONG_CHANG_ZHONG_ZHI, LocalDateTime.now().plusHours(6));
                                Status.USERS.forEach(x -> {
                                    if (!Status.peaceName().equals(x)) {
                                        status.Done(Task.NONG_CHANG_TOU_CAI_CHECK, LocalDateTime.now().plusHours(5), x);
                                    }
                                });
                            } else {
                                status.Done(Task.NONG_CHANG_ZHONG_ZHI);
                            }
                        }
                    }
                }
            }

            Thread.sleep(500L);

            // 收获龙
            Match shouhuolong = region.exists(baseDir + "shouhuolong.png", 0.5);
            if (shouhuolong != null) {
                shouhuolong.click();
            }

            Thread.sleep(2000L);

            // 喂食
            Match weishi = region.exists(baseDir + "weishi.png", 5);
            if (weishi != null) {
                weishi.click();
            }

            // 其他农场
            Match qitanongchang = region.exists(baseDir + "qitanongchang.png");
            if (qitanongchang != null) {
                qitanongchang.click();

                Thread.sleep(1000L);

                qiTaNongChang(region, status);

                // peace 翻2页,其他翻1页。
                for (int i = 0; i < (status.isPeace() ? 2 : 1); i++) {
                    ArrayList<Match> allRen = Lists.newArrayList(region.findAll(baseDir + "rentou.png"));
                    Optional<Match> lastRen = allRen.stream().sorted((a, b) -> b.x - a.x).findFirst();
                    if (lastRen.isPresent()) {
                        Match lr = lastRen.get();
                        move(lr, lr.getCenter().left(800), 1000);

                        Thread.sleep(3000L);

                        qiTaNongChang(region, status);
                    }
                }
            }
        }

        Thread.sleep(500L);
        region.click(Common.HUI_CHENG);

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.NONG_CHANG_ZHONG_ZHI, userName)
                && !canShouCai(status, userName)
                && !canTouCai(status, userName)) {
            return false;
        }

        return true;
    }

    private boolean canShouCai(Status status, String userName) {
        if (status.canDo(Task.NONG_CHANG_SHOU_CAI, userName)) {
            LocalDateTime lastFinishTime = status.getLastFinishTime(Task.NONG_CHANG_ZHONG_ZHI, userName);
            if (lastFinishTime != null) {
                return lastFinishTime.isBefore(LocalDateTime.now());
            }
        }

        return false;
    }

    private boolean canTouCai(Status status, String userName) {
        if (status.canDo(Task.NONG_CHANG_TOU_CAI, userName)) {
            return status.canDo(Task.NONG_CHANG_TOU_CAI_CHECK, userName);
        }

        return false;
    }

    private void qiTaNongChang(Region region, Status status) throws FindFailed, InterruptedException {
        // 采摘
        Match zhai = region.exists(baseDir + "zhai.png");
        if (zhai != null) {
            Iterator<Match> all = region.findAll(baseDir + "zhai.png");
            while (all.hasNext()) {
                all.next().click();
                Thread.sleep(1000L);

                Match zhaiqu = region.exists(baseDir + "zhaiqu.png");
                if (zhaiqu != null) {
                    zhaiqu.click();
                    Thread.sleep(1000L);

                    status.Done(Task.NONG_CHANG_TOU_CAI);
                }
            }
        }

        if (status.todayFinishCount(Task.NONG_CHANG_TOU_CAI, status.getCurrentUser()) >= Task.NONG_CHANG_TOU_CAI.getDayLimit(status.getCurrentUser())) {
            status.Done(Task.NONG_CHANG_TOU_CAI_CHECK, Status.nextDayCheck());
        } else {
            status.Done(Task.NONG_CHANG_TOU_CAI_CHECK);
        }

        // 喂食
        Match wei = region.exists(baseDir + "wei.png");
        if (wei != null) {
            Iterator<Match> all = region.findAll(baseDir + "wei.png");
            while (all.hasNext()) {
                all.next().click();
                Thread.sleep(1000L);

                Match weishi = region.exists(baseDir + "weishi.png");
                if (weishi != null) {
                    weishi.click();
                    Thread.sleep(500L);
                }
            }
        }
    }
}
