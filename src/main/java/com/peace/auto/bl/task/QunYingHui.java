package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.Iterator;

/**
 * Created by mind on 3/9/16.
 */
@Slf4j
public class QunYingHui implements IDo {
    String baseDir = Common.BASE_DIR + "qunyinghui/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match tiaozhan = region.exists(baseDir + "tiaozhan.png");
        if (tiaozhan != null) {
            tiaozhan.click();

            Match qunyinghuianniu = region.exists(baseDir + "longhuqunyinghui.png", 20);
            if (qunyinghuianniu != null) {
                qunyinghuianniu.click();

                Thread.sleep(3000L);

                Match qunyinhui = region.exists(baseDir + "qunyinghui", 20);
                if (qunyinhui != null) {
                    qunyinhui.click();

                    if (status.canDo(Task.QUN_YING_HUI_BAO_MING)) {
                        Match canzhan = region.exists(baseDir + "canzhan.png", 20);
                        if (canzhan != null) {
                            canzhan.click();
                            Thread.sleep(6000L);
                            status.Done(Task.QUN_YING_HUI_BAO_MING);
                        } else {
                            // 找不到参战,则默认认为已经执行过群英会
                            status.Done(Task.QUN_YING_HUI_BAO_MING);
                        }
                    }

                    if (status.canDo(Task.QUN_YING_HUI_LING_JIANG)) {
                        Match jiangli = region.exists(baseDir + "jiangli.png", 20);
                        if (jiangli != null) {
                            jiangli.click();

                            Thread.sleep(3000L);

                            Match lingqu = region.exists(baseDir + "lingqu.png");
                            if (lingqu != null && isButtonEnable(lingqu)) {
                                lingqu.click();
                            }

                            Match lingqujiangli = region.exists(new Pattern(baseDir + "lingqujiangli.png").similar(0.9f));
                            log.info("lingqujiagli: {}", lingqujiangli);
                            if (lingqujiangli != null) {
                                log.info("lingqujiagli: {}", isButtonEnable(lingqujiangli), isButtonEnable(lingqujiangli, 10, 10));
                                Iterator<Match> all = region.findAll(new Pattern(baseDir + "lingqujiangli.png").similar(0.9f));
                                Lists.newArrayList(all).forEach(x -> {
                                    if (isButtonEnable(x)) {
                                        x.click();
                                    }
                                });
                                Thread.sleep(3000L);
                            }
                        }

                        status.Done(Task.QUN_YING_HUI_LING_JIANG);
                    }


                    Thread.sleep(1000L);
                    region.click(Common.CLOSE);
                }

                Thread.sleep(1000L);
                region.click(Common.CLOSE);
            }

        }

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.QUN_YING_HUI_BAO_MING, userName)
                && !status.canDo(Task.QUN_YING_HUI_LING_JIANG, userName)) {
            return false;
        }

        return true;
    }
}
