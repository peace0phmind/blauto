package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.Iterator;

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

            region.click(Common.CLOSE);
        }
    }
}
