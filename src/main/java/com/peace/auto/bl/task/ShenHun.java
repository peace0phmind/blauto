package com.peace.auto.bl.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sikuli.script.Match;

/**
 * Created by mind on 8/4/16.
 */
@Data
@AllArgsConstructor
class ShenHun {
    private ShenHunType type;
    private Match match;

    enum ShenHunType {
        DUN,
        DAO,
        MA,
        ZHANG;
    }
}