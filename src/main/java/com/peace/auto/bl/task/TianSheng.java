package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import com.peace.auto.bl.task.Common;
import com.peace.auto.bl.task.IDo;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by mind on 3/6/16.
 */
@Slf4j
public class TianSheng implements IDo {
    String baseDir = Common.BASE_DIR + "tiansheng/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        Match tiansheng = region.exists(baseDir + "tiansheng.png", 3);
        if (tiansheng != null && tiansheng.getScore() > 0.9) {
            tiansheng.click();
            Thread.sleep(100L);

            if (status.canDo(Task.TIAN_SHEN_QI_DAO)) {
                // 祈祷
                Match qidao = region.exists(baseDir + "qidao.png", 3);
                if (qidao != null) {
                    qidao.click();
                    Thread.sleep(1000L);

                    Pattern mfp = new Pattern(baseDir + "mianfei.png").similar(0.95f);
                    Match mianfei = region.exists(mfp, 3);
                    if (mianfei != null) {
                        region.click(baseDir + "guanbijieguo.png");
                        Thread.sleep(500L);

                        Iterator<Match> all = region.findAll(mfp);
                        while (all.hasNext()) {
                            Match mf = all.next();
                            mf.below().click(baseDir + "qidaoanniu.png");
                            Thread.sleep(500L);
                        }

                        status.Done(Task.TIAN_SHEN_QI_DAO);
                    }

                    region.click(Common.CLOSE);
                }
            }

            if (status.canDo(Task.TIAN_SHEN_YUAN_GU)) {
                // 远古战场
                Match yuanguzhanchang = region.exists(baseDir + "yuanguzhanchang.png");
                if (yuanguzhanchang != null) {
                    yuanguzhanchang.click();
                    Thread.sleep(1000L);

                    Match right = region.exists(baseDir + "right.png");
                    while (right != null) {
                        right.click();
                        Thread.sleep(500L);
                        right = region.exists(baseDir + "right.png");
                    }

                    Pattern sanxingpng = new Pattern(baseDir + "sanxing.png").similar(0.6f);

                    Match sanxing = region.exists(sanxingpng);
                    if (sanxing == null) {
                        Match left = region.exists(baseDir + "left.png");
                        if (left != null) {
                            left.click();
                            Thread.sleep(500L);
                        } else {
                            return true;
                        }
                    }

                    sanxing = region.exists(sanxingpng);
                    if (sanxing != null) {
                        List<Match> sanxings = Lists.newArrayList(region.findAll(sanxingpng));

                        Optional<Match> lastsanxing = sanxings.stream().sorted((x, y) -> y.getX() - x.getX()).sorted((x, y) -> y.getY() - x.getY()).findFirst();
                        if (lastsanxing.isPresent()) {
                            Match sx = lastsanxing.get();
                            log.info("{}", sx);
                            sx.click();
                            Thread.sleep(500L);

                            for (int i = 0; i < 25; i++) {
                                region.click(baseDir + "saodang.png");
                                Match shenglibugou = region.exists(baseDir + "shenglibugou.png", 1);
                                if (shenglibugou != null) {
                                    Thread.sleep(500L);
                                    region.click(Common.QUE_DING);
                                    status.Done(Task.TIAN_SHEN_YUAN_GU);
                                    break;
                                }
                            }
                        }
                    }

                    Thread.sleep(500L);
                    region.click(Common.CLOSE);
                }
            }

            if (status.canDo(Task.TIAN_SHEN_LUAN_DOU)) {
                // 天神大乱斗
                Match tianshendaluandou = region.exists(baseDir + "tianshendaluandou.png");
                if (tianshendaluandou != null) {
                    tianshendaluandou.click();
                    Thread.sleep(1000L);

                    Match right = region.exists(baseDir + "right.png");
                    while (right != null) {
                        right.click();
                        Thread.sleep(500L);
                        right = region.exists(baseDir + "right.png");
                    }

                    Pattern sanxingpng = new Pattern(baseDir + "sanxing.png").similar(0.6f);

                    Match sanxing = region.exists(sanxingpng);
                    if (sanxing == null) {
                        Match left = region.exists(baseDir + "left.png");
                        if (left != null) {
                            left.click();
                            Thread.sleep(500L);
                        } else {
                            return true;
                        }
                    }

                    sanxing = region.exists(sanxingpng);
                    if (sanxing != null) {
                        List<Match> sanxings = Lists.newArrayList(region.findAll(sanxingpng));

                        Optional<Match> lastsanxing = sanxings.stream().sorted((x, y) -> y.getX() - x.getX()).sorted((x, y) -> y.getY() - x.getY()).findFirst();
                        if (lastsanxing.isPresent()) {
                            Match sx = lastsanxing.get();
                            log.info("{}", sx);
                            sx.click();
                            Thread.sleep(500L);

                            for (int i = 0; i < 25; i++) {
                                region.click(baseDir + "saodang.png");
                                Match shenglibugou = region.exists(baseDir + "hunlibuzu.png", 1);
                                if (shenglibugou != null) {
                                    Thread.sleep(500L);
                                    region.click(Common.QUE_DING);
                                    status.Done(Task.TIAN_SHEN_LUAN_DOU);
                                    break;
                                }
                            }

                            Thread.sleep(500L);
                            region.click(baseDir + "xiaoclose.png");
                        }

                        Thread.sleep(500L);
                        region.click(Common.CLOSE);
                    }
                }
            }

            Thread.sleep(500L);
            region.click(Common.CLOSE);
        }

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.TIAN_SHEN_QI_DAO, userName)
                && !status.canDo(Task.TIAN_SHEN_YUAN_GU, userName)) {
            return false;
        }

        return true;
    }
}