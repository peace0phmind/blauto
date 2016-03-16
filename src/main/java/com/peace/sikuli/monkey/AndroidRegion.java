package com.peace.sikuli.monkey;

import org.sikuli.basics.Debug;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;
import org.sikuli.script.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;

public class AndroidRegion extends Match {

    private static double AUTO_WAIT_TIMEOUT = 3;

    protected AndroidRobot _robot;

    protected AndroidRegion() {
        super(0, 0, 0, 0, -1, null, "");
        this.setAutoWaitTimeout(AUTO_WAIT_TIMEOUT);
//        _autoWaitTimeout = AUTO_WAIT_TIMEOUT;
    }

    public AndroidRegion(Match match, IScreen screen) {
        super(match.x, match.y, match.w, match.h, match.getScore(), screen, "");
        _robot = (AndroidRobot) screen.getRobot();
        this.setAutoWaitTimeout(AUTO_WAIT_TIMEOUT);
//        _autoWaitTimeout = AUTO_WAIT_TIMEOUT;
    }

    public AndroidRegion(Rectangle rect, IScreen screen) {
        super(rect.x, rect.y, rect.width, rect.height, -1, screen, "");
        _robot = (AndroidRobot) screen.getRobot();
        this.setAutoWaitTimeout(AUTO_WAIT_TIMEOUT);
//        _autoWaitTimeout = AUTO_WAIT_TIMEOUT;
    }

    public ScreenImage capture(String label) {
        return capture(label, false);
    }

    public ScreenImage capture(String label, boolean deleteOnExit) {
        ScreenImage img = _robot.captureScreen(getRect());
        try {
            File tmp = File.createTempFile("sikuli-scr-" + label + "-", ".png");
            if (deleteOnExit) tmp.deleteOnExit();
            Debug.action("[" + label + "] Region capture -> " + tmp.getPath());
            ImageIO.write(img.getImage(), "png", tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return img;
    }

    @Override
    public <PSC> Match exists(PSC target, double timeout) {
        return newMatch(super.exists(getAlternativePS(target), timeout));
    }

//    public <PSRML> int tap(PSRML target) throws FindFailed {
//        Location loc = getLocationFromPSRML(target);
//        _robot.tap(loc.x, loc.y);
//        return 1;
//    }

//    public <PSRML> int longPress(PSRML target) throws FindFailed {
//        Location loc = getLocationFromPSRML(target);
//        _robot.longPress(loc.x, loc.y);
//        return 1;
//    }

    public <PSRML> int pan(PSRML arg0, PSRML arg1) throws FindFailed {
        return super.dragDrop(arg0, arg1);
    }

//    public <PSRML> int type(PSRML target, String text) throws FindFailed {
//        if (target != null) {
//            tap(target);
//            try {
//                Thread.sleep(2 * 1000);
//            } catch (InterruptedException e) {
//            }
//        }
//
//        _robot.type(text);
//        try {
//            Thread.sleep(2 * 1000);
//        } catch (InterruptedException e) {
//        }
//
//        return 1;
//    }

    @Override
    public int type(String text) {
        try {
            click();
            Thread.sleep(1000L);
            _robot.type(text);
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
        return 1;
    }

    @Override
    public <PSC> Match find(PSC target) throws FindFailed {
        return newMatch(super.find(getAlternativePS(target)));
    }

    @Override
    public <PSC> Iterator<Match> findAll(PSC target) throws FindFailed {
        Iterator<Match> all = super.findAll(getAlternativePS(target));
        ArrayList<Match> wrappers = new ArrayList<Match>();
        while (all.hasNext())
            wrappers.add(newMatch(all.next()));

        return wrappers.iterator();
    }

//    public <PSC> Iterator<Match> findAllNow(PSC target) throws FindFailed {
//        Iterator<Match> all = super.findAllNow(getAlternativePS(target));
//        ArrayList<Match> wrappers = new ArrayList<Match>();
//        while (all.hasNext())
//            wrappers.add(newMatch(all.next()));
//
//        return wrappers.iterator();
//    }


    @Override
    public int click() {
        Location target = this.getTarget();
        _robot.touch(target.getX(), target.getY());
        return 0;
    }

    @Override
    public <PFRML> int click(PFRML target) throws FindFailed {
        return click(target, 0);
    }

    @Override
    public <PFRML> int click(PFRML target, Integer modifiers) throws FindFailed {
        Location loc = getLocationFromTarget(target);
        _robot.touch(loc.getX(), loc.getY());
        return 0;
    }


    @Override
    public int doubleClick() {
        click();
        return click();
    }

    public <PFRML> int doubleClick(PFRML target) throws FindFailed {
        return doubleClick(target, 0);
    }

    public <PFRML> int doubleClick(PFRML target, Integer modifiers) throws FindFailed {
        Location loc = getLocationFromTarget(target);
        _robot.touch(loc.getX(), loc.getY());
        _robot.touch(loc.getX(), loc.getY());
        return 0;
    }

    private <PSIMRL> Location getLocationFromTarget(PSIMRL target) throws FindFailed {
        if (target instanceof Pattern || target instanceof String || target instanceof Image) {
            Match m = find(target);
            if (m != null) {
                return m.getTarget();
            }
            return null;
        }
        if (target instanceof Match) {
            return ((Match) target).getTarget();
        }
        if (target instanceof Region) {
            return ((Region) target).getCenter();
        }
        if (target instanceof Location) {
            return new Location((Location) target);
        }
        return null;
    }


    @Override
    public <PSC> Match wait(PSC target, double timeout) throws FindFailed {
        return newMatch(super.wait(getAlternativePS(target), timeout));
    }

    @Override
    public <PSC> boolean waitVanish(PSC target, double timeout) {
        return super.waitVanish(getAlternativePS(target), timeout);
    }

    @Override
    public Region offset(Location loc) {
        Rectangle rect = new Rectangle(x + loc.x, y + loc.y, w, h);
        return newRegion(rect);
    }

    @Override
    public Region nearby(int range) {
        Rectangle bounds = getScreen().getBounds();
        Rectangle rect = new Rectangle(x - range, y - range, w + range * 2, h + range * 2);
        rect = rect.intersection(bounds);

        return newRegion(rect);
    }

    @Override
    public Region right(int range) {
        Rectangle bounds = getScreen().getBounds();
        Rectangle rect = new Rectangle(x + w, y, range, h);
        rect = rect.intersection(bounds);

        return newRegion(rect);
    }

    @Override
    public Region left(int range) {
        Rectangle bounds = getScreen().getBounds();
        Region r = newRegion(getRect());
        r.x = x - range < bounds.x ? bounds.x : x - range;
        r.y = y;
        r.w = x - r.x;
        r.h = h;
        return r;
    }

    @Override
    public Region above(int range) {
        Rectangle bounds = getScreen().getBounds();
        Region r = newRegion(getRect());
        r.x = x;
        r.y = y - range < bounds.y ? bounds.y : y - range;
        r.w = w;
        r.h = y - r.y;
        return r;
    }

    @Override
    public Region below(int range) {
        Rectangle bounds = getScreen().getBounds();
        Rectangle rect = new Rectangle(x, y + h, w, range);
        rect = rect.intersection(bounds);

        return newRegion(rect);
    }

    protected Region newRegion(Rectangle rect) {
        return new AndroidRegion(rect, getScreen());
    }

    protected Match newMatch(Match match) {
        if (match == null) return null;

        return new AndroidRegion(match, getScreen());
        // TODO: target offset should be kept, but Match.setTargetOffset() is only accessible in the same package.
    }

//    @Override
//    public <PSRML> Location getLocationFromPSRML(PSRML target) throws FindFailed {
//        Location loc = super.getLocationFromPSRML(getAlternativePS(target));
//        if (target instanceof Pattern) {
//            Location offset = ((Pattern) target).getTargetOffset();
//            loc.translate(offset.x, offset.y);
//        }
//        return loc;
//    }

    @SuppressWarnings("unchecked")
    private <PS> PS getAlternativePS(PS target) {
        if (target instanceof String) {
            target = (PS) getAlternativeFilename((String) target);
        } else if (target instanceof Pattern) {
            Pattern pattern = (Pattern) target;
            String filename = pattern.getFilename();
            String altFilename = getAlternativeFilename(filename);
            if (!altFilename.equals(filename)) {
                float similarity = extractPatternSimilarity(pattern);
                String debug = pattern.toString();
                pattern = new Pattern(altFilename);
                pattern = pattern.similar(similarity);
                Debug.action("Alternative pattern; " + debug + " --> " + pattern.toString());
            }
            target = (PS) pattern;
        }

        return target;
    }

    private float extractPatternSimilarity(Pattern pattern) { // tricky
        // Pattern("/path/to/image.png").similar(0.7)
        Matcher matcher = java.util.regex.Pattern.compile(
                "\\.similar\\((.+?)\\)").matcher(pattern.toString());
        boolean found = matcher.find();
        assert found : pattern.toString();

        return Float.valueOf(matcher.group(1));
    }

    private String getAlternativeFilename(String filename) {
        // not a filename with the extension, or no alternative
        if (!filename.contains(".")) return filename;

        assert Settings.BundlePath != null;
        File file = new File(filename);
        if (!file.isAbsolute()) file = new File(Settings.BundlePath, filename);
        if (!file.exists()) {
            Debug.action("The image file passed in (" + filename + ") doesn't exist, so it might be a string for OCR");
            return filename; // might be a string for OCR
        }

        String ext = filename.substring(filename.lastIndexOf("."));
        String base_in = filename.substring(0, filename.lastIndexOf("."));
        String base = base_in.contains("__") ? base_in.substring(0, base_in.lastIndexOf("__")) : base_in;
        String device_id = _robot.getModel().replace(' ', '_').toLowerCase();

        // try device-specific file in the same folder first
        String fname_dev = base + "__" + device_id + ext;
        if (fname_dev.equals(filename)) {
            Debug.action("The image file passed in (" + filename + ") is already for the device under test.");
            return filename;
        }
        File file_dev = new File(fname_dev);
        if (!file_dev.isAbsolute()) file_dev = new File(Settings.BundlePath, fname_dev);
        if (file_dev.exists()) {
            Debug.action("The image file specific to the device under test exists. (" + filename + " -> " + fname_dev + ")");
            return fname_dev;
        }

        // try device-specific file in the sibling folder, named by the module name
        File sibling_dir = new File(file.getParentFile().getParentFile().getPath(), device_id);
        file_dev = new File(sibling_dir, new File(base).getName() + ext);
        if (file_dev.exists()) {
            Debug.action("The image file specific to the device under test exists in the sibling folder. (" + filename + " -> " + file_dev.getPath() + ")");
            return file_dev.getPath();
        }

        // try generic one, if the filename passed in is an device-specific one.
        if (base != base_in) {
            String fname_comm = base + ext;
            File file_comm = new File(fname_comm);
            if (!file_comm.isAbsolute()) file_comm = new File(Settings.BundlePath, fname_comm);
            if (file_comm.exists()) {
                Debug.action("The image file passed in (" + filename + ") is for another device. The common version (" + fname_comm + ") will be used.");
                return fname_comm;
            }
        }

        Debug.action("Although the string passed in (" + filename + ") seems like a file name, all possible variations (model: " + device_id + ") do not exist. The string will be used as is.");
        return filename;
    }

}
