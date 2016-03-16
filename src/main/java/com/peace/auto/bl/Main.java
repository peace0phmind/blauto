package com.peace.auto.bl;

import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
public class Main {
    static List<IDo> tasks = Arrays.asList(
            new ShenShouWu(),

            new ChuZheng(),
            new LianBingChang(),
            new ShiLianDong(),

            new ShouGuFang(),
            new XunBao(),
            new Building(),
            new NongChang(),
            new ShengYu(),
            new TianSheng(),
            new LieChang(),
            new ShiChang(),

            new LianMeng(),
            new YingHun(),
            new HaoYou(),

            new JingJiChang(),
            new ShengLingQuan(),

            new Task(),
            new JiangLi()
    );

    static public void Do(Region region, List<IDo> dos, int times) {
        try {
            for (int i = 0; i < times; i++) {
                // 点击收起对话框
                Match duihua = region.exists(Common.BASE_DIR + "guanbiduihua.png");
                if (duihua != null && duihua.getScore() > 0.95) {
                    duihua.click();
                    Thread.sleep(1000L);
                }

                for (IDo iDo : dos) {
                    if (iDo.Done(region)) {
                        Thread.sleep(3000L);
                    }
                }

                if (times > 1) {
                    new DengLu().Done(region);
                }
            }

            IDo.setTodayFirstFinished();
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

    public static void main(String[] args) {
//        main_desktop(args);
        main_android(args);
//        main_test(args);
    }

    public static void main_android(String[] args) {
        AndroidScreen region = new AndroidScreen();

//        Do(region, tasks, 1);
        Do(region, Arrays.asList(new JingJiChang()), 1);

        region.close();
    }

    public static void main_desktop(String[] args) {
        Region region = Screen.create(0, 46, 800, 480);

        // 点击云,进入genymotion
        Match yun = region.exists(Common.BASE_DIR + "yun.png", 3);
        if (yun == null && yun.getScore() > 0.95) {
            return;
        }
        yun.doubleClick();

        Do(region, tasks, 6);
//        Test(region, new ShiChang());
    }

    public static void main_test(String[] args) {
        log.info("begin init");
        AndroidScreen region = new AndroidScreen();

//        region.saveScreenCapture("/Users/mind/peace/blauto", "ttt");

        Match bl = region.exists(Common.BASE_DIR + "bl.png");
        log.info("bl: {}", bl);
        if (bl != null) {
            bl.click();
        }

        Match close = region.exists(Common.CLOSE, 20);
        log.info("close: {}", close);
        region.saveScreenCapture("/Users/mind/peace/blauto", "ttt");
        close.saveScreenCapture("/Users/mind/peace/blauto", "ttt");
        if (close != null) {
            close.click();
        }

        region.close();
        log.info("end");
    }
}
