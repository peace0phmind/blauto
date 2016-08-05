package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.time.LocalTime;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class ShengLingQuan implements IDo {
    String baseDir = Common.BASE_DIR + "shenglingquan/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        Match richang = region.exists(Common.RI_CHANG);
        if (richang != null) {
            richang.click();

            Thread.sleep(3000L);

            Match shenglingquan = region.exists(baseDir + "shenglingquan.png", 30);
            if (shenglingquan != null) {
                shenglingquan.click();

                // 神灵泉
                if (status.canDo(Task.SHENG_LING_QUAN_XI_LIAN)) {
                    putongxilian(region, true);

                    if (status.canDo(Task.SHENG_LING_QUAN_MIAN_FEI)) {
                        region.click(baseDir + "dingjishenshui.png");

                        Match zhixingmianfeicaozuo = region.exists(baseDir + "zhixingmianfeicaozuo.png", 6);
                        if (zhixingmianfeicaozuo != null) {
                            Thread.sleep(2000L);
                            Match queding = region.exists(Common.QUE_DING, 10);
                            if (queding != null) {
                                Thread.sleep(2000L);
                                region.click(Common.QUE_DING);
                                status.Done(Task.SHENG_LING_QUAN_MIAN_FEI);
                            }

                            Thread.sleep(2000L);
                            putongxilian(region, true);
                        } else {
                            region.click(Common.QU_XIAO);
                            status.Done(Task.SHENG_LING_QUAN_MIAN_FEI);
                        }
                    }

                    if (LocalTime.now().isAfter(LocalTime.of(22, 0))) {
                        putongxilian(region, false);
                    }

                    status.Done(Task.SHENG_LING_QUAN_XI_LIAN, Status.nextRefresh());
                }

                // 高级修炼
                if (status.canDo(Task.SHENG_LING_QUAN_XIU_LIAN)) {
                    Match xiulian = region.exists(baseDir + "xiulian.png", 3);
                    if (xiulian != null) {
                        xiulian.click();

                        Match kaishixiulian = region.exists(baseDir + "kaishixiulian.png", 3);
                        if (kaishixiulian != null && kaishixiulian.getScore() > 0.95) {
                            Thread.sleep(1000L);
                            region.click(baseDir + "gaojixiulian.png");
                            Thread.sleep(1000L);
                            region.click(baseDir + "kaishixiulian.png");

                            Match goumai = region.exists(baseDir + "goumai.png", 3);
                            if (goumai != null) {
                                region.click(Common.QUE_DING);
                                status.Done(Task.SHENG_LING_QUAN_XIU_LIAN);
                            }
                        } else {
                            status.Done(Task.SHENG_LING_QUAN_XIU_LIAN, Status.nextCheck());
                        }
                    }
                }

                region.click(Common.CLOSE);
                Thread.sleep(500L);
            }

            region.click(Common.CLOSE);
            return true;
        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.SHENG_LING_QUAN_XIU_LIAN, userName)
                && !status.canDo(Task.SHENG_LING_QUAN_XI_LIAN, userName)) {
            return false;
        }

        return true;
    }

    private void putongxilian(Region region, boolean needShengping) throws FindFailed, InterruptedException {
        Match shengpin = null;
        for (int i = 0; i < 60; i++) {
            if (needShengping) {
                shengpin = region.exists(baseDir + "shengpin", 3);
            }

            if (!needShengping
                    || (needShengping && shengpin != null && shengpin.getScore() > 0.95)) {
                region.click(baseDir + "putongxiulian.png");
                Match end = region.exists(baseDir + "xilianend.png", 1);
                if (end != null) {
                    Thread.sleep(1000L);
                    region.click(Common.QUE_DING);
                    break;
                }
            }
        }
    }
}
