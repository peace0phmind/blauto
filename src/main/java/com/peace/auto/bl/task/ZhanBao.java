package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import com.peace.auto.bl.task.Common;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by mind on 3/12/16.
 */
abstract public class ZhanBao {

    private String baseDir = Common.BASE_DIR + "zhanbao/";

    public boolean canDo(Status status, String userName) {
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(Task.CHU_ZHENG_YE_GUAI,
                Task.LIAN_BING_CHANG,
                Task.SHI_LIAN_DONG).stream().allMatch(x -> {
            LocalDateTime lastFinishTime = status.getLastFinishTime(x, userName);
            if (lastFinishTime == null) {
                return true;
            } else {
                return now.isAfter(lastFinishTime);
            }
        });
    }

    public boolean canFight(Region region, Status status) throws FindFailed, InterruptedException {
        if (!canDo(status, status.getCurrentUser())) {
            return false;
        }

        boolean ret = false;
        // 先查看是否有战报
        region.click(Common.MENU);

        Match zhanbao = region.exists(new Pattern(baseDir + "zhanbao.png").similar(0.95f));
        if (zhanbao != null) {
            zhanbao.click();

            Match kongzhanbao = region.exists(baseDir + "kongzhanbao.png", 6);
            if (kongzhanbao != null && kongzhanbao.getScore() > 0.95) {
                ret = true;
            }

            region.click(Common.CLOSE);
        }

        return ret;
    }
}
