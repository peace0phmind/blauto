package com.peace.auto.bl;

import com.peace.sikuli.monkey.AndroidScreen;
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

            if (times > 1) {
                IDo.setTodayFirstFinished();
            }
        } catch (FindFailed findFailed) {
            region.saveScreenCapture(".", "error");
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

    public static void main(String[] args) throws FindFailed {
        AndroidScreen region = new AndroidScreen();

        Do(region, tasks, 7);
//        Do(region, Arrays.asList(new ShouGuFang()), 1);

        region.close();
    }
}
