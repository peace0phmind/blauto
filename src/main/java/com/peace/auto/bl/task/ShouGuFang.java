package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import com.peace.auto.bl.task.Common;
import com.peace.auto.bl.task.IDo;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class ShouGuFang implements IDo {
    private String baseDir = Common.BASE_DIR + "shougufang/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        // 进入兽骨坊
        region.doubleClick(baseDir + "shougufang.png");
        Match inshougufang = region.exists(baseDir + "inshougufang.png", 10);
        if (inshougufang == null) {

            Match shougufang = region.exists(baseDir + "shougufang.png", 0.5);
            if (shougufang != null) {
                region.doubleClick(baseDir + "shougufang.png");
                inshougufang = region.exists(baseDir + "inshougufang.png", 10);
                if (inshougufang == null) {
                    return false;
                }
            }
        }

        // 兽骨加工
        if (status.canDo(Task.SHOU_GU_JIA_GONG)) {
            shougujiagong(region, status);
        }

        // 兽骨狩猎
        if (status.canDo(Task.SHOU_GU_SHOU_LIE)) {
            region.click(baseDir + "shougushoulie.png");

            Match shuaxin = region.exists(baseDir + "shuaxin.png");
            if (shuaxin != null) {
                // 需要进行狩猎
                shoulie:
                for (int i = 0; i < 40; i++) {
                    Match daliang = region.exists(baseDir + "daliangniaolong.png");
                    if (daliang != null) {
                        Iterator<Match> all = region.findAll(baseDir + "daliangniaolong.png");
                        while (all.hasNext()) {
                            Match liewu = all.next();
                            if (liewu.getScore() > 0.95) {
                                Color pixelColor = getPixelColor(liewu, 54, 5);
                                // [r=220,g=117,b=223] 大量
                                // [r=216,g=202,b=153] 小量
                                // [r=91,g=71,b=48] 一群
                                if (pixelColor.getRed() > 200 && pixelColor.getGreen() < 120) {
                                    liewu.below().click(baseDir + "kaishishoulie.png");
                                    status.Done(Task.SHOU_GU_SHOU_LIE);
                                    Thread.sleep(1000L);
                                    break shoulie;
                                }
                            }
                        }
                    }

                    shuaxin.click();
                    Thread.sleep(2000L);
                }
            } else {
                getFinishTime(region);
                status.Done(Task.SHOU_GU_SHOU_LIE, Status.nextCheck());
            }
        }

        region.click(Common.CLOSE);

        return true;
    }

    private LocalDateTime getFinishTime(Region region) throws FindFailed {
        Match shouliezhong = region.exists(baseDir + "shouliezhong.png");
        if (shouliezhong != null) {
            shouliezhong.left(30).saveScreenCapture(".", "time");
            log.info("{}", getTime(shouliezhong.left(30), 100));
        }

        return Status.nextCheck();
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.SHOU_GU_JIA_GONG, userName)
                && !status.canDo(Task.SHOU_GU_SHOU_LIE, userName)) {
            return false;
        }

        return true;
    }

    /**
     * 兽骨加工
     *
     * @param region
     * @throws FindFailed
     */
    private void shougujiagong(Region region, Status status) throws FindFailed {
        Match shouhuogupian = region.exists(baseDir + "shouhuogupian.png", 3);
        if (shouhuogupian != null) {
            // 进行幸运翻倍
            Match xinyunfanbei = region.exists(baseDir + "xinyunfanbei.png", 0.5);
            if (xinyunfanbei != null) {
                for (int i = 0; i < 3; i++) {
                    xinyunfanbei.click();
                    status.Done(Task.SHOU_GU_JIA_GONG);

                    Match noxinyunfanbei = region.exists(baseDir + "noxinyunfanbei.png");
                    if (noxinyunfanbei != null) {
                        region.click(Common.QUE_DING);
                        break;
                    }
                }
            }
        } else {
            Match jintigudao = region.exists(baseDir + "jintigudao.png", 3);
            if (jintigudao != null) {
                jintigudao.click();

                Match goumaijindao = region.exists(baseDir + "goumaijindao.png", 3);
                if (goumaijindao != null) {
                    region.click(Common.QU_XIAO);
                } else {
                    shougujiagong(region, status);
                }
            }
        }
    }
}
