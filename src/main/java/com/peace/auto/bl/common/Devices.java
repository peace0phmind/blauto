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
    public static final Device DEVICE_1 = new Device("d8653e08-1255-4b69-926e-df05431736ec", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800");
    public static final Device DEVICE_2 = new Device("e6faded0-f5da-4ce2-ac17-f6279b24d494", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_1");
    public static final Device DEVICE_3 = new Device("d0ed3766-7b64-4810-b0e3-553f8d9d1bd3", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_2");
    public static final Status status = new Status();
    public static final DengLu DENG_LU = new DengLu();
}
