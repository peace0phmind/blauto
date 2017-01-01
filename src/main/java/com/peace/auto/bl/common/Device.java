package com.peace.auto.bl.common;

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

    private static int deviceCount = 0;

    private String id;
    private String description;
    private AndroidScreen region;


    public Device(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public static void killAllBoxSVC() throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        rt.exec("adb kill-server");
        rt.exec("killall VBoxSVC");
        rt.exec("killall VBoxNetDHCP");

        Thread.sleep(6000L);
    }

    private AndroidScreen startDevice(boolean visible) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String[] args = {"osascript", "-e", String.format(script, description, minimizeButton)};

        rt.exec(String.format("%s -x --vm-name %s --no-popup", PLAY_PATH, id));
        Thread.sleep(10 * 1000L);
        rt.exec(String.format("%s --vm-name %s", PLAY_PATH, id));

        for (int i = 0; i < 30; i++) {
            if (!visible) {
                Process exec = rt.exec(args);
                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(exec.getErrorStream()));

                BufferedReader stdOut = new BufferedReader(new
                        InputStreamReader(exec.getInputStream()));

                log.info("{}, {}", stdError.readLine(), stdOut.readLine());
            }
            Thread.sleep(1 * 1000L);
        }

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", id));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();
        ip = ip.replaceAll("Value: ", "") + ":5555";

        log.info("ip: {}", ip);

        return new AndroidScreen(ip.trim());
    }

    public AndroidScreen getRegion() throws IOException, InterruptedException, FindFailed {
        return getRegion(false);
    }

    public AndroidScreen getRegion(boolean visible) throws IOException, InterruptedException, FindFailed {
        if (region != null) {
            return region;
        }

        Runtime rt = Runtime.getRuntime();

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", id));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();

        if ("No value set!".equals(ip)) {
            log.info("No value set! Starting device.");
            region = startDevice(visible);
        } else {
            ip = ip.replaceAll("Value: ", "") + ":5555";
            log.info("ip: {}", ip);
            region = new AndroidScreen(ip.trim());
        }

        deviceCount += 1;

        return region;
    }

    public void stopDevice() throws IOException, InterruptedException {
        if (region != null) {
            region.close();
            region = null;
            deviceCount -= 1;

            Runtime rt = Runtime.getRuntime();

            String[] args = {"osascript", "-e", String.format(script, description, closeButton)};
            log.info("{}", args[2]);
            rt.exec(args);

            Thread.sleep(5000L);

            rt.exec(String.format("%s -x --vm-name %s", PLAY_PATH, id));

//            if (deviceCount == 0) {
//                log.info("do extra clean");
//                // rt.exec(String.format("VBoxManage controlvm %s poweroff", id));
//                killAllBoxSVC();
//            }
        }
    }
}