package com.peace.auto.bl;

import com.google.common.collect.Lists;
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

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.doubleClick(baseDir + "shengyu.png");

        Match inshengyu = region.exists(baseDir + "inshengyu.png", 10);
        if (inshengyu != null) {
            List<Match> list = Lists.newArrayList(region.findAll(baseDir + "shengji.png"));
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

            status.Done(Task.SHENG_YU);
        }

        region.click(Common.CLOSE);

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.SHENG_YU, userName);
    }
}
