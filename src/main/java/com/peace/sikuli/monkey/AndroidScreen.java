package com.peace.sikuli.monkey;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.util.TreeMap;

import com.android.chimpchat.ChimpChat;
import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.sikuli.basics.Debug;
import org.sikuli.script.IRobot;
import org.sikuli.script.IScreen;
import org.sikuli.script.Location;
import org.sikuli.script.Region;
import org.sikuli.script.ScreenImage;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyRunner;

public class AndroidScreen extends AndroidRegion implements IScreen {

    public AndroidScreen(String serialNumber) throws AWTException {
        TreeMap<String, String> options = new TreeMap<String, String>();
        options.put("backend", "adb");
//        MonkeyRunner
        MonkeyDevice device = MonkeyRunner.waitForConnection(new PyObject[]{new PyFloat(15), new PyString(serialNumber)}, null);

        try { // waitForConnection() never returns null, even the connection cannot be created.
            String model = device.getProperty(new PyObject[]{new PyString("build.model")}, null);
            Debug.action("Successfully connect to a device. MODEL: " + model);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to connect to a device (within timeout).", e);
        }
        _robot = new AndroidRobot(device);

        // Region's default constructor doesn't use this screen as the default one.
        Rectangle bounds = getBounds();
//        super(bounds.x, bounds.y, bounds.width, bounds.height, 0.75, null, "");
    }

    @Override
    public ScreenImage capture() {
        return _robot.captureScreen(getBounds());
    }

    @Override
    public ScreenImage capture(int x, int y, int width, int height) {
        return _robot.captureScreen(new Rectangle(x, y, width, height));
    }

    @Override
    public ScreenImage capture(Rectangle rect) {
        return _robot.captureScreen(rect);
    }



    @Override
    public ScreenImage capture(Region reg) {
//        return _robot.captureScreen(reg.getROI());
        return null;
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
