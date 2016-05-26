package com.peace.auto.bl;

import com.peace.auto.bl.job.DuoBaoModeJob;
import com.peace.auto.bl.job.OrderModeJob;
import com.peace.auto.bl.job.XunBaoModeJob;
import com.peace.auto.bl.task.DengLu;
import com.peace.auto.bl.task.HaiDiShiJie;
import com.peace.auto.bl.task.TianSheng;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.peace.auto.bl.common.CommonUtils.*;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
@DisallowConcurrentExecution
public class Main {

    public static void main(String[] args) throws FindFailed, InterruptedException, IOException, SchedulerException {
        Settings.OcrTextRead = true;

        status.getNextUserTask();

        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        OrderModeJob.init(defaultScheduler);
        XunBaoModeJob.init(defaultScheduler);
        DuoBaoModeJob.init(defaultScheduler);
        defaultScheduler.start();

//        autoTestMode();
//        testMode();
    }

    private static void autoTestMode() throws IOException, InterruptedException, FindFailed {
//        AndroidScreen region = startDevice(DEVICE_1);
        AndroidScreen region = getRegion(DEVICE_1);
//        DENG_LU.QiDong(region, status, "peace");

        new HaiDiShiJie().Done(region, status);

        region.close();
//        stopDevice(DEVICE_1);
    }

    private static void testMode() throws InterruptedException, FindFailed, IOException {
//        AndroidScreen region = new AndroidScreen("192.168.60.101:5555");
//        AndroidScreen region = startDevice(DEVICE_1);
        AndroidScreen region = getRegion(DEVICE_1);

        new TianSheng().Done(region, status);

        region.close();
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
