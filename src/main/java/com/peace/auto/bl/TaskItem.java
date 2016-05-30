package com.peace.auto.bl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by mind on 5/29/16.
 */
@Data
@AllArgsConstructor
public class TaskItem implements Serializable {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 任务类型
     */
    private Task task;

    /**
     * 任务可执行时间
     */
    private LocalDateTime executableTime;
}
