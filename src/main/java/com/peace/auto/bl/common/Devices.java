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
    public static final Device DEVICE_1 = new Device("e624d029-08b0-408a-a0f8-e905ed5907c5", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800");
    public static final Device DEVICE_2 = new Device("24d9eb43-be77-4f2b-bc09-95b88483762b", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_1");
    public static final Device DEVICE_3 = new Device("2336669d-c9fe-4d4f-9489-3163f99b3507", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_2");
    public static final Status status = new Status();
    public static final DengLu DENG_LU = new DengLu();
}
