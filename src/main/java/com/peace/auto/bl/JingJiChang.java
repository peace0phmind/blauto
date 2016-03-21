package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.time.LocalTime;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class JingJiChang implements IDo {
    String baseDir = Common.BASE_DIR + "jingjichang/";

    public boolean Done(Region region) throws FindFailed, InterruptedException {
        if (LocalTime.now().isAfter(LocalTime.of(23, 45)) || LocalTime.now().isBefore(LocalTime.of(0, 15))) {
            return false;
        }

        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match jingjichang = region.exists(baseDir + "jingjichang.png", 10);
        if (jingjichang != null && jingjichang.getScore() > 0.95) {
            jingjichang.click();

            Match injingjichang = region.exists(baseDir + "injingjichang.png", 30);
            if (injingjichang != null) {
                // 领取奖励
                Match lingqujiangli = region.exists(baseDir + "lingqujiangli.png", 3);
                if (lingqujiangli != null) {
                    lingqujiangli.click();
                }

                // 挑战
                region.click(baseDir + "tiaozhan.png");
                Match jingjivip = region.exists(baseDir + "jingjivip.png");
                if (jingjivip != null) {
                    // 结束了
                    jingjivip.above().click(Common.CLOSE);
                } else {
                    // 没有结束
                    Match queding = region.exists(baseDir + "queding.png", 3);
                    if (queding != null) {
                        queding.click();

                        Match guankan = region.exists(baseDir + "guankan.png", 3);
                        if (guankan != null) {
                            region.click(Common.QU_XIAO);

                            Match jieguo = region.exists(baseDir + "jieguo.png", 3);
                            if (jieguo != null) {
                                region.click(Common.QUE_DING);
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
}
