package com.peace.auto.bl.common;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.task.DengLu;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mind on 5/28/16.
 */
@Slf4j
public class Device {
    public static final DengLu DENG_LU = new DengLu();
    private static final String PLAY_PATH = "/Users/mind/Applications/Genymotion.app/Contents/MacOS/player.app/Contents/MacOS/player";
    private static final String HIDE_PLAYER_COMMAND = "tell application \"System Events\" to set visible of application process \"player\" to false";

    private static String script = "tell application \"System Events\"\n" +
            "\trepeat with player in (every process whose name is \"player\")\n" +
            "\t\ttell player\n" +
            "\t\t\trepeat with win in windows\n" +
            "\t\t\t\tif (name of win) contains \"%s \" then\n" +
            "\t\t\t\t\tclick ((buttons of win) whose description is \"%s\")\n" +
            "\t\t\t\tend if\n" +
            "\t\t\tend repeat\n" +
            "\t\tend tell\n" +
            "\tend repeat\n" +
            "end tell";
    private static String closeButton = "close button";
    private static String minimizeButton = "minimize button";


    private String id;
    private String description;
    private AndroidScreen region;


    public Device(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public AndroidScreen startGame(Status status) throws IOException, InterruptedException, FindFailed {
        return startGame(status, false);
    }

    public AndroidScreen startGame(Status status, boolean visible) throws IOException, InterruptedException, FindFailed {
        AndroidScreen region = startDevice(visible);
        DENG_LU.QiDong(region, status);
        return region;
    }

    public AndroidScreen startDevice(boolean visible) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String[] args = {"osascript", "-e", String.format(script, description, minimizeButton)};

//        rt.exec(String.format("%s -x --vm-name %s --no-popup", PLAY_PATH, device.getId()));
//        Thread.sleep(10 * 1000L);
        rt.exec(String.format("%s --vm-name %s", PLAY_PATH, id));

        for (int i = 0; i < 30; i++) {
            if (!visible) {
                rt.exec(args);
            }
            Thread.sleep(1 * 1000L);
        }

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", id));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();
        ip = ip.replaceAll("Value: ", "") + ":5555";

        log.info("ip: {}", ip);

        return new AndroidScreen(ip.trim());
    }

    public AndroidScreen startDevice() throws IOException, InterruptedException {
        return startDevice(false);
    }

    public AndroidScreen getRegion(Status status) throws IOException, InterruptedException, FindFailed {
        return getRegion(status, false);
    }

    public AndroidScreen getRegion(Status status, boolean visible) throws IOException, InterruptedException, FindFailed {
        if (region != null) {
            return region;
        }

        Runtime rt = Runtime.getRuntime();

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", id));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();

        if ("No value set!".equals(ip)) {
            log.info("No value set! Starting game.");
            region = startGame(status, visible);
        } else {
            ip = ip.replaceAll("Value: ", "") + ":5555";
            log.info("ip: {}", ip);
            region = new AndroidScreen(ip.trim());
        }

        return region;
    }

    public void stopDevice() throws IOException, InterruptedException {
        if (region != null) {
            region.close();
            region = null;
        }

        Runtime rt = Runtime.getRuntime();
//        rt.exec(String.format("%s -x --vm-name %s", PLAY_PATH, device.getId()));

        String[] args = {"osascript", "-e", String.format(script, description, closeButton)};
        log.info("{}", args[2]);
        rt.exec(args);

        rt.exec(String.format("VBoxManage controlvm %s poweroff", id));

        Thread.sleep(6000L);

        rt.exec("adb kill-server");
    }
}