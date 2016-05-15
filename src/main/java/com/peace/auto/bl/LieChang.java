package com.peace.auto.bl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by mind on 3/7/16.
 */
@Slf4j
public class LieChang implements IDo {

    String baseDir = Common.BASE_DIR + "liechang/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        if (!status.canDo(Task.LIE_CHANG_ZHENG_SHOU) && !status.canDo(Task.LIE_CHANG_DA_GUAI)) {
            return false;
        }

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

            // 打怪
            if (status.canDo(Task.LIE_CHANG_DA_GUAI)) {

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

                        Match baopokuangren = region.exists(baseDir + "baopokuangren.png");
                        if (baopokuangren != null && baopokuangren.getScore() > 0.95) {
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
}
