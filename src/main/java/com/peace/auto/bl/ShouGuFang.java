package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class ShouGuFang implements IDo {
    public static String baseDir = Common.BASE_DIR + "shougufang/";

    public void Do(Region region) throws FindFailed, InterruptedException {

        // 进入兽骨坊
        region.doubleClick(baseDir + "shougufang.png");
        Match inshougufang = region.exists(baseDir + "inshougufang.png", 10);
        if (inshougufang == null) {

            Match shougufang = region.exists(baseDir + "shougufang.png", 0.5);
            if (shougufang != null) {
                region.doubleClick(baseDir + "shougufang.png");
                inshougufang = region.exists(baseDir + "inshougufang.png", 10);
                if (inshougufang == null) {
                    return;
                }
            }
        }

        // 兽骨加工
        shougujiagong(region);

        // 兽骨狩猎
        region.click(baseDir + "shougushoulie.png");

        Match shuaxin = region.exists(baseDir + "shuaxin.png", 0.5);
        if (shuaxin != null) {
            // 需要进行狩猎
            for (int i = 0; i < 3; i++) {
                Match daliang = region.exists(baseDir + "daliangniaolong.png", 0.5);
                if (daliang != null) {
                    log.info("{}", daliang);
                    daliang.below().click(baseDir + "kaishishoulie.png");
                    break;
                }

                shuaxin.click();
                Thread.sleep(2000L);
            }
        }

        region.click(Common.CLOSE);
    }

    /**
     * 兽骨加工
     *
     * @param region
     * @throws FindFailed
     */
    private void shougujiagong(Region region) throws FindFailed {
        Match shouhuogupian = region.exists(baseDir + "shouhuogupian.png", 3);
        if (shouhuogupian != null) {
            // 进行幸运翻倍
            Match xinyunfanbei = region.exists(baseDir + "xinyunfanbei.png", 0.5);
            if (xinyunfanbei != null) {
                for (int i = 0; i < 3; i++) {
                    xinyunfanbei.click();

                    Match noxinyunfanbei = region.exists(baseDir + "noxinyunfanbei.png", 0.5);
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
                    shougujiagong(region);
                }
            }
        }
    }
}
