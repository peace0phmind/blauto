package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;
import org.sikuli.script.Region;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Created by mind on 3/6/16.
 */
public interface IDo {
    Properties properties = new Properties();
    String FINISHED_PROP = "finished.prop";

    static void setTodayFirstFinished() {
        properties.setProperty(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), Boolean.TRUE.toString());

        try {
            properties.store(new FileWriter(FINISHED_PROP), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean Done(Region region) throws FindFailed, InterruptedException;

    default boolean isButtonEnable(Region region) {
        return isButtonEnable(region, 0, 0);
    }

    default boolean isButtonEnable(Region region, int xOffset, int yOffset) {
        Color pixelColor = getPixelColor(region, xOffset, yOffset);

        if (pixelColor.getRed() < 160) {
            return false;
        } else {
            return true;
        }
    }

    default Color getPixelColor(Region region, int xOffset, int yOffset) {
        Location cp = region.getTopLeft().offset(xOffset, yOffset);
        return region.getScreen().getRobot().getColorAt(cp.getX(), cp.getY());
    }

    default boolean isTodayFirstFinished() {
        if (properties.isEmpty()) {
            try {
                properties.load(new FileReader(FINISHED_PROP));
            } catch (IOException e) {
                if (e instanceof FileNotFoundException) {
                    try (FileWriter fileWriter = new FileWriter(FINISHED_PROP)) {
                        fileWriter.flush();
                        properties.load(new FileReader(FINISHED_PROP));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
            System.out.println("load property");

            if (properties.isEmpty()) {
                properties.setProperty(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), Boolean.FALSE.toString());
            }
        }

        return Boolean.valueOf((String) properties.getOrDefault(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), Boolean.FALSE.toString()));
    }
}
