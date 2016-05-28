package com.peace.auto.bl.common;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.task.DengLu;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@Data
public class Devices {
    public static final Device DEVICE_1 = new Device("3e08a7ca-d763-44e3-88a8-ce4c1831a1f9", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800");
    public static final Device DEVICE_2 = new Device("efc444e7-aeb9-4ce4-8993-9e777ed033d9", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_1");
    public static final Device DEVICE_3 = new Device("e10a2c0d-b1cd-40b2-be65-5b714fa9fea1", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_2");
    public static final Status status = new Status();
    public static final DengLu DENG_LU = new DengLu();

    public static void killAllBoxSVC() throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();

        rt.exec("killall VBoxSVC");

        Thread.sleep(6000L);

        rt.exec("killall VBoxNetDHCP");

        Thread.sleep(6000L);
    }
}
