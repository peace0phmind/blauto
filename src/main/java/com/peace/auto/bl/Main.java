package com.peace.auto.bl;

import org.sikuli.script.Screen;

/**
 * Created by mind on 3/2/16.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Screen screen = new Screen();

        ShouGuFang.Do(screen);

//        Task.Do(screen);
//        JiangLi.Do(screen);
//        XunBao.Do(screen);
//        ShenShouWu.Do(screen);
//        Building.Do(screen);
//        NongChang.Do(screen);
//        ShengYu.Do(screen);
//
//        LianMeng.Do(screen);
//        HaoYou.Do(screen);


        JingJiChang.Do(screen);
    }

}
