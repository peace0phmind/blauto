package com.peace.auto.bl;

import com.peace.sikuli.monkey.AndroidScreen;
import com.sun.tools.hat.internal.parser.ReadBuffer;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
public class Main {
    private static final String PLAY_PATH = "/Users/mind/Applications/Genymotion.app/Contents/MacOS/player.app/Contents/MacOS/player";
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
    static String device1 = "3e08a7ca-d763-44e3-88a8-ce4c1831a1f9";
    static String device2 = "efc444e7-aeb9-4ce4-8993-9e777ed033d9";
    static String device3 = "e10a2c0d-b1cd-40b2-be65-5b714fa9fea1";

    private static DengLu DENG_LU = new DengLu();

    public static void main(String[] args) throws FindFailed, InterruptedException, IOException {
        autoMode();
//        testMode();
    }

    private static void testMode() throws InterruptedException, FindFailed {
        Settings.OcrTextRead = true;
        AndroidScreen region = new AndroidScreen("192.168.60.101:5555");

        new ShenQi().Done(region, status);

        region.close();
    }

    private static AndroidScreen startDevice(String deviceId) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();

        rt.exec(String.format("%s -x --vm-name %s --no-popup", PLAY_PATH, deviceId));
        Thread.sleep(10 * 1000L);
        rt.exec(String.format("%s --vm-name %s --no-popup", PLAY_PATH, deviceId));

        Thread.sleep(30 * 1000L);

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", deviceId));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();
        ip = ip.replaceAll("Value: ", "") + ":5555";

        log.info("ip: {}", ip);

        return new AndroidScreen(ip.trim());
    }

    private static void stopDevice(String deviceId) throws IOException {
        Runtime rt = Runtime.getRuntime();

        rt.exec(String.format("%s -x --vm-name %s --no-popup", PLAY_PATH, deviceId));
    }


    private static void autoMode() throws FindFailed, InterruptedException, IOException {
        Settings.OcrTextRead = true;
        AndroidScreen region = startDevice(device1);
        DENG_LU.QiDong(region, status);

        while (true) {
//        for (int i = 0; i < 7; i++) {
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
        stopDevice(device1);
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
