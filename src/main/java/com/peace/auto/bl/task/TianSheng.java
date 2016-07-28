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
import java.util.List;
import java.util.Optional;

import static com.peace.auto.bl.common.Devices.DENG_LU;

/**
 * Created by mind on 3/6/16.
 */
@Slf4j
public class TianSheng implements IDo {
    String baseDir = Common.BASE_DIR + "tiansheng/";

    Pattern sanxingpng = new Pattern(baseDir + "sanxing.png").similar(0.7f);

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        Match tiansheng = region.exists(new Pattern(baseDir + "tiansheng.png").similar(0.9f), 10);
        log.info("{}", tiansheng);
        if (tiansheng != null) {
            tiansheng.click();
            Thread.sleep(100L);

            if (status.canDo(Task.TIAN_SHEN_QI_DAO) || status.canDo(Task.TIAN_SHEN_QI_DAO_GAO_JI)) {
                // 祈祷
                boolean bQidao = false;
                boolean bGaoJiQiDao = false;
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

                            Match shurumima = region.exists(baseDir + "shurumima.png", 6);
                            if (shurumima != null) {
                                Thread.sleep(3000L);
                                shurumima.type(DENG_LU.getPassword());
                                Thread.sleep(2000L);
                                region.click(Common.QUE_DING);
                                Thread.sleep(1000L);
                                mf.below().click(baseDir + "qidaoanniu.png");
                            }

                            if (mf.getX() > 400) {
                                bGaoJiQiDao = true;
                                status.Done(Task.TIAN_SHEN_QI_DAO_GAO_JI);
                            } else {
                                bQidao = true;
                                status.Done(Task.TIAN_SHEN_QI_DAO);
                            }
                        }
                    }

                    if (status.canDo(Task.TIAN_SHEN_QI_DAO) && !bQidao) {
                        status.Done(Task.TIAN_SHEN_QI_DAO, Status.nextCheck());
                    }

                    if (status.canDo(Task.TIAN_SHEN_QI_DAO_GAO_JI) && !bGaoJiQiDao) {
                        status.Done(Task.TIAN_SHEN_QI_DAO_GAO_JI, Status.nextCheck());
                    }

                    region.click(Common.CLOSE);
                }
            }

            // 远古战场
            if (status.canDo(Task.TIAN_SHEN_YUAN_GU)) {
                if (!yuangu(region, status, true)) {
                    yuangu(region, status, false);
                }
            }

            if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN) || status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI)) {
                boolean bShenHun = false;
                boolean bGaoJiShenHun = false;
                Match huishi = region.exists(baseDir + "huishi.png");
                if (huishi != null) {
                    huishi.click();
                    Thread.sleep(3000L);

                    Match huoqushenhun = region.exists(baseDir + "huoqushenhun.png");
                    if (huoqushenhun != null) {
                        huoqushenhun.click();
                        Thread.sleep(3000L);

                        Pattern mfzh = new Pattern(baseDir + "mianfeizhaohuan.png").similar(0.9f);
                        Match mianfeizhaohuan = region.exists(mfzh);
                        if (mianfeizhaohuan != null) {
                            Iterator<Match> all = region.findAll(mfzh);
                            while (all.hasNext()) {
                                Match next = all.next();
                                next.below().click(baseDir + "zhaohuanyici.png");

                                Match queding = region.exists(Common.QUE_DING);
                                if (queding != null) {
                                    queding.click();
                                    Thread.sleep(3000l);
                                }

                                if (next.getX() > 400) {
                                    bGaoJiShenHun = true;
                                    status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI);
                                } else {
                                    bShenHun = true;
                                    status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_HUN);
                                }
                            }
                        }

                        if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN) && !bShenHun) {
                            status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_HUN, Status.nextCheck());
                        }

                        if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI) && !bGaoJiShenHun) {
                            status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI, Status.nextCheck());
                        }

                        region.click(Common.CLOSE);
                    }

                    Thread.sleep(1000L);
                    region.click(Common.CLOSE);
                }
            }

            // TODO 单独测试一下天神乱斗的情况,看是否有close不掉的问题,注意——单独
            if (status.canDo(Task.TIAN_SHEN_LUAN_DOU)) {
                // 天神大乱斗
                Match tianshendaluandou = region.exists(baseDir + "tianshendaluandou.png");
                if (tianshendaluandou != null) {
                    tianshendaluandou.click();
                    Thread.sleep(1000L);

                    findLastFinishPage(region);

                    Match sanxing = region.exists(sanxingpng);
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
                    } else {
                        status.Done(Task.TIAN_SHEN_LUAN_DOU, Status.nextDayCheck());
                    }

                    Thread.sleep(500L);
                    region.click(Common.CLOSE);
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
                && !status.canDo(Task.TIAN_SHEN_QI_DAO_GAO_JI, userName)
                && !status.canDo(Task.TIAN_SHEN_YUAN_GU, userName)
                && !status.canDo(Task.TIAN_SHEN_LUAN_DOU, userName)
                && !status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI, userName)
                && !status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN, userName)) {
            return false;
        }

        return true;
    }

    private boolean yuangu(Region region, Status status, boolean bJingYing) throws InterruptedException, FindFailed {
        boolean ret = false;

        Match yuanguzhanchang = region.exists(baseDir + "yuanguzhanchang.png");
        if (yuanguzhanchang != null) {
            yuanguzhanchang.click();
            Thread.sleep(1000L);

            if (bJingYing) {
                region.click(baseDir + "jingying.png");
                Thread.sleep(1000L);
            }

            findLastFinishPage(region);

            Match sanxing = region.exists(sanxingpng);
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
                            ret = true;
                            break;
                        }

                        if (bJingYing) {
                            Match cishushangxian = region.exists(baseDir + "cishudadaoshangxian.png", 1);
                            if (cishushangxian != null) {
                                Thread.sleep(500L);
                                region.click(Common.QUE_DING);
                                break;
                            }
                        }
                    }
                }
            }

            Thread.sleep(500L);
            region.click(Common.CLOSE);
        }

        return ret;
    }

    private void findLastFinishPage(Region region) throws InterruptedException {
        Match right = region.exists(baseDir + "right.png");
        while (right != null) {
            right.click();
            Thread.sleep(500L);
            right = region.exists(baseDir + "right.png");
        }

        Match sanxing = region.exists(sanxingpng);
        if (sanxing == null) {
            Match left = region.exists(baseDir + "left.png");
            if (left != null) {
                left.click();
                Thread.sleep(500L);
            }
        }
    }
}
