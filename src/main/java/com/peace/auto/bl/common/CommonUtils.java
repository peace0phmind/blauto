package com.peace.auto.bl.common;

import com.peace.auto.bl.task.DengLu;
import com.peace.auto.bl.Status;
import com.peace.sikuli.monkey.AndroidScreen;
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
    public static final String DEVICE_1 = "3e08a7ca-d763-44e3-88a8-ce4c1831a1f9";
    public static final String DEVICE_2 = "efc444e7-aeb9-4ce4-8993-9e777ed033d9";
    public static final String DEVICE_3 = "e10a2c0d-b1cd-40b2-be65-5b714fa9fea1";
    public static final Status status = new Status();
    public static final DengLu DENG_LU = new DengLu();

    private static final String PLAY_PATH = "/Users/mind/Applications/Genymotion.app/Contents/MacOS/player.app/Contents/MacOS/player";
    private static final String HIDE_PLAYER_COMMAND = "tell application \"System Events\" to set visible of application process \"player\" to false";

    public static AndroidScreen startDevice(String deviceId) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();

        rt.exec(String.format("%s -x --vm-name %s --no-popup", PLAY_PATH, deviceId));
        Thread.sleep(10 * 1000L);
        rt.exec(String.format("%s --vm-name %s --no-popup", PLAY_PATH, deviceId));

        for (int i = 0; i < 30; i++) {
            hidePlayer();
            Thread.sleep(1 * 1000L);
        }

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", deviceId));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();
        ip = ip.replaceAll("Value: ", "") + ":5555";

        log.info("ip: {}", ip);

        return new AndroidScreen(ip.trim());
    }

    public static AndroidScreen getRegion(String deviceId) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();

        Process exec = rt.exec(String.format("VBoxManage guestproperty get %s androvm_ip_management", deviceId));
        String ip = new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine();
        ip = ip.replaceAll("Value: ", "") + ":5555";

        log.info("ip: {}", ip);

        return new AndroidScreen(ip.trim());
    }

    public static void hidePlayer() throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] args = {"osascript", "-e", HIDE_PLAYER_COMMAND};
        rt.exec(args);
    }

    public static void stopDevice(String deviceId) throws IOException {
        Runtime rt = Runtime.getRuntime();

        rt.exec(String.format("%s -x --vm-name %s --no-popup", PLAY_PATH, deviceId));
    }
}
