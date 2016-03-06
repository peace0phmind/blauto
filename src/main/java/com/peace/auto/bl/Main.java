package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
public class Main {
    static List<IDo> doList = Arrays.asList(
//            new ShouGuFang(),
//            new Task(),
//            new JiangLi(),
//            new XunBao(),
//            new ShenShouWu(),
            new Building()
//            new NongChang(),
//            new ShengYu(),
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
