package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by mind on 3/6/16.
 */
@Slf4j
public class TianSheng implements IDo {
    String baseDir = Common.BASE_DIR + "tiansheng/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        Match tiansheng = region.exists(baseDir + "tiansheng.png", 3);
        if (tiansheng != null && tiansheng.getScore() > 0.9) {
            tiansheng.click();
            Thread.sleep(100L);

            // 祈祷
            Match qidao = region.exists(baseDir + "qidao.png", 3);
            if (qidao != null) {
                qidao.click();
                Thread.sleep(1000L);

                Pattern mfp = new Pattern(baseDir + "mianfei.png").similar(0.95f);
                Match mianfei = region.exists(mfp, 3);
                if (mianfei != null) {
                    region.click(baseDir + "guanbijieguo.png");
                    Thread.sleep(500L);

                    Iterator<Match> all = region.findAll(mfp);
                    while (all.hasNext()) {
                        Match mf = all.next();
                        mf.below().click(baseDir + "qidaoanniu.png");
                        Thread.sleep(500L);
                    }
                }

                region.click(Common.CLOSE);
            }

            // 远古战场
            Match yuanguzhanchang = region.exists(baseDir + "yuanguzhanchang.png");
            if (yuanguzhanchang != null) {
                yuanguzhanchang.click();
                Thread.sleep(1000L);

                Match sanxing = region.exists(new Pattern(baseDir + "sanxing.png").similar(0.9f));
                if (sanxing != null) {
                    Iterator<Match> allsanxing = region.findAll(new Pattern(baseDir + "sanxing.png").similar(0.9f));
                    List<Match> sanxings = new ArrayList<>();
                    while (allsanxing.hasNext()) {
                        sanxings.add(allsanxing.next());
                    }

                    Optional<Match> lastsanxing = sanxings.stream().sorted((x, y) -> y.getX() - x.getX()).sorted((x, y) -> y.getY() - x.getY()).findFirst();
                    if (lastsanxing.isPresent()) {
                        Match sx = lastsanxing.get();
                        sx.click();
                        Thread.sleep(500L);

                        for (int i = 0; i < 25; i++) {
                            region.click(baseDir + "saodang.png");
                            Match shenglibugou = region.exists(baseDir + "shenglibugou.png", 1);
                            if (shenglibugou != null) {
                                Thread.sleep(500L);
                                region.click(Common.QUE_DING);
                                break;
                            }
                        }
                    }
                }

                Thread.sleep(500L);
                region.click(Common.CLOSE);
            }

            Thread.sleep(500L);
            region.click(Common.CLOSE);
        }
    }
}
