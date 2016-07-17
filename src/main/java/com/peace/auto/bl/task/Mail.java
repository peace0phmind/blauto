package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mind on 7/3/16.
 */
@Slf4j
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

                for (int i = 0; i < 10; i++) {
                    log.info("Do {} times check mail.", i);

                    Match huixingzhen = region.exists(baseDir + "huixingzhen.png");
                    if (huixingzhen != null) {
                        readMail(region, huixingzhen, status);
                    } else {
                        Match xin = region.exists(baseDir + "xincheckbox.png");
                        if (xin == null) {
                            break;
                        } else {
                            Iterator<Match> all = region.findAll(baseDir + "xincheckbox.png");
                            ArrayList<Match> allMailCheckBox = Lists.newArrayList(all);
                            for (Match mailCheckBox : allMailCheckBox) {
                                mailCheckBox.click();
                                Thread.sleep(500L);
                            }

                            deleteMail(region);

                            Match lingqufujian = region.exists(baseDir + "lingqufujian.png");
                            if (lingqufujian != null) {
                                region.click(Common.QUE_DING);
                                Thread.sleep(1000L);

                                for (Match mailCheckBox : allMailCheckBox) {
                                    readMail(region, mailCheckBox.left(100), status);
                                }

                                deleteMail(region);
                            }

                            Thread.sleep(1000L);
                        }
                    }
                }

                status.Done(Task.CLEAR_MAIL);

                Thread.sleep(500L);
                region.click(Common.CLOSE);
            }
        }

        return true;
    }

    private void deleteMail(Region region) throws FindFailed {
        region.click(baseDir + "shanchu.png");

        Match querenshanchu = region.exists(baseDir + "querenshanchu.png");
        if (querenshanchu != null) {
            region.click(Common.QUE_DING);
        }
    }

    private void readMail(Region region, Region mail, Status status) throws InterruptedException, FindFailed {
        mail.click();
        Thread.sleep(2000L);

        Match shouqu = region.exists(baseDir + "shouqu.png");
        if (shouqu != null) {
            region.saveScreenCapture(".", "mail-" + status.getCurrentUser());
            shouqu.click();
            Thread.sleep(1000L);
        }

        region.click(baseDir + "fanhui.png");
        Thread.sleep(2000L);
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return status.canDo(Task.CLEAR_MAIL, userName);
    }
}
