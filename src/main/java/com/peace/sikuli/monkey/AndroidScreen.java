package com.peace.sikuli.monkey;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import org.sikuli.basics.Debug;
import org.sikuli.script.*;

import java.awt.*;
import java.util.TreeMap;

public class AndroidScreen extends AndroidRegion implements IScreen {

    private static final TreeMap<String, String> options = new TreeMap<String, String>() {{
        put("backend", "adb");
    }};

    private static ChimpChat chimpChat = null;

    public AndroidScreen(String deviceId) {
        if (chimpChat == null) {
            chimpChat = ChimpChat.getInstance(options);
        }

        IChimpDevice iChimpDevice = chimpChat.waitForConnection(10000L, deviceId);

        String model = iChimpDevice.getProperty("build.model");
        Debug.action("Successfully connect to a device. MODEL: " + model);

        _robot = new AndroidRobot(iChimpDevice);

        initScreen();
        super.initScreen(this);
    }

    public void close() {
        if (chimpChat != null) {
            chimpChat.shutdown();
            chimpChat = null;
        }
    }

    private void initScreen() {
        Rectangle bounds = getBounds();
        x = (int) bounds.getX();
        y = (int) bounds.getY();
        w = (int) bounds.getWidth();
        h = (int) bounds.getHeight();
    }


    @Override
    public ScreenImage capture() {
        initScreen();
        return _robot.captureScreen(getBounds());
    }

    @Override
    public ScreenImage capture(int x, int y, int width, int height) {
        initScreen();
        return _robot.captureScreen(new Rectangle(x, y, width, height));
    }

    @Override
    public ScreenImage capture(Rectangle rect) {
        initScreen();
        return _robot.captureScreen(rect);
    }


    @Override
    public ScreenImage capture(Region reg) {
        Rectangle rectangle;
        if (reg instanceof AndroidScreen) {
            rectangle = getBounds();
        } else {
            rectangle = reg.getRect();
        }
        initScreen();
        return _robot.captureScreen(rectangle);
    }

    @Override
    public Rectangle getBounds() {
        return _robot.getBounds();
    }

    @Override
    public IRobot getRobot() {
        return _robot;
    }

//    public <PSRML> int type(String text) throws FindFailed {
//        return _robot.type(text);
//    }

    @Override
    public Region newRegion(Rectangle rect) {
        return super.newRegion(rect);
    }

    @Override
    public void showTarget(Location loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public ScreenImage getLastScreenImageFromScreen() {
        return null;
    }

    @Override
    public ScreenImage userCapture(String string) {
        return null;
    }

    @Override
    public int getIdFromPoint(int srcx, int srcy) {
        return 0;
    }

}
