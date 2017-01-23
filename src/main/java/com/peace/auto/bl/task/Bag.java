package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mind on 15/11/2016.
 */
@Slf4j
public class Bag implements IDo {
    String baseDir = Common.BASE_DIR + "bag/";

    @Override
    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (status.isPeace()) {
            return false;
        }

        Match menu = region.exists(Common.MENU);
        if (menu != null) {
            menu.click();

            Match bag = region.exists(baseDir + "bag.png");
            if (bag != null) {
                bag.click();

                Match zidongzhengli = region.exists(baseDir + "zidongzhengli.png", 6);
                if (zidongzhengli != null) {
                    zidongzhengli.click();

                    for (int i = 0; i < 8; i++) {
                        try (DirectoryStream<Path> paths = Files.newDirectoryStream(FileSystems.getDefault().getPath(baseDir), "yong_*.png")) {
                            for (Path path : paths) {
                                Match p = region.exists(new Pattern(path.toFile().toString()).similar(0.95f), 0.1);
                                if (p == null) {
                                    continue;
                                }

                                for (int j = 0; j < 6; j++) {
                                    if (p == null) {
                                        break;
                                    }
                                    p.click();

                                    Match shiyong = region.exists(baseDir + "shiyong.png");
                                    if (shiyong != null) {
                                        shiyong.click();
                                        Match queding = region.exists(Common.QUE_DING);
                                        queding.click();
                                        Thread.sleep(3000L);
                                    }

                                    p = region.exists(new Pattern(path.toFile().toString()).similar(0.95f), 0.1);
                                }
                            }
                        } catch (IOException e) {
                            log.error("{}", e);
                        }

                        try (DirectoryStream<Path> paths = Files.newDirectoryStream(FileSystems.getDefault().getPath(baseDir), "mai_*.png")) {
                            for (Path path : paths) {
                                Match p = region.exists(new Pattern(path.toFile().toString()).similar(0.95f), 0.1);
                                if (p == null) {
                                    continue;
                                }

                                p.click();

                                Thread.sleep(6000L);

                                Match maichu = region.exists(baseDir + "maichu.png");
                                if (maichu != null) {
                                    maichu.click();
                                    Match queding = region.exists(Common.QUE_DING);
                                    queding.click();
                                    Thread.sleep(4000L);
                                }
                            }
                        } catch (IOException e) {
                            log.error("{}", e);
                        }

                        Match close = region.exists(Common.CLOSE);
                        Region left = close.below(200);
                        move(left, new Location(0, left.y), 1);
                    }

                    region.click(Common.CLOSE);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (status.isPeace()) {
            return false;
        }
        
        return true;
    }
}
