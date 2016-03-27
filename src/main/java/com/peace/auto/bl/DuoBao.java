package com.peace.auto.bl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.awt.*;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mind on 3/17/16.
 */
@Slf4j
public class DuoBao implements IDo {
    String baseDir = Common.BASE_DIR + "duobao/";

    @Override
    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        return xunbao(region, false);
    }

    public boolean xunbao(Region region) throws InterruptedException, FindFailed {
        return xunbao(region, true);
    }

    private boolean xunbao(Region region, boolean keepXunbao) throws FindFailed, InterruptedException {
        LocalTime now = LocalTime.now();
        if (!((now.isAfter(LocalTime.of(11, 30)) && now.isBefore(LocalTime.of(13, 55)))
                || (now.isAfter(LocalTime.of(21, 30)) && now.isBefore(LocalTime.of(23, 55))))) {
            return false;
        }

        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match tiaozhan = region.exists(baseDir + "tiaozhan.png", 10);
        if (tiaozhan != null) {
            tiaozhan.click();

            Thread.sleep(3000L);

            Match duobao = region.exists(baseDir + "duobaoqibing.png", 20);
            if (duobao != null) {
                duobao.click();

                region.click(baseDir + "duobaoqibingjingru.png");

                // 领取宝图
                Match baotu = region.exists(baseDir + "lingqubaotu.png");
                if (baotu != null) {
                    baotu.click();
                    Thread.sleep(1000L);

                    Match kaishi = region.exists(baseDir + "kaishixunbao.png");
                    if (kaishi != null) {
                        kaishi.click();
                    }
                }

                Thread.sleep(2000L);

                Match chazhao = region.exists(baseDir + "chazhaofangjian.png");
                if (chazhao != null) {
                    // 持续寻宝
                    if (keepXunbao) {
                        while (true) {
                            jiaru(region, keepXunbao);
                            Color color = getPixelColor(region, 656, 100);
                            if (Math.abs(color.getRed() - 173) < 3
                                    && Math.abs(color.getGreen() - 54) < 3
                                    && Math.abs(color.getBlue() - 45) < 3) {
                                break;
                            }
                        }
                    } else {
                        Match shurufangjian = region.exists(baseDir + "shurufangjianhaoma.png");
                        if (shurufangjian != null) {
                            shurufangjian.type("31");
                        }
                        chazhao.click();

                        Thread.sleep(2000L);
                        jiaru(region, keepXunbao);
                    }
                }

                Thread.sleep(3000L);
                region.click(Common.CLOSE);
            }
            Thread.sleep(500L);
            region.click(Common.CLOSE);
        }

        return true;
    }

    private void jiaru(Region region, boolean keepXunbao) throws FindFailed, InterruptedException {
        Match ru = region.exists(baseDir + "jiaru.png", 60);
        if (ru != null) {
            if (keepXunbao) {
                Thread.sleep(5000L);
                Match changshiweixunbao = region.exists(baseDir + "changshiweixunbao.png");
                if (changshiweixunbao != null) {
                    region.click(Common.QUE_DING);
                    Thread.sleep(1000L);
                }
            }

            ArrayList<Match> kongweis = Lists.newArrayList(region.findAll(baseDir + "baotukongwei.png"));
            ArrayList<Match> jiarus = Lists.newArrayList(region.findAll(baseDir + "jiaru.png"));

            List<Integer> xLine = Arrays.asList(0, 250, 500, 800);
            List<Integer> yLine = Arrays.asList(0, 300, 480);

            Match firstJiaRu = null;
            out_loop:
            for (int i = 0; i < xLine.size() - 1; i++) {
                for (int j = 0; j < yLine.size() - 1; j++) {
                    final int finalI = i;
                    final int finalJ = j;
                    List<Match> kongwei = kongweis.stream().filter(x ->
                            (x.getX() > xLine.get(finalI) && x.getX() < xLine.get(finalI + 1))
                                    && (x.getY() > yLine.get(finalJ) && x.getY() < yLine.get(finalJ + 1))).collect(Collectors.toList());

                    List<Match> jiaru = jiarus.stream().filter(x ->
                            (x.getX() > xLine.get(finalI) && x.getX() < xLine.get(finalI + 1))
                                    && (x.getY() > yLine.get(finalJ) && x.getY() < yLine.get(finalJ + 1))).collect(Collectors.toList());

                    if (jiaru != null && jiaru.size() > 0 && isButtonEnable(jiaru.get(0), 3, 3)) {
                        if (kongwei != null) {
                            switch (kongwei.size()) {
                                case 1:
                                    firstJiaRu = jiaru.get(0);
                                    break out_loop;
                                case 2:
                                    if (firstJiaRu == null) {
                                        firstJiaRu = jiaru.get(0);
                                    }
                                    break;
                                default:
                                    log.error("kongwei number error: {}", kongwei.size());
                            }
                        }
                    }
                }
            }

            if (firstJiaRu != null) {
                firstJiaRu.click();
                Thread.sleep(1000L);
            }
        }
    }
}
