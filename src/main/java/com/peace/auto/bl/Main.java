package com.peace.auto.bl;

import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

            new HaiDiShiJie(),

            new ShengHuo(),
            new QunYingHui(),
            new ShenQi()
            // duobao
    );

    static Status status = new Status();
    private static DengLu DENG_LU = new DengLu();

    public static void main(String[] args) throws FindFailed, InterruptedException, IOException {
        String device1 = "3e08a7ca-d763-44e3-88a8-ce4c1831a1f9";

        Runtime rt = Runtime.getRuntime();
        rt.exec("/Users/mind/Applications/Genymotion.app/Contents/MacOS/player.app/Contents/MacOS/player -x --vm-name 3e08a7ca-d763-44e3-88a8-ce4c1831a1f9 --no-popup");
        Thread.sleep(3 * 1000L);
        // 启动
        Process exec = rt.exec("/Users/mind/Applications/Genymotion.app/Contents/MacOS/player.app/Contents/MacOS/player --vm-name 3e08a7ca-d763-44e3-88a8-ce4c1831a1f9 --no-popup");
        Thread.sleep(30 * 1000L);
        log.info("start ok");

        autoMode();

        // 退出player
        rt.exec("/Users/mind/Applications/Genymotion.app/Contents/MacOS/player.app/Contents/MacOS/player -x --vm-name 3e08a7ca-d763-44e3-88a8-ce4c1831a1f9 --no-popup");
    }

    private static void autoMode() throws FindFailed, InterruptedException {
        Settings.OcrTextRead = true;
        AndroidScreen region = new AndroidScreen("192.168.60.101:5555");
        DENG_LU.QiDong(region, status);

//        while (true) {
        for (int i = 0; i < 1; i++) {
            try {
                // 点击收起对话框
                Match duihua = region.exists(Common.BASE_DIR + "guanbiduihua.png");
                if (duihua != null && duihua.getScore() > 0.95) {
                    duihua.click();
                    Thread.sleep(1000L);
                }

                for (IDo iDo : tasks) {
                    if (iDo.Done(region, status)) {
                        Thread.sleep(3 * 1000L);
                    }
                }

                DENG_LU.Done(region, status);
            } catch (FindFailed findFailed) {
                region.saveScreenCapture(".", "error");
                log.error("{}", findFailed);
                break;
            } catch (InterruptedException e) {
                log.error("{}", e);
                break;
            }
        }

        region.close();
    }

    private static void lingQuXunBao() {
        String device1 = "3e08a7ca-d763-44e3-88a8-ce4c1831a1f9";
        String device2 = "efc444e7-aeb9-4ce4-8993-9e777ed033d9";

        String device1ip = "192.168.60.101:5555";
        String device2ip = "192.168.60.102:5555";



    }

    private static BufferedImage getBlackWhiteImage(BufferedImage original) {
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        int red;
        int newPixel;
        int threshold = 170;

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();

                int alpha = new Color(original.getRGB(i, j)).getAlpha();

                if (red > threshold) {
                    newPixel = 0;
                } else {
                    newPixel = 255;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);

            }
        }

        return binarized;
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }
}
