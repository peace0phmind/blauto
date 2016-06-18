package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import com.peace.auto.bl.task.Common;
import com.peace.auto.bl.task.IDo;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.List;
import java.util.Optional;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class XunBao implements IDo {

    String baseDir = Common.BASE_DIR + "xunbao/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {

        Match xunbao = region.exists(baseDir + "xunbao.png", 10);

        log.debug("{}", xunbao);
        if (xunbao != null) {
            Match inbaoshiwu = region.exists(baseDir + "inbaoshiwu.png", 10);
            if (inbaoshiwu == null) {
                return false;
            }

            for (int i = 0; i < 200; i++) {
                List<Match> list = Lists.newArrayList(region.findAll(baseDir + "xunbaobutton.png"));
                Optional<Match> lastButton = list.stream().sorted((a, b) -> b.x - a.x).findFirst();

                if (lastButton.isPresent()) {
                    Match match = lastButton.get();
                    match.click();
                }

                Match exists = region.exists(baseDir + "xunbaoend.png", 0.1);
                if (exists != null) {
                    exists.below().click(Common.QUE_DING);
                    status.Done(Task.XUN_BAO);
                    break;
                }
            }

            region.click(Common.CLOSE);
        }

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.XUN_BAO, userName);
    }
}
