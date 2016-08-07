package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.peace.auto.bl.common.Devices.DENG_LU;


/**
 * Created by mind on 3/6/16.
 */
@Slf4j
public class TianSheng implements IDo {
    private static final List<Location> kongweis = new ArrayList<Location>() {{
        add(new Location(318, 156));
        add(new Location(318, 246));
        add(new Location(318, 336));
        add(new Location(228, 156));
        add(new Location(228, 246));
        add(new Location(228, 336));
        add(new Location(138, 156));
        add(new Location(138, 246));
        add(new Location(138, 336));
    }};
    String baseDir = Common.BASE_DIR + "tiansheng/";
    Pattern sanxingpng = new Pattern(baseDir + "sanxing.png").similar(0.7f);

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(Common.RI_CHANG);
        Thread.sleep(6000L);

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
                Match hunshi = region.exists(baseDir + "hunshi.png");
                if (hunshi != null) {
                    hunshi.click();
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
            if (status.canDo(Task.TIAN_SHEN_HUN_JIE)) {
                hunjie(region, status);
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

            Thread.sleep(1500L);
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
                && !status.canDo(Task.TIAN_SHEN_HUN_JIE, userName)
                && !status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI, userName)
                && !status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN, userName)) {
            return false;
        }

        return true;
    }

    public boolean hunjie(Region region, Status status) throws InterruptedException, FindFailed {
        Match hunshi = region.exists(baseDir + "hunshi.png");
        if (hunshi != null) {
            hunshi.click();
            Thread.sleep(3000L);

            Match hunjie = region.exists(baseDir + "jinruhunjie.png", 6);
            if (hunjie != null) {
                hunjie.click();
                Thread.sleep(3000L);

                boolean firstjinru = true;

                for (int j = 0; j < 10; j++) {
                    Pattern guanka = new Pattern(baseDir + "guanka.png").similar(0.92f);
                    region.exists(guanka, 10);

                    ArrayList<Match> matches = Lists.newArrayList(region.findAll(guanka));
                    List<Match> sorted = matches.stream().filter(x -> x.y > 240).sorted((x, y) -> y.x - x.x).collect(Collectors.toList());
                    sorted.addAll(matches.stream().filter(x -> x.y <= 240).sorted((x, y) -> x.x - y.x).collect(Collectors.toList()));

                    log.info("{}", sorted);

                    if (sorted.size() > 2) {
                        sorted.get(1).click();
                        Thread.sleep(3000L);

                        if (firstjinru) {
                            kongweis.forEach(x -> {
                                doRobot(region, (robot) -> {
                                    robot.touch(x.x, x.y);
                                });
                            });

                            List<ShenHun> shenHuns = getShenHun(region.find(baseDir + "dingwei.png").right());

                            shenHuns = shenHuns.stream().sorted((x, y) -> x.getMatch().x - y.getMatch().x)
                                    .sorted((x, y) -> x.getType().ordinal() - y.getType().ordinal()).collect(Collectors.toList());

                            log.info("{}", shenHuns);

                            for (int i = 0; i < Math.min(shenHuns.size(), 6); i++) {
                                move(shenHuns.get(i).getMatch(), kongweis.get(i), 1000);
                            }

                            firstjinru = false;
                        }

//                        Pattern p1 = new Pattern(baseDir + "1.png").similar(0.85f);

//                        shenhuns.stream().filter(x -> x.getMatch().y <= 350 && x.getMatch().x <= 400).collect(Collectors.toList()).forEach(x -> {
//                            x.getMatch().click();
//                            try {
//                                Thread.sleep(1000L);
//                            } catch (InterruptedException e) {
//                            }
//                        });

//                        kongwei = Lists.newArrayList(region.findAll(p1)).stream().filter(x -> x.x < 400)
//                                .sorted((x, y) -> x.y / 10 - y.y / 10).sorted((x, y) -> y.x / 10 - x.x / 10).collect(Collectors.toList());

//                        shenhuns = shenhuns.stream().filter(x -> x.getMatch().y > 350).sorted((x, y) -> x.getMatch().x - y.getMatch().x)
//                                .sorted((x, y) -> x.getType().ordinal() - y.getType().ordinal()).collect(Collectors.toList());


                        Match kaishizhandou = region.exists(baseDir + "kaishizhandou.png");
                        if (kaishizhandou != null) {
                            kaishizhandou.click();
                            Thread.sleep(1000L);

                            Match quxiao = region.exists(Common.QU_XIAO);
                            if (quxiao != null) {
                                if (status.canDo(Task.TIAN_SHEN_HUN_JIE_GOU_MAI)) {
                                    region.click(Common.QUE_DING);
                                    Thread.sleep(1000L);
                                    status.Done(Task.TIAN_SHEN_HUN_JIE_GOU_MAI);
                                    kaishizhandou.click();
                                    Thread.sleep(1000L);
                                } else {
                                    status.Done(Task.TIAN_SHEN_HUN_JIE);
                                    quxiao.click();
                                    break;
                                }
                            }

                            region.click();
                            Match tiaoguo = region.exists(baseDir + "tiaoguo.png", 6);
                            if (tiaoguo == null) {
                                region.click();
                                tiaoguo = region.exists(baseDir + "tiaoguo.png", 6);
                            }
                            if (tiaoguo != null) {
                                tiaoguo.click();
                                Thread.sleep(3000L);

                                Match shengli = region.exists(baseDir + "shengli.png");
                                if (shengli != null) {
                                    region.click(Common.CLOSE);
                                    Thread.sleep(1000L);
                                }

                                Match baoxiang = region.exists(baseDir + "baoxiang.png", 10);
                                if (baoxiang != null) {
                                    for (int z = 0; z < 7; z++) {
                                        baoxiang.click();
                                        Match choujiangjieshu = region.exists(baseDir + "choujiangjieshu.png");
                                        if (choujiangjieshu != null) {
                                            break;
                                        }

                                        Match mantangcai = region.exists(baseDir + "mantangcai.png", 1);
                                        if (mantangcai != null) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        Thread.sleep(1000L);
                        region.click(Common.CLOSE);
                        Thread.sleep(3000L);
                    } else {
                        status.Done(Task.TIAN_SHEN_HUN_JIE);
                    }
                }

                Match kaishizhandou = region.exists(baseDir + "kaishizhandou.png");
                if (kaishizhandou != null) {
                    region.click(Common.CLOSE);
                }

                Match fanhui = region.exists(baseDir + "fanhui.png");
                if (fanhui != null) {
                    Thread.sleep(1000L);
                    fanhui.click();
                }
            }

            Thread.sleep(1000L);
            region.click(Common.CLOSE);
        }
        return false;
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

    private List<ShenHun> getShenHun(Region region) throws FindFailed {
        List<ShenHun> shenhuns = new ArrayList<>();
        Pattern ma = new Pattern(baseDir + "ma.png").similar(0.85f);
        if (region.exists(ma) != null) {
            shenhuns.addAll(Lists.newArrayList(region.findAll(ma)).stream().map(x -> new ShenHun(ShenHun.ShenHunType.MA, x)).collect(Collectors.toList()));
        }

        Pattern dun = new Pattern(baseDir + "dun.png").similar(0.85f);
        if (region.exists(dun) != null) {
            shenhuns.addAll(Lists.newArrayList(region.findAll(dun)).stream().map(x -> new ShenHun(ShenHun.ShenHunType.DUN, x)).collect(Collectors.toList()));
        }

        Pattern zhang = new Pattern(baseDir + "zhang.png").similar(0.85f);
        if (region.exists(zhang) != null) {
            shenhuns.addAll(Lists.newArrayList(region.findAll(zhang)).stream().map(x -> new ShenHun(ShenHun.ShenHunType.ZHANG, x)).collect(Collectors.toList()));
        }

        Pattern dao = new Pattern(baseDir + "dao.png").similar(0.85f);
        if (region.exists(dao) != null) {
            shenhuns.addAll(Lists.newArrayList(region.findAll(dao)).stream().map(x -> new ShenHun(ShenHun.ShenHunType.DAO, x)).collect(Collectors.toList()));
        }

        return shenhuns;
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
