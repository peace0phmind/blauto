package com.peace.auto.bl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadFactory;

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
            Match shouhuo = region.exists(baseDir + "shouhuo.png", 0.5);
            if (shouhuo != null) {
                shouhuo.click();
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
                            status.Done(Task.NONG_CHANG_ZHONG_ZHI);
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


            Match qitanongchang = region.exists(baseDir + "qitanongchang.png");
            if (qitanongchang != null) {
                qitanongchang.click();

                Thread.sleep(1000L);

                // 偷菜
                if (status.canDo(Task.NONG_CHANG_TOU_CAI)) {
                    Match zhai = region.exists(baseDir + "zhai.png");
                    if (zhai != null) {
                        zhai.click();
                        Thread.sleep(1000L);

                        Match zhaiqu = region.exists(baseDir + "zhaiqu.png");
                        if (zhaiqu != null) {
                            zhaiqu.click();
                            Thread.sleep(1000L);

                            status.Done(Task.NONG_CHANG_TOU_CAI);
                        }
                    }
                }

                // 喂食
                weishi(region, status);

                ArrayList<Match> allRen = Lists.newArrayList(region.findAll(baseDir + "rentou.png"));
                Optional<Match> lastRen = allRen.stream().sorted((a, b) -> b.x - a.x).findFirst();
                if (lastRen.isPresent()) {
                    Match lr = lastRen.get();
                    move(lr, lr.getCenter().left(800), 1000);

                    Thread.sleep(3000L);

                    weishi(region, status);
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
                && !status.canDo(Task.NONG_CHANG_TOU_CAI, userName)) {
            return false;
        }

        return true;
    }

    private void weishi(Region region, Status status) throws FindFailed, InterruptedException {
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
