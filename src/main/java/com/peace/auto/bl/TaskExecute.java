package com.peace.auto.bl;

import org.sikuli.script.FindFailed;

import java.awt.*;

/**
 * Created by mind on 6/8/16.
 */
@FunctionalInterface
public interface TaskExecute {

    void execute(Rectangle rectangle) throws FindFailed, InterruptedException;
}
