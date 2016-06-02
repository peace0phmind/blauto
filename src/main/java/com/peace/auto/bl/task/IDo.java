package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.sikuli.monkey.AndroidScreen;
import org.sikuli.natives.OCR;
import org.sikuli.script.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * Created by mind on 3/6/16.
 */
public interface IDo {

    boolean Done(Region region, Status status) throws FindFailed, InterruptedException;

    boolean CanDo(Status status, String userName);

    default boolean isButtonEnable(Region region) {
        return isButtonEnable(region, 0, 0);
    }

    default boolean isButtonEnable(Region region, int xOffset, int yOffset) {
        Color pixelColor = getPixelColor(region, xOffset, yOffset);

        if (pixelColor.getRed() < 180) {
            return false;
        } else {
            return true;
        }
    }

    default Color getPixelColor(Region region, int xOffset, int yOffset) {
        Location cp = region.getTopLeft().offset(xOffset, yOffset);
        return region.getScreen().getRobot().getColorAt(cp.getX(), cp.getY());
    }

    default String getWord(Region region, String range) {
        if (range != null && !"".equals(range.trim())) {
            OCR.setParameter("tessedit_char_whitelist", range);
        }
        ScreenImage simg = region.getScreen().capture(region.getRect());
        TextRecognizer tr = TextRecognizer.getInstance();
        return tr.recognizeWord(simg);
    }

    default void clickInside(Region region, Pattern pattern) throws FindFailed {
        Iterator<Match> all = region.findAll(pattern);
        Lists.newArrayList(all).stream().sorted((a, b) -> a.x - b.x).findFirst().get().click();

    }

    default String getWord(Region region) {
        return getWord(region, "0123456789/abcdefghijklmnopqrstuvwxyz.-");
    }

    default int getNumber(Region region) {
        return Integer.parseInt(getWord(region, "0123456789").replaceAll(" ", ""));
    }

    /**
     * @param region
     * @param threshold
     *   building : threshold = 170
     * @return
     */
    default String getTime(Region region, int threshold) {
        OCR.setParameter("tessedit_char_whitelist", "0123456789:");

        ScreenImage simg = region.getScreen().capture(region.getRect());
        TextRecognizer tr = TextRecognizer.getInstance();
        BufferedImage blackWhiteImage = getBlackWhiteImage(simg.getImage(), threshold);
        try {
            ImageIO.write(blackWhiteImage, "png", new File(String.format("/Users/mind/peace/blauto/%d.png", LocalDateTime.now().getNano())));
        } catch (IOException e) {
        }
        return tr.recognizeWord(blackWhiteImage);
    }

    default Region newRegion(Region region, Rectangle rectangle) {
        if (region instanceof AndroidScreen) {
            return ((AndroidScreen) region).newRegion(rectangle);
        }

        return Region.create(rectangle);
    }

    default void move(Region region, Location dest, long ms) {
        IRobot robot = region.getScreen().getRobot();
        robot.smoothMove(region.getCenter(), dest, ms);
    }

    default BufferedImage getBlackWhiteImage(BufferedImage original, int threshold) {
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        int red;
        int newPixel;
//     building   int threshold = 170;
//     shenqi   int threshold = 30;

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

    default int colorToRGB(int alpha, int red, int green, int blue) {
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
