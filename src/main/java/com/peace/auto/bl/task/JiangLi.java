package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import sun.jvm.hotspot.debugger.ThreadAccess;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class JiangLi implements IDo {
    String baseDir = Common.BASE_DIR + "jiangli/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(new Pattern(baseDir + "jiangli.png").similar(0.9f));

        Match injiangli = region.exists(baseDir + "injiangli.png", 20);
        if (injiangli == null) {
            return false;
        }

        if (status.canDo(Task.MEI_RI_JIANG_LI)) {
            Match qiandao = region.exists(baseDir + "qiandao.png", 1);
            if (qiandao != null) {
                qiandao.click();
            }

            Match lianxuqiandaojiangli = region.exists(baseDir + "lingquqiandaojiangli.png", 1);
            if (lianxuqiandaojiangli != null) {
                lianxuqiandaojiangli.click();
            }

            // 连续登陆
            region.click(baseDir + "lianxudenglu.png");

            Match lianxulingqu = region.exists(baseDir + "lianxulingqu.png", 1);
            if (lianxulingqu != null) {
                lianxulingqu.click();
            }

            Match lingqu = region.exists(baseDir + "lingqu.png", 0.5);
            if (lingqu != null) {
                lingqu.click();
            }

            lingqu = region.exists(baseDir + "lingqu.png", 0.5);
            if (lingqu != null) {
                lingqu.click();
            }

            status.Done(Task.MEI_RI_JIANG_LI);
        }

        // 活跃度
        if (status.canDo(Task.HUO_YUE_DU)) {
            region.click(baseDir + "huoyuedu.png");
            Match lingqujiangli = region.exists(baseDir + "lingqujiangli.png", 1);

            while (lingqujiangli != null && isButtonEnable(lingqujiangli, 5, 5)) {
                lingqujiangli.click();
                lingqujiangli = region.exists(baseDir + "lingqujiangli.png", 1);
            }

            status.Done(Task.HUO_YUE_DU);
        }

        try {
            lvJingDuiHuan(region, status);
        } catch (IOException e) {
            log.error("{}", e);
        }

        if (status.canDo(Task.ZHEN_QING_HUI_KUI)) {
            Match zhenqinghuikui = region.exists(baseDir + "zhenqinghuikui.png");
            log.info("zhenqinghuikui: {}", zhenqinghuikui);
            if (zhenqinghuikui != null) {
                zhenqinghuikui.click();
                Thread.sleep(1000L);

                Match qing = region.exists(baseDir + "qing.png");
                if (qing != null) {
                    Match jinru = qing.right().exists(baseDir + "jinru.png");

                    if (jinru != null) {
                        jinru.click();
                        Thread.sleep(3000L);

                        int count = getNeedCount(region);
                        if (count > 2) {
                            region.saveScreenCapture(".", String.format("jianianhua-%d-%s-", count, status.getCurrentUser()));
                        }

                        int i = 0;
                        while (count > 5) {
                            Match kaishichoujiang = region.exists(baseDir + "kaishichoujiang.png");
                            if (kaishichoujiang == null) {
                                Match huoqujiangli = region.exists(baseDir + "huoqujiangli.png", 6);
                                if (huoqujiangli != null) {
                                    huoqujiangli.click();
                                }

                                break;
                            }

                            region.saveScreenCapture(".", String.format("jianianhua-%d-%d-%s-", count, i++, status.getCurrentUser()));
                            kaishichoujiang.click();
                            Thread.sleep(3000L);

                            Match fanpai = region.exists(baseDir + "fanpai.png");
                            region.saveScreenCapture(".", String.format("jianianhua-%d-%d-%s-", count, i++, status.getCurrentUser()));
                            if (fanpai == null) {
                                break;
                            }
                            ArrayList<Match> pais = Lists.newArrayList(region.findAll(baseDir + "fanpai.png"));
                            Random random = new Random();
                            Match pai = pais.get(random.nextInt(pais.size()));

                            region.saveScreenCapture(".", String.format("jianianhua-%d-%d-%s-", count, i++, status.getCurrentUser()));
                            pai.click();
                            region.saveScreenCapture(".", String.format("jianianhua-%d-%d-%s-", count, i++, status.getCurrentUser()));
                            Thread.sleep(300L);
                            region.saveScreenCapture(".", String.format("jianianhua-%d-%d-%s-", count, i++, status.getCurrentUser()));
                            Thread.sleep(3000L);
                            region.saveScreenCapture(".", String.format("jianianhua-%d-%d-%s-", count, i++, status.getCurrentUser()));
                            count = getNeedCount(region);
                        }


                        status.Done(Task.ZHEN_QING_HUI_KUI, Status.nextHour());

                        region.click(Common.CLOSE);
                        Thread.sleep(1000L);
                    }
                }
            }
        }

        region.click(Common.CLOSE);

        return true;
    }

    private void lvJingDuiHuan(Region region, Status status) throws InterruptedException, IOException {
        if (status.canDo(Task.LV_JING_DUI_HUAN)) {
            Match jiangliduihuan = region.exists(baseDir + "jiangliduihuan.png");
            if (jiangliduihuan != null) {
                jiangliduihuan.click();

                Match lvjingzhishi = region.exists(baseDir + "lvjingzhishi.png", 6);
                if (lvjingzhishi != null) {
                    lvjingzhishi.click();

                    Thread.sleep(3000L);

                    DirectoryStream<Path> paths = Files.newDirectoryStream(FileSystems.getDefault().getPath(baseDir), "lvjing_*.png");
                    for (Path path : paths) {
                        Pattern png = new Pattern(path.toFile().toString()).similar(0.85f);
                        Match p = region.exists(png, 0.3);

                        if (p != null) {
                            Match duihuan = p.below().exists(baseDir + "duihuan.png");
                            if (duihuan != null) {
                                duihuan.click();
                                Thread.sleep(1000L);
                            }
                        }
                    }

                    status.Done(Task.LV_JING_DUI_HUAN);
                }
            }
        }
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.HUO_YUE_DU, userName)
                && !status.canDo(Task.MEI_RI_JIANG_LI, userName)
                && !status.canDo(Task.LV_JING_DUI_HUAN, userName)
                && !status.canDo(Task.ZHEN_QING_HUI_KUI, userName)) {
            return false;
        }

        return true;
    }

    private int getNeedCount(Region region) throws FindFailed {
        int count = 0;
        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(FileSystems.getDefault().getPath(baseDir), "need_*.png");
            for (Path path : paths) {
                Pattern png = new Pattern(path.toFile().toString()).similar(0.85f);
                Match p = region.exists(png, 0.3);
                if (p == null) {
                    continue;
                } else {
                    count += Lists.newArrayList(region.findAll(png)).size();
                }
            }
        } catch (IOException e) {
            log.error("{}", e);
        }

        return count;
    }
}
