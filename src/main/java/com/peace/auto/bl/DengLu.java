package com.peace.auto.bl;

import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by mind on 3/7/16.
 */
@Slf4j
public class DengLu implements IDo {
    String baseDir = Common.BASE_DIR + "denglu/";

    private float similar = 0.5f;

    private boolean qiehuanzhanghao(Region region, String loginName) throws FindFailed, InterruptedException {
        Match qiehuanzhanghao = region.exists(baseDir + "qiehuanzhanghao.png", 10);
        if (qiehuanzhanghao != null) {
            qiehuanzhanghao.click();

            Thread.sleep(5000L);
            // 在qq中切换账号
            Match tianjiazhanghao = region.exists(baseDir + "tianjiazhanghao.png", 20);
            if (tianjiazhanghao != null) {
                Thread.sleep(3000L);

                List<Match> qqs = Lists.newArrayList(tianjiazhanghao.above().findAll(new Pattern(baseDir + "peace.png").similar(similar)));

                String lastChar = loginName.substring(loginName.length() - 1);

                Optional<Match> firstqq = qqs.stream().filter(x -> {
                    String text = x.text();
//                    log.info("{}, {}", text, text.length());
                    return lastChar.equals(text.substring(text.length() - 1));
                }).findFirst();

                // 点击账号
                if (firstqq.isPresent()) {
                    Match qq = firstqq.get();
                    log.info("登录: {}", qq.text());
                    qq.click();

                    // 进入部落
                    return jinrubuluo(region, loginName);
                } else {
                    region.click(baseDir + "qqdenglu.png");
                    Thread.sleep(1000L);

                    region.click(baseDir + "denglu.png");

                    // 进入部落
                    return jinrubuluo(region, loginName);
                }
            }
        }

        return false;
    }

    private boolean jinrubuluo(Region region, String loginName) throws FindFailed, InterruptedException {
        if (!closeGongGaoLan(region)) {
            if (chongxindenglu(region, loginName)) {
                return true;
            }
        }

        Match jinrubuluo = region.exists(baseDir + "jinrubuluo.png", 60);
        if (jinrubuluo != null) {
            jinrubuluo.click();

            Match dating = region.exists(Common.BASE_DIR + "building/building.png", 30);
            if (dating != null) {
                log.info("login ok");
                return true;
            }
        }

        return false;
    }

    private boolean closeGongGaoLan(Region region) throws FindFailed, InterruptedException {
        Match gonggaolan = region.exists(baseDir + "gonggaolan.png", 5);
        if (gonggaolan != null && gonggaolan.getScore() > 0.95) {
            Thread.sleep(5000L);
            Match close = region.exists(Common.CLOSE);
            if (close == null) {
                return false;
            }

            for (int i = 0; i < 10; i++) {
                close.click();
                gonggaolan = region.exists(baseDir + "gonggaolan.png", 1);
                if (gonggaolan == null) {
                    return true;
                }
            }
        }

        return false;
    }


    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        return Done(region, status, status.getNextLoginName());
    }

    public boolean Done(Region region, Status status, String loginName) throws FindFailed, InterruptedException {
        log.info("reboot to user: {}", loginName);

        region.click(Common.MENU);

        status.setWantUser(loginName);

        Match peizhi = region.exists(baseDir + "peizhi.png");
        if (peizhi != null) {
            peizhi.click();

            Match tuichudenglu = region.exists(baseDir + "tuichudenglu.png");
            if (tuichudenglu != null) {
                tuichudenglu.click();

                return chongxindenglu(region, loginName);
            }
        }

        return false;
    }

    public boolean QiDong(Region region, Status status) throws FindFailed, InterruptedException {
        return QiDong(region, status, status.getNextLoginName());
    }

    public boolean QiDong(Region region, Status status, String loginName) throws FindFailed, InterruptedException {
        status.setWantUser(loginName);

        Match bl = region.exists(Common.BASE_DIR + "bl.png", 10);
        if (bl != null) {
            bl.click();

            Thread.sleep(10 * 1000L);

            return jinrubuluo(region, loginName);
        }

        return false;
    }

    private boolean chongxindenglu(Region region, String loginName) throws InterruptedException, FindFailed {
        Match qqhaoyouwan = region.exists(baseDir + "qqhaoyouwan.png", 5);
        if (qqhaoyouwan != null) {
            qqhaoyouwan.click();

            Thread.sleep(6000L);

            return qiehuanzhanghao(region, loginName);
        }

        return false;
    }
}
