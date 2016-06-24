package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

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
        Match shengyu = region.exists(baseDir + "shengyu.png", 10);
        log.debug("{}", shengyu);
        if (shengyu != null) {
            shengyu.click();

            // peace 开启新队列
            if (status.isPeace()) {
                Match kaiqixinduilie = region.exists(baseDir + "kaiqixinduilie.png", 3);
                if (kaiqixinduilie != null) {
                    Iterator<Match> all = region.findAll(baseDir + "kaiqixinduilie.png");
                    while (all.hasNext()) {
                        kaiqixinduilie = all.next();

                        if (kaiqixinduilie.getX() < 400) {
                            kaiqixinduilie.click();

                            Match goumaiduilie = region.exists(baseDir + "goumaiduilie.png", 3);
                            if (goumaiduilie != null) {
                                region.click(Common.QUE_DING);
                                Thread.sleep(3000L);
                            }
                        }
                    }
                }
            }

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

                        // 检查荣耀
                        checkRongYao(region);

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
        }

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.SHENG_YU, userName);
    }
}
