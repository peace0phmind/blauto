package com.peace.auto.bl;

import com.google.common.collect.Lists;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.Iterator;

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
            region.click(baseDir + "shuaxindongxue.png");

            Match goumai = region.exists(baseDir + "goumai.png");
            if (goumai != null) {
                Iterator<Match> all = region.findAll(Common.CLOSE);
                Lists.newArrayList(all).stream().sorted((a, b) -> a.x - b.x).findFirst().get().click();
            } else {
                region.click(baseDir + "yijiansaodang.png");
            }

            Thread.sleep(1000L);
            region.click(Common.CLOSE);

            status.Done(Task.HAI_DI_SHI_JIE);
        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.HAI_DI_SHI_JIE, userName);
    }
}