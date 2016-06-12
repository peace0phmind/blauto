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
    public static final Device DEVICE_1 = new Device("a2e70946-cb12-4e1f-a172-34ab5e06f745", "device1");
    public static final Device DEVICE_2 = new Device("0a8f9229-849b-47a9-bc34-6803143d1a5c", "device2");
    public static final Device DEVICE_3 = new Device("868472e5-e924-4700-838d-f7a6aabc37c9", "device3");
    public static final Status status = new Status();
    public static final DengLu DENG_LU = new DengLu();
}
