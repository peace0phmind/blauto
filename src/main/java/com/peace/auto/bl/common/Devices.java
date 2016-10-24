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
    public static final Device DEVICE_1 = new Device("7ae02055-7415-4a14-9058-75a681d5152f", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800");
    public static final Device DEVICE_2 = new Device("417466f3-8209-4d73-aa84-90d0dd445656", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_1");
    public static final Device DEVICE_3 = new Device("b6f0f543-4c68-48b5-9066-e62df5f9d593", "Samsung Galaxy S2 - 4.1.1 - API 16 - 480x800_2");
    public static final Status status = new Status();
    public static final DengLu DENG_LU = new DengLu();
}
