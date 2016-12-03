package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
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
        region.click(Common.RI_CHANG);

        Thread.sleep(6000L);

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
                    Thread.sleep(3000L);
                }

                if (!duoqu(region, status, "huguo.png")) {
                    if (!duoqu(region, status, "fengguo.png")) {
                        duoqu(region, status, "langguo.png");
                    }
                }

                region.click(Common.CLOSE);

                Thread.sleep(3000L);

                region.click(Common.CLOSE);
            }
        }
        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        LocalTime now = LocalTime.now();
        if (!((now.isAfter(LocalTime.of(11, 30)) && now.isBefore(LocalTime.of(13, 59)))
                || (now.isAfter(LocalTime.of(20, 30)) && now.isBefore(LocalTime.of(22, 59))))) {
            return false;
        }

        return status.canDo(Task.SHENG_HUO, userName);
    }

    private boolean duoqu(Region region, Status status, String guojia) throws FindFailed, InterruptedException {
        boolean ret = false;
        Match gj = region.exists(baseDir + guojia, 6);
        if (gj != null) {
            gj.click();

            Match huo = region.exists(baseDir + "huo.png", 6);
            if (huo != null) {
                huo.click();

                Match quedingduoqu = region.exists(baseDir + "quedingduoqu.png", 6);
                if (quedingduoqu != null) {
                    region.click(Common.QUE_DING);

                    Match duoquwan = region.exists(baseDir + "duoquwan.png");
                    if (duoquwan != null) {
                        region.click(Common.QUE_DING);
                    } else {
                        status.Done(Task.SHENG_HUO);
                        ret = true;
                    }
                }
            }

            Thread.sleep(3000L);
            region.click(baseDir + "fanhui.png");
            Thread.sleep(3000L);
        }

        return ret;
    }
}
