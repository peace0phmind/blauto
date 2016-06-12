package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by mind on 6/11/16.
 */
@Slf4j
public class TestMain {

    public static void main(String[] args) {
        while (true) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Test Main.");
        }
    }
}
