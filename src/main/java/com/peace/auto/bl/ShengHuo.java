package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.time.LocalTime;

/**
 * Created by mind on 3/25/16.
 */
@Slf4j
public class ShengHuo implements IDo {
    String baseDir = Common.BASE_DIR + "shenghuo/";

    @Override
    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        LocalTime now = LocalTime.now();
        if (!((now.isAfter(LocalTime.of(11, 30)) && now.isBefore(LocalTime.of(14, 0)))
                || (now.isAfter(LocalTime.of(20, 30)) && now.isBefore(LocalTime.of(23, 0))))) {
            return false;
        }

        if (!status.canDo(Task.SHENG_HUO)) {
            return false;
        }

        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match tiaozhan = region.exists(baseDir + "tiaozhan.png", 10);
        if (tiaozhan != null) {
            tiaozhan.click();

            Thread.sleep(3000L);

            Match shenghuozhan = region.exists(baseDir + "shenghuozhengduozhan.png", 10);
            if (shenghuozhan != null) {
                shenghuozhan.click();

                Thread.sleep(1000L);

                region.click(baseDir + "shenghuozhenduo.png");

                Match lingqu = region.exists(baseDir + "lingqu.png", 6);
                if (lingqu != null && isButtonEnable(lingqu)) {
                    lingqu.click();
                }

                Match langguo = region.exists(baseDir + "langguo.png", 6);
                if (langguo != null) {
                    langguo.click();

                    Match huo = region.exists(baseDir + "huo.png", 6);
                    if (huo != null) {
                        huo.click();

                        Match quedingduoqu = region.exists(baseDir + "quedingduoqu.png", 6);
                        if (quedingduoqu != null) {
                            region.click(Common.QUE_DING);

                            status.Done(Task.SHENG_HUO);

                            Thread.sleep(1000L);

                            region.click(baseDir + "fanhui.png");

                            Thread.sleep(1000L);
                        }
                    }
                }

                region.click(Common.CLOSE);

                Thread.sleep(1000L);

                region.click(Common.CLOSE);
            }
        }
        return false;
    }
}
