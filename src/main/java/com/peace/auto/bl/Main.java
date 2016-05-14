package com.peace.auto.bl;

import com.peace.sikuli.monkey.AndroidScreen;
import com.sun.org.apache.xpath.internal.operations.And;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.util.StringUtils;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
public class Main {
    static List<IDo> tasks = Arrays.asList(
            new ShenShouWu(),

            new LianBingChang(),
            new ShiLianDong(),
            new ChuZheng(),

            new LieChang(),

            new ShouGuFang(),
            new XunBao(),
            new Building(),
            new NongChang(),
            new ShengYu(),
            new TianSheng(),
            new ShiChang(),

            new LianMeng(),
            new YingHun(),
            new HaoYou(),

            new JingJiChang(),
            new ShengLingQuan(),

            new RenWu(),
            new JiangLi(),

            // shenghuo
            new ShengHuo(),
            new QunYingHui(),
            new ShenQi()
            // duobao
    );

    static Status status = new Status();

    static public void Do(AndroidScreen region, List<IDo> dos, int times) {
        Do(region, dos, times, true, 3);
    }

    static public void Do(AndroidScreen region, List<IDo> dos, int times, boolean reboot, int waitSeconds) {
        try {
            for (int i = 0; i < times; i++) {
                setUser(region);

                // 点击收起对话框
                Match duihua = region.exists(Common.BASE_DIR + "guanbiduihua.png");
                if (duihua != null && duihua.getScore() > 0.95) {
                    duihua.click();
                    Thread.sleep(1000L);
                }

                for (IDo iDo : dos) {
                    if (iDo.Done(region, status)) {
                        Thread.sleep(waitSeconds * 1000L);
                    }
                }

                if (reboot) {
                    if (!new DengLu().Done(region, status)) {
                        return;
                    }
                }
            }
        } catch (FindFailed findFailed) {
            region.saveScreenCapture(".", "error");
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

    public static void main(String[] args) throws FindFailed, InterruptedException {
        Settings.OcrTextRead = true;
        AndroidScreen region = new AndroidScreen();

//        region.saveScreenCapture(".", "info");

//        Region region1 = region.newRegion(new Rectangle(125, 380, 18, 18));
//        region1.saveScreenCapture(".", "r");
//        log.info("{}", region1.text());

        dayMode(region);
//        nightMode(region);

        region.close();
    }

    private static void dayMode(AndroidScreen region) throws FindFailed, InterruptedException {
        new DengLu().QiDong(region, status);
//        new DengLu().QiDong(region, status, "peace");
//        new DengLu().QiDong(region, status, "peace0ph002");
//        new DengLu().Done(region, status, "peace");
//        new DengLu().Done(region, status, "peace0ph001");

//        status.setWantUser("peace");
//        Do(region, tasks, 18);

        // xiaohao renwu
//        new DengLu().Done(region, status);
//        region.click(Common.CLOSE);
//        region.click(Common.QUE_DING);

        // 切换账号 到peace, 如果peace在最下面
//        Do(region, tasks, 1, false, 3);
//        Do(region, Arrays.asList(new JingJiChang()), 16, false, 10 * 60);
//        new DuoBao().xunbao(region);
//        Do(region, Arrays.asList(new JingJiChang()), 12, false, 10 * 60);
//        Do(region, Arrays.asList(new LieChang()), 1, false, 1);
//        Do(region, Arrays.asList(new LianMeng()), 1, false, 1);

        // peace jingjichang
        Do(region, tasks, 56);
//        Do(region, Arrays.asList(new DuoBao()), 14);

//        Thread.sleep(60 * 60 * 1000);
//
//        Do(region, tasks, 6);
    }

    private static void nightMode(AndroidScreen region) throws FindFailed, InterruptedException {
//        Thread.sleep(30 * 60 * 1000);

//        new DengLu().QiDong(region, "peace0ph001");
        Do(region, tasks, 12);

        for (int i = 0; i < 3; i++) {
            // 切换账号 到peace, 如果peace在最下面
            new DengLu().Done(region, status, "peace");
            Do(region, Arrays.asList(new JingJiChang()), 6, false, 10 * 60);
            Do(region, tasks, 7);
        }
    }

    private static void setUser(AndroidScreen region) throws FindFailed, InterruptedException {
        Match touxiang = region.exists(Common.BASE_DIR + "touxiang.png");
        if (touxiang != null) {
            touxiang.click();

            Match qiuzhangxinxi = region.exists(Common.BASE_DIR + "qiuzhangxinxi.png");
            if (qiuzhangxinxi == null) {
                touxiang.click();
                qiuzhangxinxi = region.exists(Common.BASE_DIR + "qiuzhangxinxi.png");
            }

            if (qiuzhangxinxi == null) {
                return;
            }

            ScreenImage simg = region.getScreen().capture(new Rectangle(134, 382, 8, 14));
            TextRecognizer tr = TextRecognizer.getInstance();
            String word = tr.recognizeWord(simg);

            region.click(Common.CLOSE);
            Thread.sleep(1000L);

            if (!status.changeUser(word)) {
                new DengLu().Done(region, status, status.getWantUser());
                setUser(region);
            } else {
                log.info("current user: {}, want user: {}, word: {}", status.getCurrentUser(), status.getWantUser(), word);
            }
        }
    }
}
