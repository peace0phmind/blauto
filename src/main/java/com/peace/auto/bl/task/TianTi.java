package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.time.LocalTime;

/**
 * Created by mind on 03/10/2016.
 */
@Slf4j
public class TianTi implements IDo {
    private static final String baseDir = Common.BASE_DIR + "tianti/";

    @Override
    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        Match shenhuntianti = region.exists(new Pattern(baseDir + "shenhuntianti.png").similar(0.98f));

        boolean result = false;

        if (shenhuntianti != null) {
            shenhuntianti.click();


            if (status.canDo(Task.TIAN_TI_ZHAN_DOU)) {
                result = result || zhanDou(region, status);
            }

            Thread.sleep(3000L);

            if (status.canDo(Task.TIAN_TI_LING_QU)) {
                result = result || lingQu(region, status);
            }

            Thread.sleep(1000L);
            region.click(baseDir + "close.png");
        }

        return result;
    }

    private boolean zhanDou(Region region, Status status) throws InterruptedException, FindFailed {
        long count = Task.TIAN_TI_ZHAN_DOU.getDayLimit(status.getCurrentUser()) - status.todayFinishCount(Task.TIAN_TI_ZHAN_DOU);
        for (int i = 0; i < count; i++) {
            Match kaishipipai = region.exists(baseDir + "kaishipipei.png");
            if (kaishipipai != null) {
                kaishipipai.click();

                Match ciShuBuZu = region.exists(baseDir + "tiaozhancishubuzu.png", 2);
                if (ciShuBuZu != null) {
                    region.click(Common.QUE_DING);
                    Thread.sleep(1000L);

                    for (int j = 0; j < count - i; j++) {
                        status.Done(Task.TIAN_TI_ZHAN_DOU);
                    }

                    break;
                }

                Match touxiang = region.exists(baseDir + "touxiang.png", 30);
                if (touxiang != null) {
                    touxiang.click();

                    Match tiaoguo = region.exists(baseDir + "tiaoguo.png");
                    if (tiaoguo != null) {
                        tiaoguo.click();

                        Match jixuyouxi = region.exists(baseDir + "dianjirenyiweizhijixuyouxi.png", 10);
                        if (jixuyouxi != null) {
                            jixuyouxi.click();
                            Thread.sleep(1000L);

                            status.Done(Task.TIAN_TI_ZHAN_DOU);
                        } else {
                            region.click();
                        }

                        // 升级提示
                        jixuyouxi = region.exists(baseDir + "dianjirenyiweizhijixuyouxi.png");
                        if (jixuyouxi != null) {
                            jixuyouxi.click();
                            Thread.sleep(1000L);
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean lingQu(Region region, Status status) throws FindFailed, InterruptedException {
        Match houhuodejiangli = region.exists(baseDir + "houhuodejiangli.png");
        if (houhuodejiangli != null) {
            status.Done(Task.TIAN_TI_LING_QU, Status.nextCheck());
        } else {
            Match lingqu = region.exists(baseDir + "lingqu.png");
            if (lingqu != null) {
                lingqu.click();

                Match jianglilengque = region.exists(baseDir + "jianglilenque.png");
                if (jianglilengque != null) {
                    region.click(Common.QUE_DING);
                }

                Match jixuyouxi = region.exists(baseDir + "dianjirenyiweizhijixuyouxi.png", 3);
                if (jixuyouxi != null) {
                    jixuyouxi.click();
                    Thread.sleep(1000L);
                }

                status.Done(Task.TIAN_TI_LING_QU);
            } else {
                log.info("lingqu is null: {}", lingqu);
            }
        }
        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        boolean result = false;
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.of(20, 0)) && now.isBefore(LocalTime.MAX)) {
            result = result || status.canDo(Task.TIAN_TI_ZHAN_DOU, userName);
        }

        return result || status.canDo(Task.TIAN_TI_LING_QU, userName);
    }
}
