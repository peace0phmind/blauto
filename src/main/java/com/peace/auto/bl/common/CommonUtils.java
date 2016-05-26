package com.peace.auto.bl.common;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.task.DengLu;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@Data
public class CommonUtils {
    public static final Device DEVICE_1 = new Device("3e08a7ca-d763-44e3-88a8-ce4c1831a1f9", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800");
    public static final Device DEVICE_2 = new Device("efc444e7-aeb9-4ce4-8993-9e777ed033d9", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_1");
    public static final Device DEVICE_3 = new Device("e10a2c0d-b1cd-40b2-be65-5b714fa9fea1", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_2");
    public static final Status status = new Status();
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

    public static AndroidScreen startDevice(Device device, boolean visible) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String[] args = {"osascript", "-e", String.format(script, device.getDescription(), minimizeButton)};

        rt.exec(String.format("%s -x --vm-name %s --no-popup", PLAY_PATH, device.getId()));
        Thread.sleep(10 * 1000L);
        rt.exec(String.format("%s --vm-name %s", PLAY_PATH, device.getId()));

        for (int i = 0; i < 30; i++) {
            if (!visible) {
                rt.exec(args);
            }
            Thread.sleep(1 * 1000L);
        }

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", device.getId()));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();
        ip = ip.replaceAll("Value: ", "") + ":5555";

        log.info("ip: {}", ip);

        return new AndroidScreen(ip.trim());
    }


    public static AndroidScreen startDevice(Device device) throws IOException, InterruptedException {
        return startDevice(device, false);
    }

    public static AndroidScreen getRegion(Device device) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", device.getId()));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();
        ip = ip.replaceAll("Value: ", "") + ":5555";

        log.info("ip: {}", ip);

        return new AndroidScreen(ip.trim());
    }

    public static void stopDevice(Device device) throws IOException {
        Runtime rt = Runtime.getRuntime();
//        rt.exec(String.format("%s -x --vm-name %s", PLAY_PATH, device.getId()));

        String[] args = {"osascript", "-e", String.format(script, device.getDescription(), closeButton)};
        log.info("{}", args[2]);
        rt.exec(args);

        rt.exec("adb kill-server");
    }
}

@Data
@AllArgsConstructor
class Device {
    private String id;
    private String description;
}
