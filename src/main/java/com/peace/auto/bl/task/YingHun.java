package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.time.LocalDate;

/**
 * Created by mind on 3/6/16.
 */
public class YingHun implements IDo {
    String baseDir = Common.BASE_DIR + "yinghun/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(Common.MENU);

        Match yinghun = region.exists(baseDir + "yinghun.png", 3);
        if (yinghun != null) {
            yinghun.click();

            Thread.sleep(1000L);

            if (status.canDo(Task.YING_HUN)) {
                Match zhaohuan = region.exists(baseDir + "zhaohuanyinghun.png", 10);
                if (zhaohuan != null) {
                    zhaohuan.click();

                    Match meiri = region.exists(baseDir + "meirilingqu.png", 10);
                    if (meiri != null && isButtonEnable(meiri)) {
                        meiri.click();
                        status.Done(Task.YING_HUN);
                    }

                    region.click(Common.CLOSE);
                    Thread.sleep(500L);
                }
            }

            if (status.canDo(Task.RONG_LIAN)) {
                Match shengji = region.exists(baseDir + "shengji.png", 10);
                if (shengji != null) {
                    shengji.click();

                    Match ronglian = region.exists(baseDir + "ronglian.png", 10);
                    if (ronglian != null) {
                        ronglian.click();

                        Match rightButton = region.exists(baseDir + "rightButton.png", 10);
                        if (rightButton != null) {
                            for (int i = 0; i < LocalDate.now().getDayOfYear() % 4; i++) {
                                rightButton.click();
                                Thread.sleep(500L);
                            }

                            Match putongronglian = region.exists(baseDir + "putongronglian.png", 10);
                            if (putongronglian != null) {
                                for (int i = 0; i < 1000; i++) {
                                    putongronglian.click();
                                    checkRongYao(region);
                                    Match xiuliancishu = region.exists(baseDir + "xiuliancishu.png", 0.5);
                                    if (xiuliancishu != null) {
                                        region.click(Common.QUE_DING);
                                        status.Done(Task.RONG_LIAN);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Thread.sleep(1000L);
                    region.click(Common.CLOSE);
                }
            }
        }

        Thread.sleep(1000L);
        region.click(Common.CLOSE);

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.YING_HUN, userName)
                && !status.canDo(Task.RONG_LIAN, userName)) {
            return false;
        }

        return true;
    }
}
