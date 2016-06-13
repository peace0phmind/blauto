package com.peace.sikuli.monkey;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.IRobot;
import org.sikuli.script.IScreen;
import org.sikuli.script.Location;
import org.sikuli.script.ScreenImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class AndroidRobot implements IRobot {

    private IChimpDevice _device;
    private Rectangle _bounds; // cache
    private Rectangle _landscapeBounds; // cache
    private String _model; // cache
    private BufferedImage screen;

    public AndroidRobot(IChimpDevice dev) {
        _device = dev;
    }

    private int errorCount = 0;

    private static void sleep(float seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ScreenImage captureScreen(Rectangle rect) {
//        log.info("Capture screen: {}", rect);
        try {
            byte[] bytes = _device.takeSnapshot().convertToBytes("png");
            screen = ImageIO.read(new ByteArrayInputStream(bytes));

            if (isLandscape()) {
                BufferedImage rotate = new BufferedImage(screen.getHeight(), screen.getWidth(), screen.getType());
                Graphics2D graphics = rotate.createGraphics();

                AffineTransform affineTransform = new AffineTransform();
                affineTransform.translate((rotate.getWidth() - screen.getWidth()) / 2, (rotate.getHeight() - screen.getHeight()) / 2);
                affineTransform.rotate(Math.toRadians(-90), screen.getWidth() / 2, screen.getHeight() / 2);
                graphics.setTransform(affineTransform);
                graphics.drawImage(screen, 0, 0, null);
                graphics.dispose();

                screen = rotate;
            }

            BufferedImage part = screen.getSubimage(rect.x, rect.y, rect.width, rect.height);
            return new ScreenImage(rect, part);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Rectangle getBounds() {
        if (_bounds == null || _landscapeBounds == null) {
            int width = Integer.parseInt(_device.getProperty("display.width"));
            int height = Integer.parseInt(_device.getProperty("display.height"));

            int realWidth = Math.min(width, height);
            int realHeight = Math.max(width, height);

            _bounds = new Rectangle(0, 0, realWidth, realHeight);
            _landscapeBounds = new Rectangle(0, 0, realHeight, realWidth);
        }

        return isLandscape() ? _landscapeBounds : _bounds;
    }

    public boolean isLandscape() {
        String orientation = _device.shell("dumpsys input");
        if (orientation == null || orientation.trim().length() == 0) {
            log.info("Run command: dumpsys input error.");
            if (errorCount++ > 10) {
                System.exit(-1);
            } else {
                throw new RuntimeException("Run command: dumpsys input error.");
            }
        }
        String[] split = orientation.split("\r\n");
        Optional<String> stringOptional = Arrays.asList(split).stream().filter(x -> x.contains("SurfaceOrientation:")).findFirst();
        if (stringOptional.isPresent()) {
            String s = stringOptional.get().trim().replaceFirst("SurfaceOrientation:", "").trim();
            if ("1".equals(s)) {
                return true;
            }
        }

        return false;
    }

    public String getModel() {
        if (_model != null) return _model;

        _model = _device.getProperty("build.model");
        return _model;
    }


    public int type(String text) {
        _device.type(text);
        return 1;
    }

    public void touch(int x, int y) {
        _device.touch(x, y, TouchPressType.DOWN_AND_UP);
    }

    public void longPress(int x, int y) {
        _device.drag(x, y, x, y, 2, 2);
    }

    public void pressHome() {
        pressHome(0);
    }

    public void pressHome(float durationSec) {
        press("KEYCODE_HOME", durationSec);
    }

    public void pressMenu() {
        press("KEYCODE_MENU", 0);
    }

    public void pressBack() {
        press("KEYCODE_BACK", 0);
    }

    public void pressSearch() {
        press("KEYCODE_SEARCH", 0);
    }

    public void pressEnter() {
        press("KEYCODE_ENTER", 0);
    }

    public void pressBackspace() {
        press("KEYCODE_DEL", 0);
    }

    public void pressDpadUp() {
        press("KEYCODE_DPAD_UP", 0);
    }

    public void pressDpadDown() {
        press("KEYCODE_DPAD_DOWN", 0);
    }

    public void pressDpadLeft() {
        press("KEYCODE_DPAD_LEFT", 0);
    }

    public void pressDpadRight() {
        press("KEYCODE_DPAD_RIGHT", 0);
    }

    public void pressDpadCenter() {
        pressDpadCenter(0);
    }

    public void pressDpadCenter(float durationSec) {
        press("KEYCODE_DPAD_CENTER", durationSec);
    }

    private void press(String keycodeName, float durationSec) {
        sleep(1);
        if (durationSec == 0) {
            _device.press(keycodeName, TouchPressType.DOWN_AND_UP);
        } else {
            _device.press(keycodeName, TouchPressType.DOWN);
            sleep(durationSec);
            _device.press(keycodeName, TouchPressType.UP);
        }
    }

    @Override
    public void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mouseMove(int x, int y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mouseDown(int buttons) {
    }

    @Override
    public int mouseUp(int buttons) {
        return 0;
    }

    @Override
    public void clickStarts() {
    }

    @Override
    public void clickEnds() {
    }

    @Override
    public void mouseWheel(int wheelAmt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void keyDown(String keys) {

    }

    @Override
    public void keyUp(String keys) {

    }

    @Override
    public void keyDown(int code) {

    }

    @Override
    public void keyUp(int code) {

    }

    @Override
    public void keyUp() {

    }

    @Override
    public void pressModifiers(int modifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void releaseModifiers(int modifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAutoDelay(int ms) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Color getColorAt(int x, int y) {
        if (screen == null) {
            captureScreen(getBounds());
        }
        int clr = screen.getRGB(x, y);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;
        return new Color(red, green, blue);
    }

    @Override
    public void cleanup() {
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public IScreen getScreen() {
        return null;
    }

    @Override
    public void smoothMove(Location dest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void smoothMove(Location src, Location dest, long ms) {
        double distance = Math.sqrt(Math.pow(src.getX() - dest.getX(), 2) + Math.pow(src.getY() - dest.getY(), 2));
        log.debug("Drag from {} to {}: distance {}", src, dest, distance);
        _device.drag(src.getX(), src.getY(), dest.getX(), dest.getY(), (int) distance, ms);
    }

    @Override
    public void typeChar(char character, KeyMode mode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void typeKey(int key) {

    }

    @Override
    public void typeStarts() {

    }

    @Override
    public void typeEnds() {

    }

    @Override
    public void waitForIdle() {
        throw new UnsupportedOperationException();
    }

}
