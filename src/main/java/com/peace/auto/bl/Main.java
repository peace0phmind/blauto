package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

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

            new LianMeng(),
            new HaoYou(),

            new JingJiChang(),
            new ShengLingQuan(),

            new Task(),
            new JiangLi()
    );

    public static void main(String[] args) {
        Region region = new Region(0, 45, 800, 480);
//        region.highlight(10);

        try {
            Match yun = region.exists(Common.BASE_DIR + "yun.png", 3);
            if (yun == null) {
                return;
            }
            yun.doubleClick();

            Match duihua = region.exists(Common.BASE_DIR + "guanbiduihua.png", 3);
            if (duihua != null && duihua.getScore() > 0.95) {
                duihua.click();
                Thread.sleep(1000L);
            }

            for (IDo iDo : doList) {
                iDo.Do(region);

                Thread.sleep(3000L);
            }
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

}
