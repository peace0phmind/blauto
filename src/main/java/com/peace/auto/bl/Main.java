package com.peace.auto.bl;

import org.sikuli.script.Region;

/**
 * Created by mind on 3/2/16.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Region region = new Region(0, 45, 800, 480);
//        region.highlight(10);

//        ShouGuFang.Do(region);
//
//        Task.Do(region);
//        JiangLi.Do(region);
//        XunBao.Do(region);
//        ShenShouWu.Do(region);
        Building.Do(region);
//        NongChang.Do(region);
//        ShengYu.Do(region);
//
//        LianMeng.Do(region);
//        HaoYou.Do(region);
//
//        JingJiChang.Do(region);
//        ShengLingQuan.Do(region);
    }

}
