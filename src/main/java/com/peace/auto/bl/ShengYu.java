package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class ShengYu implements IDo {
    String baseDir = Common.BASE_DIR + "shengyu/";

    public boolean Done(Region region) throws FindFailed, InterruptedException {
        region.doubleClick(baseDir + "shengyu.png");

        Match inshengyu = region.exists(baseDir + "inshengyu.png", 10);
        if (inshengyu != null) {
            Iterator<Match> all = region.findAll(baseDir + "shengji.png");
            List<Match> list = new ArrayList<>();
            while (all.hasNext()) {
                list.add(all.next());
            }

            List<Match> sorted = list.stream().sorted((x, y) -> x.getX() - y.getX()).sorted((x, y) -> x.getY() - y.getY()).collect(Collectors.toList());

            shengyuLoop:
            for (Match shengji : sorted) {
                if (!isButtonEnable(shengji)) {
                    continue;
                }

                do {
                    shengji.click();

                    Match end = region.exists(baseDir + "shengyuend.png", 0.5);
                    if (end != null) {
                        region.click(Common.QUE_DING);
                        break shengyuLoop;
                    }
                } while (isButtonEnable(shengji));
            }
        }

        region.click(Common.CLOSE);

        return true;
    }
}
