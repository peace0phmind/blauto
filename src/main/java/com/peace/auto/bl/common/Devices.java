package com.peace.auto.bl.common;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.task.DengLu;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by mind on 5/25/16.
 */
@Slf4j
@Data
public class Devices {
//    public static final Device DEVICE = new Device("efc444e7-aeb9-4ce4-8993-9e777ed033d9", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_1");
    public static final Device DEVICE_1 = new Device("4c7824f6-fcd4-4027-955d-c94b299c44ec", "device1");
    public static final Device DEVICE_2 = new Device("d11a2220-e8b9-480e-851e-b62e6953dadc", "device2");
    public static final Device DEVICE_3 = new Device("8e415fa8-fabd-4429-9c62-4ee3a6b3e9b3", "device3");
    public static final Status status = new Status();
    public static final DengLu DENG_LU = new DengLu();
}
