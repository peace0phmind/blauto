package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.Iterator;

/**
 * Created by mind on 3/9/16.
 */
@Slf4j
public class ShiChang implements IDo {

    String baseDir = Common.BASE_DIR + "shichang/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
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
                        Region left = jixu.left(210).below(150).offset(new Location(50, -40));
//                        left.saveScreenCapture("/Users/mind/peace/blauto", "tt");

                        Match huoju = left.exists(baseDir + "huoju.png");
                        if (huoju != null) {
                            continue;
                        }

                        Match shengjing = left.exists(baseDir + "shengjing.png");
                        if (shengjing != null) {
                            log.info("find shengjing: {}", shengjing);
                            continue;
                        }

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

        return true;
    }
}
