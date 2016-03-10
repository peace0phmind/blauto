package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
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
        region.click(Common.MENU);

        Match chuzheng = region.exists(baseDir + "chuzheng.png");
        if (chuzheng != null) {
            chuzheng.click();

            Match chuzhenganniu = region.exists(baseDir + "chuzhenganniu.png");
            if (chuzhenganniu != null) {
                String diren = direns.get(LocalDate.now().getDayOfYear() % 4);

                region.click(baseDir + diren);
                Thread.sleep(1000L);

                chuzhenganniu.aboveAt(20).click();
                Thread.sleep(1000L);

                chuzhenganniu.click();

                Match zidongbubing = region.exists(baseDir + "zidongbubing.png");
                if (zidongbubing != null) {
                    // 出征成功, 点击close
                    region.click(Common.CLOSE);

                } else {

                }
            }
        }

    }
}
