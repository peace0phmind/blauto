package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class JingJiChang implements IDo {
    String baseDir = Common.BASE_DIR + "jingjichang/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match jingjichang = region.exists(baseDir + "jingjichang.png", 10);
        if (jingjichang != null && jingjichang.getScore() > 0.95) {
            jingjichang.click();

            Match injingjichang = region.exists(baseDir + "injingjichang.png", 30);
            if (injingjichang != null) {
                Thread.sleep(1000L);
                // 每天首次执行,领取奖励
                if (status.canDo(Task.JING_JI_CHANG_LING_QU)) {
                    Match lingqujiangli = region.exists(baseDir + "lingqujiangli.png", 3);
                    if (lingqujiangli != null) {
                        lingqujiangli.click();
                        status.Done(Task.JING_JI_CHANG_LING_QU);
                    }
                }

                // 挑战
                Thread.sleep(1000L);
                Iterator<Match> all = region.findAll(baseDir + "tiaozhan.png");
                List<Match> tiaozhans = Lists.newArrayList(all).stream().sorted((x, y) -> x.getX() - y.getX()).collect(Collectors.toList());
                log.info("Tiao zhan size: {}", tiaozhans.size());
                tiaozhans.get((int) status.todayFinishCount(Task.JING_JI_CHANG) % tiaozhans.size()).click();
                Thread.sleep(1000L);

                Match jingjivip = region.exists(baseDir + "jingjivip.png");
                if (jingjivip != null) {
                    // 结束了
                    jingjivip.above().click(Common.CLOSE);
                    status.Done(Task.JING_JI_CHANG, LocalDateTime.now());
                } else {
                    // 没有结束
                    Match queding = region.exists(baseDir + "queding.png", 3);
                    if (queding != null) {
                        queding.click();

                        Thread.sleep(6000L);
                        checkRongYao(region);

                        Match guankan = region.exists(baseDir + "guankan.png", 6);
                        if (guankan != null) {
                            region.click(Common.QU_XIAO);

                            Match jieguo = region.exists(baseDir + "jieguo.png", 3);
                            if (jieguo != null) {
                                region.click(Common.QUE_DING);
                                status.Done(Task.JING_JI_CHANG);
                            }
                        }
                    }
                }
            }
        }

        Thread.sleep(500L);

        region.click(Common.CLOSE);

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (LocalTime.now().isAfter(LocalTime.of(23, 45)) || LocalTime.now().isBefore(LocalTime.of(0, 15))) {
            return false;
        }

        if (!status.canDo(Task.JING_JI_CHANG_LING_QU, userName)
                && !status.canDo(Task.JING_JI_CHANG, userName)) {
            return false;
        }

        return true;
    }
}
