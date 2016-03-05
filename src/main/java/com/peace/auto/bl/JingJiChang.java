package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class JingJiChang {
    static public void Do(Region region) {
        String baseDir = Common.BASE_DIR + "jingjichang/";

        try {
            region.click(Common.RI_CHANG);

            Match jingjichang = region.exists(baseDir + "jingjichang.png", 30);
            if (jingjichang != null) {
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

            region.click(Common.CLOSE);
            Thread.sleep(500L);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
