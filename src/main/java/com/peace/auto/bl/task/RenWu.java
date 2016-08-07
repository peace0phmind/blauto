package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class RenWu implements IDo {
    String baseDir = Common.BASE_DIR + "task/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (status.canDo(Task.YUE_KA)) {
            Match yueka = region.exists(baseDir + "yueka.png");
            if (yueka != null) {
                yueka.click();
                Thread.sleep(1000L);

                region.click(baseDir + "lingquyueka.png");

                Thread.sleep(2000L);

                Match close = region.exists(Common.CLOSE, 20);
                if (close != null) {
                    close.click();
                }

                status.Done(Task.YUE_KA);
            }
        }

        // 宝箱
        if (status.canDo(Task.LIN_QU_BAO_XIANG)) {
            Match baoxiang = region.exists(new Pattern(baseDir + "baoxiang.png").similar(0.95f));
            if (baoxiang != null) {
                baoxiang.click();
                Thread.sleep(3000L);

                Match linqubaoixang = region.exists(baseDir + "linqubaoxiang.png");
                if (linqubaoixang != null) {
                    linqubaoixang.click();
                    Thread.sleep(3000L);
                }

                status.Done(Task.LIN_QU_BAO_XIANG);
            }
        }

        // 收集金币
        if (status.canDo(Task.SHOU_JI_JIN_BI)) {
            // 点击收起对话框
            Match duihua = region.exists(Common.BASE_DIR + "guanbiduihua.png");
            if (duihua != null && duihua.getScore() > 0.95) {
                duihua.click();
                log.info("Close dialog!");
                Thread.sleep(1000L);
            }

            Match jinbi = region.exists(baseDir + "jinbi.png", 0.5);
            if (jinbi != null) {
                jinbi.click();
                status.Done(Task.SHOU_JI_JIN_BI);
            } else {
                log.info("No jinbi.");
                status.Done(Task.SHOU_JI_JIN_BI, Status.nextCheck());
            }
        }

        // 收集礼包
        if (status.canDo(Task.LI_BAO)) {
            Match libao = region.exists(baseDir + "libao.png", 0.5);
            if (libao != null && libao.getScore() > 0.95) {
                libao.click();

                Match lingqu = region.exists(baseDir + "lingqu.png", 10);
                if (lingqu != null) {
                    Thread.sleep(2000L);
                    region.click(baseDir + "lingqu.png");
                    status.Done(Task.LI_BAO);
                }

                Match nolibao = region.exists(baseDir + "nolibao.png", 0.5);
                if (nolibao != null) {
                    region.click(Common.QUE_DING);
                }
            }
        }

        // 领取任务
        if (status.canDo(Task.LIN_QU_REN_WU)) {
            Match renwu = region.exists(baseDir + "renwu.png", 0.5);
            log.info("{}", renwu);
            if (renwu != null) {
                renwu.click();
                Thread.sleep(2000L);

                Pattern renwuPattern = new Pattern(baseDir + "renwulingqu.png").similar(0.95f);

                try (DirectoryStream<Path> paths = Files.newDirectoryStream(FileSystems.getDefault().getPath(baseDir), "renwu_*.png")) {
                    for (Path path : paths) {
                        Match p = region.exists(new Pattern(path.toFile().toString()).similar(0.95f));
                        if (p == null) {
                            continue;
                        }

                        Match renwulingqu = p.right().exists(renwuPattern, 3);
                        if (renwulingqu != null) {
                            renwulingqu.click();
                            Thread.sleep(1000L);

                            Match lingqu = region.exists(baseDir + "lingqu.png", 0.5);
                            if (lingqu != null) {
                                lingqu.click();
                                Thread.sleep(3000L);
                            }
                        }

                        status.Done(Task.LIN_QU_REN_WU);
                    }
                } catch (IOException e) {
                    log.error("{}", e);
                }

                if (region.exists(renwuPattern) != null && status.canDo(Task.LIN_QU_REN_WU_UNKNOWN)) {
                    region.saveScreenCapture(".", "unknown-renwu-" + status.getCurrentUser());
                    status.Done(Task.LIN_QU_REN_WU_UNKNOWN, Status.nextDayCheck());
                }

                try {
                    region.click(Common.CLOSE);
                } catch (Exception e) {
                    log.error("{}", e);
                    renwu.saveScreenCapture(".", "renwu");
                    region.saveScreenCapture(".", "renwu-all");
                }
            }
        }

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.YUE_KA, userName)
                && !status.canDo(Task.LIN_QU_BAO_XIANG, userName)
                && !status.canDo(Task.LI_BAO, userName)
                && !status.canDo(Task.SHOU_JI_JIN_BI, userName)
                && !status.canDo(Task.LIN_QU_REN_WU, userName)) {
            return false;
        }

        return true;
    }
}