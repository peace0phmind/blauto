package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.Iterator;

/**
 * Created by mind on 7/3/16.
 */
public class Mail implements IDo {
    String baseDir = Common.BASE_DIR + "mail/";

    @Override
    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(Common.MENU);

        Match xinfeng = region.exists(baseDir + "xinfeng.png");
        if (xinfeng != null) {
            xinfeng.click();

            Thread.sleep(3000L);

            Match shanchu = region.exists(baseDir + "shanchu.png");
            if (shanchu != null) {

                while (true) {
                    Match huixingzhen = region.exists(baseDir + "huixingzhen.png");
                    if (huixingzhen != null) {
                        huixingzhen.click();
                        Thread.sleep(1000L);

                        Match shouqu = region.exists(baseDir + "shouqu.png");
                        if (shouqu != null) {
                            shouqu.click();
                            Thread.sleep(1000L);
                        }

                        region.click(baseDir + "fanhui.png");
                    } else {
                        Match xin = region.exists(baseDir + "xincheckbox.png");
                        if (xin == null) {
                            break;
                        } else {
                            Iterator<Match> all = region.findAll(baseDir + "xincheckbox.png");
                            while (all.hasNext()) {
                                all.next().click();
                                Thread.sleep(500L);
                            }

                            region.click(baseDir + "shanchu.png");

                            Match querenshanchu = region.exists(baseDir + "querenshanchu.png");
                            if (querenshanchu != null) {
                                region.click(Common.QUE_DING);
                            }

                            Thread.sleep(1000L);
                        }
                    }
                }


                Thread.sleep(500L);
                region.click(Common.CLOSE);
            }
        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.CLEAR_MAIL, userName);
    }
}
