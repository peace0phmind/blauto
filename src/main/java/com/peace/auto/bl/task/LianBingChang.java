package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mind on 3/8/16.
 */
public class LianBingChang extends ZhanBao implements IDo {

    String baseDir = Common.BASE_DIR + "lianbingchang/";

    List<String> junduis = Arrays.asList(
            "houjun.png",
            "youjun.png",
            "zuojun.png",
            "qianjun.png",
            "zhongjun.png");

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (canFight(region, status)) {
            // 再进行练兵
            region.click(Common.RI_CHANG);

            Thread.sleep(3000L);

            Match lianbingchang = region.exists(baseDir + "lianbingchang.png");
            if (lianbingchang != null && lianbingchang.getScore() > 0.95) {
                lianbingchang.click();

                Match lianbingchangb = region.exists(baseDir + "lianbingchangbutton.png", 10);
                if (lianbingchangb != null) {
                    lianbingchangb.click();

                    Match kaiqi = region.exists(baseDir + "kaiqi.png");
                    if (kaiqi != null) {
                        kaiqi.click();
                        Match queding = region.exists(Common.QUE_DING);
                        if (queding != null) {
                            queding.click();
                            Thread.sleep(500L);
                        }
                    }

                    boolean canLianBing = false;
                    for (String jundui : junduis) {
                        Match jd = region.exists(baseDir + jundui);
                        if (jd != null) {
                            jd.click();
                            Thread.sleep(500L);

                            Match lianbing = region.exists(baseDir + "lianbing.png");
                            if (lianbing != null && isButtonEnable(lianbing)) {
                                lianbing.click();
                                canLianBing = true;

                                Match bunengtongshi = region.exists(baseDir + "bunengtongshichuzheng.png");
                                if (bunengtongshi != null) {
                                    region.click(Common.QUE_DING);
                                } else {
                                    region.click(baseDir + "quanbubuman.png");
                                    region.click(baseDir + "quedingchuzheng.png");
                                    status.Done(Task.LIAN_BING_CHANG);
                                }

                                break;
                            }
                        }
                    }

                    // 不能练兵,则下次不进行检查。
                    if (!canLianBing) {
                        status.Done(Task.LIAN_BING_CHANG, LocalDateTime.now());
                        status.Done(Task.LIAN_BING_CHANG, LocalDateTime.now());
                        status.Done(Task.LIAN_BING_CHANG, LocalDateTime.now());
                    }

                    Thread.sleep(2000L);
                    region.click(Common.CLOSE);
                }
            }

            Thread.sleep(2000L);
            region.click(Common.CLOSE);

            return true;
        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.LIAN_BING_CHANG, userName)
                && !super.canDo(status, userName)) {
            return false;
        }

        return true;
    }
}
