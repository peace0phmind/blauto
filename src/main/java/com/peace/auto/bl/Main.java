package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
public class Main {
    static List<IDo> doList1 = Arrays.asList(
            new ShouGuFang(),
            new XunBao(),
            new ShenShouWu(),
            new Building(),
            new NongChang(),
            new ShengYu(),
            new TianSheng(),
            new LieChang(),

            new LianMeng(),
            new YingHun(),
            new HaoYou(),

            new JingJiChang(),
            new ShengLingQuan(),

            new Task(),
            new JiangLi()
    );

    static List<IDo> doList = Arrays.asList(
            new Building(),
            new NongChang(),
            new ShengYu(),
            new TianSheng(),

            new YingHun(),

            new JingJiChang(),
            new ShengLingQuan(),

            new Task(),
            new JiangLi()
    );

    static List<IDo> doList2 = Arrays.asList(
            new LieChang()
    );

    public static void main(String[] args) {
        Region region = Screen.create(0, 45, 800, 480);
//        region.setWaitScanRate(2);

        try {
            log.info("Begin: {}", LocalDateTime.now());
//            log.info("{}, {}", region, region.getWaitScanRate());
//            region.saveScreenCapture("/Users/mind/Pictures/test", "aaa");

            // 点击云,进入genymotion
            Match yun = region.exists(Common.BASE_DIR + "yun.png", 3);
            if (yun == null) {
                return;
            }
            yun.doubleClick();

            // 点击收起对话框
            Match duihua = region.exists(Common.BASE_DIR + "guanbiduihua.png", 3);
            if (duihua != null && duihua.getScore() > 0.95) {
                duihua.click();
                Thread.sleep(1000L);
            }

            for (IDo iDo : doList) {
                iDo.Do(region);

                Thread.sleep(3000L);
            }

            log.info("End: {}", LocalDateTime.now());
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

}
