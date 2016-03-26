package com.peace.auto.bl;

import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

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

            new RenWu(),
            new JiangLi(),

            // shenghuo
            new ShengHuo()
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

        // qidong login
//        new DengLu().QiDong(region);

        // xiaohao renwu
//        new DengLu().Done(region, status);

//        Do(region, tasks, 5);

        // 切换账号 到peace, 如果peace在最下面
//        new DengLu().similar(0.5f).Done(region, status);
//        Do(region, tasks, 1, false, 3);

        // peace jingjichang
//        Do(region, Arrays.asList(new JingJiChang()), 15, false, 10 * 60);

//        new ShengHuo().Done(region, status);

//        new DuoBao().xunbao(region);
//        Do(region, Arrays.asList(new DuoBao()), 6);

        region.close();
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

            Region nameRegion = region.newRegion(new Rectangle(246, 94, 54, 22));
            String name = nameRegion.text();

            region.click(Common.CLOSE);
            Thread.sleep(1000L);

            String user = name.substring(name.length() - 1);
            log.info("Recognize name: {}, User is : {}", name, user);
            status.changeUser(user);
        }
    }
}
