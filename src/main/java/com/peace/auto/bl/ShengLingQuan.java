package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/5/16.
 */
@Slf4j
public class ShengLingQuan {

    static public void Do(Region region) {
        String baseDir = Common.BASE_DIR + "shenglingquan/";

        try {
            region.click(Common.RI_CHANG);

            Match shenglingquan = region.exists(baseDir + "shenglingquan.png", 30);
            if (shenglingquan != null) {
                shenglingquan.click();

                // 高级修炼
                Match xiulian = region.exists(baseDir + "xiulian.png", 3);
                if (xiulian != null) {
                    xiulian.click();

                    Match tingzhixiulian = region.exists(baseDir + "tingzhixiulian.png", 3);
                    if (tingzhixiulian == null) {
                        region.click(baseDir + "gaojixiulian.png");
                        region.click(baseDir + "kaishixiulian.png");

                        Match goumai = region.exists(baseDir + "goumai.png", 3);
                        if (goumai != null) {
                            region.click(Common.QUE_DING);
                        }
                    }
                }

                // 神灵泉
//                region.click(baseDir + "shenglingquan1.png");
//
//                for (int i = 0; i < 5; i++) {
//                    Match shengpin = region.exists(baseDir + "shengpin", 3);
//                    if (shengpin == null) {
//                        break;
//                    } else {
//                        region.click(baseDir + "putongxiulian.png");
//                        Thread.sleep(500L);
//                    }
//                }
            }

            region.click(Common.CLOSE);
            Thread.sleep(500L);

            region.click(Common.CLOSE);
            Thread.sleep(500L);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
