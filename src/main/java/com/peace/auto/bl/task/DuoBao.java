package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.TaskExecute;
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

    @Override
    public boolean CanDo(Status status, String userName) {
        return true;
    }

    public boolean xunbao(Region region) throws InterruptedException, FindFailed {
        return xunbao(region, true);
    }

    public boolean duobao(Region region, Region region1, Region region2, int roomNo) throws FindFailed, InterruptedException {
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

                Thread.sleep(2000L);

                Match chazhao = region.exists(baseDir + "chazhaofangjian.png");
                if (chazhao != null) {
                    // 持续寻宝
                    Match shurufangjian = region.exists(baseDir + "shurufangjianhaoma.png");
                    if (shurufangjian != null) {
                        shurufangjian.type(String.valueOf(roomNo));
                    }
                    chazhao.click();

                    xunbao(region1, region2, false, roomNo, (r) -> {
                        if (r != null) {
                            Thread.sleep(500L);
                            region.click(baseDir + "shuaxin.png");
                            Thread.sleep(1000L);
                            newRegion(region, r).click();
                        }
                    });
                }

                Thread.sleep(1000L);
                region.click(Common.CLOSE);
            }
            Thread.sleep(500L);
            region.click(Common.CLOSE);
        }

        return true;
    }

    public void xunbao(Region region1, Region region2, boolean tuOnly, int roomNo, TaskExecute t) throws InterruptedException, FindFailed {
        LocalTime now = LocalTime.now();
        if (!((now.isAfter(LocalTime.of(11, 30)) && now.isBefore(LocalTime.of(13, 55)))
                || (now.isAfter(LocalTime.of(21, 30)) && now.isBefore(LocalTime.of(23, 55))))) {
            return;
        }

        region1.click(Common.RI_CHANG);
        region2.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match tiaozhan1 = region1.exists(baseDir + "tiaozhan.png", 10);
        Match tiaozhan2 = region2.exists(baseDir + "tiaozhan.png", 10);
        if (tiaozhan1 != null && tiaozhan2 != null) {
            tiaozhan1.click();
            tiaozhan2.click();

            Thread.sleep(3000L);

            Match duobao1 = region1.exists(baseDir + "duobaoqibing.png", 20);
            Match duobao2 = region2.exists(baseDir + "duobaoqibing.png", 20);
            if (duobao1 != null && duobao2 != null) {
                duobao1.click();
                duobao2.click();

                region1.click(baseDir + "duobaoqibingjingru.png");
                region2.click(baseDir + "duobaoqibingjingru.png");

                // 领取宝图
                Match baotu1 = region1.exists(baseDir + "lingqubaotu.png");
                Match baotu2 = region2.exists(baseDir + "lingqubaotu.png");
                if (baotu1 != null && baotu2 != null) {
                    baotu1.click();
                    baotu2.click();

                    Thread.sleep(1000L);

                    if (!tuOnly) {
                        Match kaishi1 = region1.exists(baseDir + "kaishixunbao.png");
                        Match kaishi2 = region2.exists(baseDir + "kaishixunbao.png");
                        if (kaishi1 != null && kaishi2 != null) {
                            kaishi1.click();
                            kaishi2.click();
                        }
                    }
                }

                Thread.sleep(2000L);

                if (!tuOnly) {
                    Match chazhao1 = region1.exists(baseDir + "chazhaofangjian.png");
                    Match chazhao2 = region2.exists(baseDir + "chazhaofangjian.png");
                    if (chazhao1 != null && chazhao2 != null) {
                        // 持续寻宝
                        Match shurufangjian1 = region1.exists(baseDir + "shurufangjianhaoma.png");
                        Match shurufangjian2 = region2.exists(baseDir + "shurufangjianhaoma.png");
                        if (shurufangjian1 != null && shurufangjian2 != null) {
                            shurufangjian1.type(String.valueOf(roomNo));
                            shurufangjian2.type(String.valueOf(roomNo));
                        }
                        chazhao1.click();
                        chazhao2.click();

                        Thread.sleep(2000L);

                        Match ru1 = region1.exists(baseDir + "jiaru.png", 60);
                        Match ru2 = region2.exists(baseDir + "jiaru.png", 60);
                        if (ru1 != null && ru2 != null) {
                            Match jiaru1 = getFirstJiaRu(region1);
                            if (jiaru1 != null) {
                                jiaru1.click();
                                Thread.sleep(1000L);

                                region2.click(baseDir + "shuaxin.png");

                                Thread.sleep(3000L);

                                Match jiaru2 = getFirstJiaRu(region2);
                                if (jiaru2 != null) {
                                    jiaru2.click();

                                    if (t != null) {
                                        t.execute(jiaru2.getRect());
                                    }
                                }
                            }
                        }
                    }
                }

                Thread.sleep(1000L);
                region1.click(Common.CLOSE);
                region2.click(Common.CLOSE);
            }
            Thread.sleep(500L);
            region1.click(Common.CLOSE);
            region2.click(Common.CLOSE);
        }
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
                Match queding = region.exists(Common.QUE_DING);
                if (queding != null && queding.getScore() > 0.9) {
                    region.click(Common.QUE_DING);
                    Thread.sleep(1000L);
                }
            }

            Match firstJiaRu = getFirstJiaRu(region);

            if (firstJiaRu != null) {
                firstJiaRu.click();
                Thread.sleep(1000L);
            }
        }
    }

    private Match getFirstJiaRu(Region region) throws FindFailed {
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

        return firstJiaRu;
    }
}
