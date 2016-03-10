package com.peace.auto.bl;

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
    static List<IDo> doList = Arrays.asList(
            new ShouGuFang(),
            new XunBao(),
            new ShenShouWu(),
            new Building(),
            new NongChang(),
            new ShengYu(),
            new TianSheng(),
            new LieChang(),
            new LianBingChang(),
            new ShiChang(),

            new LianMeng(),
            new YingHun(),
            new HaoYou(),

            new JingJiChang(),
            new ShengLingQuan(),

            new Task(),
            new JiangLi()
    );

    static public void Do(Region region, int times) {
        try {
            for (int i = 0; i < times; i++) {
                // 点击云,进入genymotion
                Match yun = region.exists(Common.BASE_DIR + "yun.png", 3);
                if (yun == null && yun.getScore() > 0.95) {
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

                new DengLu().Do(region);
            }
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

    static public void Test(Region region, IDo ido) {
        try {
            // 点击云,进入genymotion
            Match yun = region.exists(Common.BASE_DIR + "yun.png", 3);
            if (yun == null && yun.getScore() > 0.95) {
                return;
            }
            yun.doubleClick();

            ido.Do(region);

        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

    public static void main(String[] args) {
        Region region = Screen.create(0, 45, 800, 480);

        Do(region, 6);

//        Test(region, new LianBingChang());
    }

}
