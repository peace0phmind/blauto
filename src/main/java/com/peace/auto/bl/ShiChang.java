package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.Iterator;

/**
 * Created by mind on 3/9/16.
 */
public class ShiChang implements IDo {

    String baseDir = Common.BASE_DIR + "shichang/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        Match shichang = region.exists(baseDir + "shichang.png");

        if (shichang != null) {
            shichang.click();

            Match jiaoyi = region.exists(baseDir + "jiaoyi.png");
            if (jiaoyi != null) {

                Match jixu = region.exists(baseDir + "jixu.png");
                if (jixu != null) {
                    Iterator<Match> alljixu = region.findAll(baseDir + "jixu.png");
                    while (alljixu.hasNext()) {
                        jixu = alljixu.next();
                        Region left = jixu.leftAt(-80).below(60).grow(160, 120);
                        left.highlight(3);

                        jiaoyi = left.exists(baseDir + "jiaoyi.png");
                        if (jiaoyi != null) {
                            jiaoyi.click();

                            Match wupingbuzu = region.exists(baseDir + "wupingbuzu.png");
                            if (wupingbuzu != null) {
                                region.click(Common.QUE_DING);
                            }
                        }
                    }
                }
            }

            region.click(Common.CLOSE);
        }
    }
}
