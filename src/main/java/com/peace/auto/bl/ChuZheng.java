package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mind on 3/9/16.
 */
public class ChuZheng implements IDo {
    String baseDir = Common.BASE_DIR + "chuzheng/";

    List<String> direns = Arrays.asList(
            "anfenglieshou.png",
            "xieyuemanbing.png",
            "eleipaoshou.png",
            "heiwuyaowu.png"
    );

    public void Do(Region region) throws FindFailed, InterruptedException {
        if (isTodayFirstFinished()) {
            return;
        }

        boolean keyichuzheng = false;

        // 先查看是否有战报
        region.click(Common.MENU);

        Match zhanbao = region.exists(new Pattern(baseDir + "zhanbao.png").similar(0.95f));
        if (zhanbao != null) {
            zhanbao.click();

            Match kongzhanbao = region.exists(baseDir + "kongzhanbao.png", 6);
            if (kongzhanbao != null && kongzhanbao.getScore() > 0.95) {
                keyichuzheng = true;
            }

            region.click(Common.CLOSE);
        }


        if (keyichuzheng) {
            region.click(Common.MENU);

            Match chuzheng = region.exists(baseDir + "chuzheng.png");
            if (chuzheng != null) {
                chuzheng.click();
                Thread.sleep(1000L);

                Match chuzhenganniu = region.exists(baseDir + "chuzhenganniu.png");
                if (chuzhenganniu != null) {
                    String diren = direns.get(LocalDate.now().getDayOfYear() % 4);

                    region.click(baseDir + diren);
                    Thread.sleep(1000L);

                    chuzhenganniu.aboveAt(-50).click();
                    Thread.sleep(1000L);

                    chuzhenganniu.click();

                    Match zidongbubing = region.exists(baseDir + "zidongbubing.png");
                    if (zidongbubing != null) {
                        zidongbubing.click();

                        for (int i = 0; i < 5; i++) {
                            region.click(baseDir + "tianjiacishu.png");
                            Thread.sleep(500L);
                        }

                        region.click(baseDir + "quanbubuman.png");

                        region.click(baseDir + "quedingchuzheng.png");

                        Thread.sleep(1000L);
                        // 出征成功, 点击close
                        region.click(Common.CLOSE);
                    }
                }
            }
        }
    }
}
