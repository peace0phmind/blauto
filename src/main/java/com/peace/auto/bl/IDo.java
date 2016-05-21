package com.peace.auto.bl;

import com.peace.sikuli.monkey.AndroidScreen;
import org.sikuli.natives.OCR;
import org.sikuli.script.*;

import java.awt.*;

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

    default String getWord(Region region) {
        return getWord(region, "0123456789/abcdefghijklmnopqrstuvwxyz.-");
    }

    default int getNumber(Region region) {
        return Integer.parseInt(getWord(region, "0123456789").replaceAll(" ", ""));
    }

    default String getTime(Region region) {
        return getWord(region, "0123456789:");
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
}
