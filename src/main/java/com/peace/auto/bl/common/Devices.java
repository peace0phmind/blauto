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
    public static final Device DEVICE_1 = new Device("022bda98-7294-4bf8-9566-0b960ae44605", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800");
    public static final Device DEVICE_2 = new Device("e84d8131-37b2-4885-a657-d3ad97d29023", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_1");
    public static final Device DEVICE_3 = new Device("6b333677-2f82-4e71-8975-6158a34e6d48", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_2");
    public static final Status status = new Status();
    public static final DengLu DENG_LU = new DengLu();
}
