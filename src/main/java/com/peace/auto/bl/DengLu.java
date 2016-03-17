package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by mind on 3/7/16.
 */
@Slf4j
public class DengLu implements IDo {
    String baseDir = Common.BASE_DIR + "denglu/";

    public boolean qiehuanzhanghao(Region region) throws FindFailed, InterruptedException {
        Match qiehuanzhanghao = region.exists(baseDir + "qiehuanzhanghao.png", 10);
        if (qiehuanzhanghao != null) {
            qiehuanzhanghao.click();

            Thread.sleep(5000L);
            // 在qq中切换账号
            Match tianjiazhanghao = region.exists(baseDir + "tianjiazhanghao.png", 20);
            if (tianjiazhanghao != null) {
                Thread.sleep(3000L);

                List<Match> qqs = new ArrayList<>();
                Iterator<Match> all = tianjiazhanghao.above().findAll(baseDir + "peace.png");
                while (all.hasNext()) {
                    qqs.add(all.next());
                }

                // 点击账号
                Optional<Match> firstqq = qqs.stream().sorted((x, y) -> y.getY() - x.getY()).findFirst();
                if (firstqq.isPresent()) {
                    firstqq.get().click();

                    // 进入部落
                    Match jinrubuluo = region.exists(baseDir + "jinrubuluo.png", 30);
                    if (jinrubuluo != null) {
                        jinrubuluo.click();

                        Match dating = region.exists(Common.BASE_DIR + "building/building.png", 30);
                        if (dating != null) {
                            log.info("login ok");
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Done(Region region) throws FindFailed, InterruptedException {
        region.click(Common.MENU);

        Match peizhi = region.exists(baseDir + "peizhi.png");
        if (peizhi != null) {
            peizhi.click();

            Match tuichudenglu = region.exists(baseDir + "tuichudenglu.png");
            if (tuichudenglu != null) {
                tuichudenglu.click();

                Match qqhaoyouwan = region.exists(baseDir + "qqhaoyouwan.png");
                if (qqhaoyouwan != null) {
                    qqhaoyouwan.click();

                    Thread.sleep(6000L);

                    for (int i = 0; i < 3; i++) {
                        if (qiehuanzhanghao(region)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
