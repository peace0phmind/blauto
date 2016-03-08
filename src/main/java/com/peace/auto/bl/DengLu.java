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

    public void qiehuanzhanghao(Region region) throws FindFailed, InterruptedException {
        Region qq = Screen.create(0, 45, 480, 800);
        Match tianjiazhanghao = qq.exists(baseDir + "tianjiazhanghao.png", 10);
        if (tianjiazhanghao != null) {

            List<Match> qqs = new ArrayList<>();
            Iterator<Match> all = tianjiazhanghao.above().findAll(baseDir + "peace.png");
            while (all.hasNext()) {
                qqs.add(all.next());
            }

            Optional<Match> firstqq = qqs.stream().sorted((x, y) -> y.getY() - x.getY()).findFirst();
            if (firstqq.isPresent()) {
                firstqq.get().click();
            }
        }
    }

    public void Do(Region region) throws FindFailed, InterruptedException {
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

                    Match qiehuanzhanghao = region.exists(baseDir + "qiehuanzhanghao.png", 10);
                    if (qiehuanzhanghao != null) {
                        qiehuanzhanghao.click();

                        // 在qq中切换账号
                        qiehuanzhanghao(region);

                        Match jinrubuluo = region.exists(baseDir + "jinrubuluo.png", 30);
                        if (jinrubuluo != null) {
                            jinrubuluo.click();

                            Match dating = region.exists(Common.BASE_DIR + "building/building.png", 30);
                            if (dating != null) {
                                log.info("login ok");
                            } else {
                                System.exit(1);
                            }
                        }
                    }
                }
            }
        }
    }
}
