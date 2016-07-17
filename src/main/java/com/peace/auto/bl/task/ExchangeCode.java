package com.peace.auto.bl.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by mind on 7/16/16.
 */
@Data
@AllArgsConstructor
public class ExchangeCode {
    /**
     * id
     */
    private int id;

    /**
     * exchange code
     */
    private String code;

    /**
     * exchange code begin time
     */
    private LocalDateTime beginTime;

    /**
     * exchange code end time
     */
    private LocalDateTime endTime;

    /**
     * user do exchange time
     */
    private LocalDateTime executeTime;
}
