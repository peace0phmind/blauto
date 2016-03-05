package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class NongChang {
    static public void Do(Screen screen) {
        String baseDir = Common.BASE_DIR + "nongchang/";

        try {
            screen.doubleClick(baseDir + "nongchang.png");

            Match innongchang = screen.exists(baseDir + "innongchang.png", 30);

            if (innongchang != null) {
                // 收获
                Match shouhuo = screen.exists(baseDir + "shouhuo.png", 0.5);
                if (shouhuo != null) {
                    shouhuo.click();
                }

                Match bozhong = screen.exists(baseDir + "bozhong.png", 0.5);
                if (bozhong != null) {
                    bozhong.click();

                    Match zhongzhi = screen.exists(baseDir + "mianfeizhongzhi.png");
                    if (zhongzhi != null) {
                        zhongzhi.click();

//                        screen.click(Common.CLOSE);
                    }
                }

                Thread.sleep(500L);

                // 喂食
                Match weishi = screen.exists(baseDir + "weishi.png", 0.5);
                if (weishi != null) {
                    weishi.click();
                }
            }

            screen.click(Common.HUI_CHENG);
            Thread.sleep(1000L);

        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
