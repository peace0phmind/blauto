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
//            new ShouGuFang(), // ok
//            new Task(), // ok
//            new JiangLi(), // ok
//            new XunBao(), // ok
//            new ShenShouWu(),
//            new Building(), // ok
//            new NongChang(),
//            new ShengYu(), // ok
//
//            new LianMeng(),
//            new HaoYou(),
//
//            new JingJiChang(),
//            new ShengLingQuan(),
    );

    public static void main(String[] args) {
        Region region = new Region(0, 45, 800, 480);
//        region.highlight(10);

        try {
            Match zhu = region.exists(Common.BASE_DIR + "zhu.png", 3);
            if (zhu == null) {
                return;
            }
            zhu.doubleClick();

            for (IDo iDo : doList) {
                iDo.Do(region);

                Thread.sleep(1000L);
            }
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

}
