package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by mind on 7/16/16.
 */
@Slf4j
public class ExchangeCodeTask implements IDo {

    String baseDir = Common.BASE_DIR + "code/";

    @Override
    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        // 弹出对话聊天框
        Match duihua = region.exists(baseDir + "duihua.png");
        if (duihua != null) {
            duihua.click();
            Thread.sleep(1000L);
        }

        Match duihuashouqi = region.exists(baseDir + "duihuashouqi.png");
        if (duihuashouqi != null) {
            duihuashouqi.left(30).click();

            Match inliaotian = region.exists(baseDir + "inliaotian.png", 6);
            log.info("inliaotian: {}", inliaotian);
            if (inliaotian != null) {
                Match lianmeng = region.exists(baseDir + "lianmeng.png");
                if (lianmeng != null) {
                    lianmeng.click();
                    Thread.sleep(1000L);

                    Match shuru = region.exists(baseDir + "duihuashuru.png");
                    if (shuru != null) {
                        List<ExchangeCode> exchangeableCodes = status.getExchangeableCodes(LocalDateTime.now(), status.getCurrentUser());

                        if (exchangeableCodes.size() > 0) {
                            for (ExchangeCode ec : exchangeableCodes) {
                                if (ec.getBeginTime().isBefore(LocalDateTime.now())) {
                                    shuru.type(ec.getCode());
                                    Thread.sleep(1000L);
                                    region.click(baseDir + "fasong.png");

                                    // exchange code done
                                    status.CodeExchanged(ec.getId(), status.getCurrentUser());
                                    Thread.sleep(1000L);
                                }
                            }
                        }
                    }
                }

                Thread.sleep(1000L);
                region.click(Common.CLOSE);

                Thread.sleep(3000L);
                new Mail().Done(region, status);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (status.canDo(Task.EXCHANGE_CODE, userName)) {
            return status.getExchangeableCodes(LocalDateTime.now(), userName).size() > 0;
        }

        return false;
    }
}
