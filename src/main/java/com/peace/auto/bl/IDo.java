package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;
import org.sikuli.script.Region;

import java.awt.*;

/**
 * Created by mind on 3/6/16.
 */
public interface IDo {
    void Do(Region region) throws FindFailed, InterruptedException;

    default boolean isButtonEnable(Region region) {
        return isButtonEnable(region, 0, 0);
    }

    default boolean isButtonEnable(Region region, int xOffset, int yOffset) {
        try {
            Location cp = region.getTopLeft().offset(xOffset, yOffset);
            Robot robot = new Robot();
            Color pixelColor = robot.getPixelColor(cp.x, cp.y);

            if (pixelColor.getRed() < 160) {
                return false;
            } else {
                return true;
            }
        } catch (AWTException e) {
            return false;
        }
    }
}
