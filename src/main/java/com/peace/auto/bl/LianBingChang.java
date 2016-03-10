package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mind on 3/8/16.
 */
public class LianBingChang implements IDo {

    String baseDir = Common.BASE_DIR + "lianbingchang/";

    List<String> junduis = Arrays.asList(
            "houjun.png",
            "youjun.png",
            "zuojun.png");

    public void Do(Region region) throws FindFailed, InterruptedException {
        boolean keyilianbing = false;

        // 先查看是否有战报
        region.click(Common.MENU);

        Match zhanbao = region.exists(new Pattern(baseDir + "zhanbao.png").similar(0.95f));
        if (zhanbao != null) {
            zhanbao.click();

            Match kongzhanbao = region.exists(baseDir + "kongzhanbao.png", 6);
            if (kongzhanbao != null && kongzhanbao.getScore() > 0.95) {
                keyilianbing = true;
            }

            region.click(Common.CLOSE);
        }

        if (keyilianbing) {
            // 再进行练兵
            region.click(Common.RI_CHANG);

            Thread.sleep(3000L);

            Match lianbingchang = region.exists(baseDir + "lianbingchang.png");
            if (lianbingchang != null && lianbingchang.getScore() > 0.95) {
                lianbingchang.click();

                Match lianbingchangb = region.exists(baseDir + "lianbingchangbutton.png", 10);
                if (lianbingchangb != null) {
                    lianbingchangb.click();

                    for (String jundui : junduis) {
                        region.click(baseDir + jundui);
                        Thread.sleep(500L);

                        Match lianbing = region.exists(baseDir + "lianbing.png");
                        if (lianbing != null && isButtonEnable(lianbing)) {
                            lianbing.click();

                            region.click(baseDir + "quanbubuman.png");
                            region.click(baseDir + "quedingchuzheng.png");

                            break;
                        }
                    }

                    Thread.sleep(1000L);
                    region.click(Common.CLOSE);
                }
            }

            Thread.sleep(1000L);
            region.click(Common.CLOSE);
        }

    }
}
