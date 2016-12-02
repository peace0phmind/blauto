package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.natives.Mat;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.awt.*;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.peace.auto.bl.common.Devices.DENG_LU;
import static java.time.DayOfWeek.SUNDAY;


/**
 * Created by mind on 3/6/16.
 */
@Slf4j
public class TianSheng implements IDo {
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

            if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN) || status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI)
                    || status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_JI) || status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_JI_GAO_JI)) {
                boolean bShenHun = false;
                boolean bShenJi = false;
                boolean bGaoJiShenHun = false;
                boolean bGaoJiShenJi = false;
                Match hunshi = region.exists(baseDir + "hunshi.png");
                if (hunshi != null) {
                    hunshi.click();
                    Thread.sleep(3000L);

                    Match huoqushenhun = region.exists(baseDir + "huoqushenhun.png");
                    if (huoqushenhun != null) {
                        huoqushenhun.click();
                        Thread.sleep(3000L);

                        if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN) || status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI)) {
                            Pattern mfzh = new Pattern(baseDir + "mianfeizhaohuan.png").similar(0.9f);
                            Match mianfeizhaohuan = region.exists(mfzh);
                            if (mianfeizhaohuan != null) {
                                Iterator<Match> all = region.findAll(mfzh);
                                while (all.hasNext()) {
                                    Match next = all.next();
                                    next.below().click(baseDir + "zhaohuanyici.png");

                                    if (next.getX() > 400) {
                                        Match mima = region.exists(baseDir + "shurumima.png", 6);
                                        if (mima != null) {
                                            Thread.sleep(3000L);
                                            mima.type(DENG_LU.getPassword());
                                            Thread.sleep(2000L);
                                            region.click(Common.QUE_DING);
                                            Thread.sleep(2000L);
                                            next.below().click(baseDir + "zhaohuanyici.png");
                                        }

                                        bGaoJiShenHun = true;
                                        status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI);
                                    } else {
                                        bShenHun = true;
                                        status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_HUN);
                                    }

                                    Match queding = region.exists(Common.QUE_DING);
                                    if (queding != null) {
                                        queding.click();
                                        Thread.sleep(3000l);
                                    }
                                }
                            }
                        }

                        if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_JI) || status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_JI_GAO_JI)) {
                            Match shenji = region.exists(baseDir + "zhaohuanshenji.png");
                            if (shenji != null) {
                                shenji.click();
                                Thread.sleep(3000L);
                            }

                            Pattern mfzh = new Pattern(baseDir + "mianfeizhaohuan.png").similar(0.9f);
                            Match mianfeizhaohuan = region.exists(mfzh);
                            if (mianfeizhaohuan != null) {
                                Iterator<Match> all = region.findAll(mfzh);
                                while (all.hasNext()) {
                                    Match next = all.next();
                                    next.below().click(baseDir + "zhaohuanyici.png");

                                    if (next.getX() > 400) {
                                        Match mima = region.exists(baseDir + "shurumima.png", 6);
                                        if (mima != null) {
                                            Thread.sleep(3000L);
                                            mima.type(DENG_LU.getPassword());
                                            Thread.sleep(2000L);
                                            region.click(Common.QUE_DING);
                                            Thread.sleep(2000L);
                                            next.below().click(baseDir + "zhaohuanyici.png");
                                        }

                                        bGaoJiShenJi = true;
                                        status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_JI_GAO_JI);
                                    } else {
                                        bShenJi = true;
                                        status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_JI);
                                    }

                                    Match queding = region.exists(Common.QUE_DING);
                                    if (queding != null) {
                                        queding.click();
                                        Thread.sleep(3000l);
                                    }
                                }
                            }
                        }


                        if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN) && !bShenHun) {
                            status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_HUN, Status.nextCheck());
                        }

                        if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI) && !bGaoJiShenHun) {
                            status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI, Status.nextCheck());
                        }

                        if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_JI) && !bShenJi) {
                            status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_JI, Status.nextCheck());
                        }

                        if (status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_JI_GAO_JI) && !bGaoJiShenJi) {
                            status.Done(Task.TIAN_SHEN_HUO_QU_SHEN_JI_GAO_JI, Status.nextCheck());
                        }

                        region.click(Common.CLOSE);
                    }

                    Thread.sleep(1000L);
                    region.click(Common.CLOSE);
                }
            }
            if (status.canDo(Task.TIAN_SHEN_HUN_JIE)) {
                try {
                    hunjie(region, status);
                } catch (IOException e) {
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

                    sanxingpng = new Pattern(baseDir + "sanxing.png").similar(0.8f);
                    if (LocalDateTime.now().getDayOfWeek() == SUNDAY) {
                        sanxingpng = new Pattern(baseDir + "sanxing_sunday.png").similar(0.8f);
                    }

                    Match sanxing = region.exists(sanxingpng);
                    if (sanxing != null) {
                        List<Match> sanxings = Lists.newArrayList(region.findAll(sanxingpng));

                        List<Match> lists = sanxings.stream().sorted((x, y) -> y.getX() - x.getX()).sorted((x, y) -> y.getY() - x.getY()).collect(Collectors.toList());
                        if (lists.size() > 0) {
                            int j = 0;
                            Match sx = lists.get(j);
                            log.info("{}", sx);
                            sx.click();
                            Thread.sleep(5000L);

                            for (int i = 0; i < 25; i++) {
                                region.click(baseDir + "saodang.png");
                                Match shenglibugou = region.exists(baseDir + "hunlibuzu.png", 1);
                                if (shenglibugou != null) {
                                    Thread.sleep(1000L);
                                    region.click(Common.QUE_DING);
                                    status.Done(Task.TIAN_SHEN_LUAN_DOU);
                                    break;
                                }

                                Match tiaozhanshangxian = region.exists(baseDir + "tiaozhanshangxian.png", 1);
                                if (tiaozhanshangxian != null) {
                                    Thread.sleep(1000L);
                                    region.click(Common.QUE_DING);
                                    Thread.sleep(1000L);
                                    region.click(baseDir + "xiaoclose.png");

                                    Thread.sleep(6000L);

                                    j++;
                                    if (j < lists.size()) {
                                        sx = lists.get(j);
                                        log.info("{}", sx);
                                        sx.click();
                                        Thread.sleep(3000L);
                                    } else {
                                        break;
                                    }
                                }
                            }

                            Thread.sleep(500L);
                            region.click(baseDir + "xiaoclose.png");
                        }
                    } else {
                        status.Done(Task.TIAN_SHEN_LUAN_DOU);
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
                && !status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_JI, userName)
                && !status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_JI_GAO_JI, userName)
                && !status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN_GAO_JI, userName)
                && !status.canDo(Task.TIAN_SHEN_HUO_QU_SHEN_HUN, userName)) {
            return false;
        }

        return true;
    }

    public boolean hunjie(Region region, Status status) throws InterruptedException, FindFailed, IOException {
        Match hunshi = region.exists(baseDir + "hunshi.png");
        if (hunshi != null) {
            hunshi.click();
            Thread.sleep(3000L);

            Match shenghunpeiyang = region.exists(baseDir + "shenghunpeiyang.png", 6);
            if (shenghunpeiyang != null) {
                shenghunpeiyang.click();
                Thread.sleep(2000L);

                Match up = region.exists(baseDir + "up.png", 6);
                if (up != null) {
                    up.below(60).click();
                    up.below(60).click();
                    up.below(60).click();
                }

                Match xuanzecailiao = region.exists(baseDir + "xuanzecailiao.png", 6);
                if (xuanzecailiao != null) {
                    xuanzecailiao.click();
                    Thread.sleep(2000L);

                    Region newReg = newRegion(region, new Rectangle(400, 0, 400, 480));

                    for (int i = 0; i < 5; i++) {
                        List<Match> cailiaos = getCaiLiaos(newReg);
                        if (cailiaos.size() == 0) {
                            region.click(baseDir + "rightcailiao.png");
                            continue;
                        }

                        log.info("{}", cailiaos);

                        cailiaos.forEach(x -> {
                            x.click();
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException e) {
                            }
                        });

                        region.click(baseDir + "shengji.png");

                        Match queding = region.exists(Common.QUE_DING);
                        if (queding != null) {
                            region.saveScreenCapture(".", "shen_hun");
                            queding.click();
                        }
                    }
                }

                Thread.sleep(1000L);
                region.click(Common.CLOSE);
            }

            Match hunjie = region.exists(baseDir + "jinruhunjie.png", 6);
            if (hunjie != null) {
                hunjie.click();
                Thread.sleep(3000L);

                Match guankaanniu = region.exists(baseDir + "guankaButton.png");
                if (guankaanniu != null) {
                    guankaanniu.click();
                    Thread.sleep(2000L);

                    if (jinRuGuanKa(region)) {
                        Match saodang = region.exists(baseDir + "shenhunsaodang.png");
                        if (saodang != null) {
                            Thread.sleep(1000L);
                            saodang.click();
                            Thread.sleep(1000L);

                            Match quxiao = region.exists(Common.QU_XIAO);
                            if (quxiao != null) {
                                if (status.canDo(Task.TIAN_SHEN_HUN_JIE_GOU_MAI)) {
                                    region.click(Common.QUE_DING);
                                    Thread.sleep(1000L);
                                    status.Done(Task.TIAN_SHEN_HUN_JIE_GOU_MAI);
                                    saodang.click();
                                    Thread.sleep(1000L);

                                    zaicisaodang(region, status, saodang);
                                } else {
                                    status.Done(Task.TIAN_SHEN_HUN_JIE);
                                    quxiao.click();
                                }
                            } else {
                                zaicisaodang(region, status, saodang);
                            }
                        }
                    } else {
                        status.Done(Task.TIAN_SHEN_HUN_JIE);
                    }

                    region.click(Common.CLOSE);
                    Thread.sleep(2000L);
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

    private List<Match> getCaiLiaos(Region region) throws IOException, FindFailed {
        List<Match> cailiaos = new ArrayList<>();
        DirectoryStream<Path> paths = Files.newDirectoryStream(FileSystems.getDefault().getPath(baseDir), "cailiao_*.png");
        for (Path path : paths) {
            Pattern png = new Pattern(path.toFile().toString()).similar(0.85f);
            Match p = region.exists(png, 0.5);
            if (p == null) {
                continue;
            }

            try {

                cailiaos.addAll(Lists.newArrayList(region.findAll(png)));
            } catch (Exception e) {
                log.info("{}", e);
                cailiaos.add(p);
            }
        }

        return cailiaos;
    }

    private void zaicisaodang(Region region, Status status, Region saodang) throws InterruptedException, FindFailed {
        Match zaicisaodang = region.exists(baseDir + "zaicisaodang.png");
        if (zaicisaodang != null) {
            for (int j = 0; j < 15; j++) {
                Thread.sleep(1000L);

                zaicisaodang.click();

                Match quxiao = region.exists(Common.QU_XIAO);
                if (quxiao != null) {
                    if (status.canDo(Task.TIAN_SHEN_HUN_JIE_GOU_MAI)) {
                        region.click(Common.QUE_DING);
                        Thread.sleep(1000L);
                        status.Done(Task.TIAN_SHEN_HUN_JIE_GOU_MAI);
                        saodang.click();
                        Thread.sleep(1000L);
                    } else {
                        status.Done(Task.TIAN_SHEN_HUN_JIE);
                        quxiao.click();
                        break;
                    }
                }
            }
        }

        clickInside(region, Common.CLOSE);
        Thread.sleep(2000L);
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

    private boolean jinRuGuanKa(Region region) throws FindFailed, InterruptedException {
        Pattern guanka = new Pattern(baseDir + "guanka.png").similar(0.95f);

        boolean bPuTongClicked = false;
        for (int i = 0; i < 10; i++) {
            Match mgk = region.exists(guanka);
            if (mgk != null) {
                mgk.click();
                Thread.sleep(2000L);
                return true;
            } else {
                if (bPuTongClicked) {
                    Match left = region.exists(new Pattern(baseDir + "leftgk.png").similar(0.95f));
                    if (left != null) {
                        left.click();
                        Thread.sleep(2000L);
                        bPuTongClicked = false;
                    }
                } else {
                    Match kunlan = region.exists(baseDir + "kunlan.png");
                    if (kunlan != null) {
                        kunlan.click();
                        Thread.sleep(2000L);

                        mgk = region.exists(guanka);
                        if (mgk != null) {
                            mgk.click();
                            Thread.sleep(2000L);
                            return true;
                        }
                    }

                    Match putong = region.exists(baseDir + "putong.png");
                    if (putong != null) {
                        putong.click();
                        Thread.sleep(2000L);
                        bPuTongClicked = true;
                    }
                }
            }
        }

        return false;
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
