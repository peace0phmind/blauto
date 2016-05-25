package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class HaoYou implements IDo {
    String baseDir = Common.BASE_DIR + "haoyou/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(Common.MENU);

        Match haoyou = region.exists(baseDir + "haoyou.png", 3);
        if (haoyou != null) {
            haoyou.click();

            Match aixing = region.exists(baseDir + "songaixing.png", 3);
            if (aixing != null && aixing.getScore() > 0.95) {
                Iterator<Match> all = region.findAll(baseDir + "songaixing.png");
                while (all.hasNext()) {
                    all.next().click();
                    Thread.sleep(500L);
                    region.click(baseDir + "xiaciba.png");
                    Thread.sleep(500L);
                }
            }

            Match haoyoufuli = region.exists(baseDir + "haoyoufuli.png");
            if (haoyoufuli != null) {
                haoyoufuli.click();

                Thread.sleep(1000L);

                Match quanbulingqu = region.exists(baseDir + "quanbulingqu.png");
                if (quanbulingqu != null) {
                    quanbulingqu.click();
                    Thread.sleep(500L);
                }

                region.click(baseDir + "guyong.png");
                Thread.sleep(1000L);
                quanbulingqu = region.exists(baseDir + "quanbulingqu.png");
                if (quanbulingqu != null) {
                    quanbulingqu.click();
                    Thread.sleep(500L);
                }

                region.click(baseDir + "hujiao.png");
                Thread.sleep(1000L);
                Match qingtashangxian = region.exists(baseDir + "qingtashangxian.png");
                if (qingtashangxian != null) {
                    Iterator<Match> all = region.findAll(baseDir + "qingtashangxian.png");
                    ArrayList<Match> allqingtashangxian = Lists.newArrayList(all);

                    allqingtashangxian.stream().sorted((a, b) -> b.y - a.y).forEach(x -> {
                        x.click();
                    });
                }

                Thread.sleep(500L);
                region.click(Common.CLOSE);
            }
        }

        status.Done(Task.HAO_YOU);

        region.click(Common.CLOSE);
        Thread.sleep(500L);
        region.click(Common.MENU1);

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.HAO_YOU, userName);
    }
}
