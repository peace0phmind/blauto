package com.peace.auto.bl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

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

    public boolean Done(Region region) throws FindFailed, InterruptedException {
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

            // 喂食
            Match weishi = region.exists(baseDir + "weishi.png", 0.5);
            if (weishi != null) {
                weishi.click();
            }


            Match qitanongchang = region.exists(baseDir + "qitanongchang.png");
            if (qitanongchang != null) {
                qitanongchang.click();

                Thread.sleep(1000L);

                Match wei = region.exists(baseDir + "wei.png");
                if (wei != null) {
                    Iterator<Match> all = region.findAll(baseDir + "wei.png");
                    while (all.hasNext()) {
                        all.next().click();
                        Thread.sleep(1000L);

                        weishi = region.exists(baseDir + "weishi.png");
                        if (weishi != null) {
                            weishi.click();
                            Thread.sleep(500L);
                        }
                    }
                }
            }
        }

        Thread.sleep(500L);
        region.click(Common.HUI_CHENG);

        return true;
    }
}
