package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class ShenShouWu implements IDo {
    String baseDir = Common.BASE_DIR + "shenshouwu/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        // 点击收起对话框
        Match duihua = region.exists(Common.BASE_DIR + "guanbiduihua.png");
        if (duihua != null && duihua.getScore() > 0.95) {
            duihua.click();
            Thread.sleep(1000L);
        }

        // 进行喂食
        Match shenshouwu = region.exists(baseDir + "shenshouwu.png", 10);
        log.info("{}", shenshouwu);
        if (shenshouwu != null) {
            shenshouwu.click();

            Match inweishi = region.exists(baseDir + "weishi.png", 10);
            if (inweishi != null) {
                inweishi.click();

                Match weishi = region.find(baseDir + "chujishicai.png").below().find(baseDir + "doweishi.png");
                if (weishi != null) {
                    for (int i = 0; i < Task.SHEN_SHOU_WU.getDayLimit(status.getCurrentUser()); i++) {
                        weishi.click();
                        status.Done(Task.SHEN_SHOU_WU);

                        Match noweishi = region.exists(baseDir + "noweishi.png", 0.5);
                        if (noweishi != null) {
                            region.click(Common.QUE_DING);
                            for (int j = 0; j < (Task.SHEN_SHOU_WU.getDayLimit(status.getCurrentUser()) - i - 1); j++) {
                                status.Done(Task.SHEN_SHOU_WU);
                            }
                            break;
                        }
                    }
                }
            }

            region.click(Common.CLOSE);
        }

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.SHEN_SHOU_WU, userName);
    }
}
