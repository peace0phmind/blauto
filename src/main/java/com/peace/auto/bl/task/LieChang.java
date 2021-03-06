package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.List;
import java.util.Optional;

/**
 * Created by mind on 3/7/16.
 */
@Slf4j
public class LieChang implements IDo {

    String baseDir = Common.BASE_DIR + "liechang/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        Match liechang = region.exists(baseDir + "liechang.png");
        if (liechang != null) {
            liechang.click();

            Thread.sleep(1000L);

            // 征收
            if (status.canDo(Task.LIE_CHANG_ZHENG_SHOU)) {
                Match zhengshou = region.exists(baseDir + "zhengshou.png");
                if (zhengshou != null && isButtonEnable(zhengshou, 10, 10)) {
                    zhengshou.click();
                    status.Done(Task.LIE_CHANG_ZHENG_SHOU);
                }
                Thread.sleep(500L);
            }

            // 每天强制征收
            if (status.canDo(Task.LIE_CHANG_QIANG_ZHENG)) {
                Match qiangzhizhengshou = region.exists(baseDir + "qiangzhizhengshou.png");
                if (qiangzhizhengshou != null && isButtonEnable(qiangzhizhengshou, 10, 10)) {
                    qiangzhizhengshou.click();

                    Pattern zhengshou1ci = new Pattern(baseDir + "zhengshou1ci.png").similar(0.99f);
                    for (int i = 0; i < 21; i++) {
                        Match zhenshou1 = region.exists(zhengshou1ci);
                        if (zhenshou1 != null) {
                            for (int j = 0; j < Task.LIE_CHANG_QIANG_ZHENG.getDayLimit(status.getCurrentUser()); j++) {
                                Thread.sleep(1000L);
                                region.click(baseDir + "zhengshouanniu.png");
                                status.Done(Task.LIE_CHANG_QIANG_ZHENG);
                            }
                            break;
                        }
                        region.click(baseDir + "jian.png");
                    }

                    region.click(Common.CLOSE);
                } else {
                    status.Done(Task.LIE_CHANG_QIANG_ZHENG);
                    status.Done(Task.LIE_CHANG_QIANG_ZHENG);
                    status.Done(Task.LIE_CHANG_QIANG_ZHENG);
                }

                Thread.sleep(500L);
            }

            // 打怪
            if (status.canDo(Task.LIE_CHANG_DA_GUAI)) {

                while (true) {
                    Match right = region.exists(new Pattern(baseDir + "right.png").similar(0.98f));
                    if (right == null) {
                        break;
                    }
                    right.click();
                    Thread.sleep(2000L);
                }

                Match guaiwu = region.exists(baseDir + "guaiwu.png", 10);
                if (guaiwu != null) {
                    List<Match> guaiwus = Lists.newArrayList(region.findAll(baseDir + "guaiwu.png"));

                    Thread.sleep(3000L);
                    Optional<Match> firstguaiwu = guaiwus.stream().sorted((x, y) -> x.getX() - y.getX()).findFirst();
                    if (firstguaiwu.isPresent()) {
                        firstguaiwu.get().click();

                        Thread.sleep(1000L);
                        region.click(baseDir + "kaishichuangguan.png");

                        Thread.sleep(1000L);

                        Match peacemianfei = region.exists(new Pattern(baseDir + "peacemianfei.png").similar(0.95f));
                        if (peacemianfei != null) {
                            region.click(baseDir + "kaishizhandou.png");

                            Match dianjijixu = region.exists(baseDir + "dianjipingmujixu.png", 10);
                            while (dianjijixu != null) {
                                dianjijixu.click();
                                Thread.sleep(500L);
                                dianjijixu = region.exists(baseDir + "dianjipingmujixu.png");
                            }

                            Match jixu = region.exists(baseDir + "jixu.png");
                            if (jixu != null) {
                                jixu.click();
                                Thread.sleep(500L);
                            }

                            Match dengdai = region.exists(baseDir + "dengdai.png");
                            if (dengdai != null) {
                                dengdai.click();

                                Match xuanguan = region.exists(baseDir + "xuanguan.png");
                                if (xuanguan != null) {
                                    xuanguan.click();
                                    status.Done(Task.LIE_CHANG_DA_GUAI);
                                }
                            }
                        } else {
                            status.Done(Task.LIE_CHANG_DA_GUAI, Status.nextCheck());
                            region.click(Common.CLOSE);
                            Thread.sleep(500L);
                            region.click(baseDir + "close.png");
                        }
                    }
                }
            }

            Thread.sleep(3000L);
            region.click(baseDir + "huicheng.png");

            return true;
        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.LIE_CHANG_ZHENG_SHOU, userName)
                && !status.canDo(Task.LIE_CHANG_DA_GUAI, userName)
                && !status.canDo(Task.LIE_CHANG_QIANG_ZHENG, userName)) {
            return false;
        }

        return true;
    }
}
