package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 5/14/16.
 */
public class HaiDiShiJie implements IDo {
    String baseDir = Common.BASE_DIR + "haidishijie/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        Match haidishijie = region.exists(baseDir + "haidishijie.png", 30);
        if (haidishijie != null) {
            haidishijie.click();

            Thread.sleep(1000L);

            if (status.canDo(Task.HAI_DI_SHI_JIE_TIAO_ZHAN)) {
                Match tiaozhan = region.exists(baseDir + "tiaozhan.png");
                if (tiaozhan != null) {
                    for (int i = 0; i < 30; i++) {
                        tiaozhan.click();

                        Match quanbubuman = region.exists(baseDir + "quanbubuman.png");
                        if (quanbubuman != null) {
                            quanbubuman.click();
                            Thread.sleep(1000L);
                            region.click(baseDir + "queding.png");
                            Thread.sleep(3000L);
                        }

                        Match wufatiaozhan = region.exists(baseDir + "wufatiaozhan.png");
                        if (wufatiaozhan != null) {
                            region.click(Common.QUE_DING);
                            break;
                        }
                    }
                }

                status.Done(Task.HAI_DI_SHI_JIE_TIAO_ZHAN);
            }


            if (status.canDo(Task.HAI_DI_SHI_JIE_SAO_DANG)) {
                region.click(baseDir + "shuaxindongxue.png");

                Match goumai = region.exists(baseDir + "goumai.png");
                if (goumai != null) {
                    clickInside(region, Common.CLOSE);
                } else {
                    region.click(baseDir + "yijiansaodang.png");
                }

                status.Done(Task.HAI_DI_SHI_JIE_SAO_DANG);
            }

            Thread.sleep(1000L);
            region.click(Common.CLOSE);

        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.HAI_DI_SHI_JIE_SAO_DANG, userName) &&
                !status.canDo(Task.HAI_DI_SHI_JIE_TIAO_ZHAN, userName)) {
            return false;
        }

        return true;
    }
}