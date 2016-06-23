package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.ExecuteTask;
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
        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        LocalTime now = LocalTime.now();
        if (!((now.isAfter(LocalTime.of(11, 30)) && now.isBefore(LocalTime.of(13, 55)))
                || (now.isAfter(LocalTime.of(21, 30)) && now.isBefore(LocalTime.of(23, 55))))) {
            return false;
        }

        return true;
    }

    public boolean duobao(Region region, Region region1, Region region2, int roomNo) throws FindFailed, InterruptedException {
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

    public void xunbao(Region region1, Region region2, boolean tuOnly, int roomNo, ExecuteTask t) throws InterruptedException, FindFailed {
        if (!tuOnly) {
            LocalTime now = LocalTime.now();
            if (!((now.isAfter(LocalTime.of(11, 30)) && now.isBefore(LocalTime.of(13, 55)))
                    || (now.isAfter(LocalTime.of(21, 30)) && now.isBefore(LocalTime.of(23, 55))))) {
                return;
            }
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
                                Thread.sleep(500L);

                                region2.click(baseDir + "shuaxin.png");

                                Thread.sleep(2000L);

                                Match jiaru2 = getFirstJiaRu(region2);
                                if (jiaru2 != null) {
                                    jiaru2.click();

                                    if (t != null) {
                                        t.execute(jiaru2.getRect());

                                        Thread.sleep(6000L);
                                        region2.click(baseDir + "shuaxin.png");
                                        Thread.sleep(6000L);
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
