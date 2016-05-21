package com.peace.auto.bl;

import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.Lists;
import com.peace.sikuli.monkey.AndroidRegion;
import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.awt.*;
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

    public void checkUser(Region region, Status status, String userName) throws FindFailed, InterruptedException {
        status.setWantUser(userName);
        checkUser(region, status);
    }

    public void checkUser(Region region, Status status) throws FindFailed, InterruptedException {
        Match touxiang = region.exists(Common.BASE_DIR + "touxiang.png");
        if (touxiang != null) {
            touxiang.click();

            Match qiuzhangxinxi = region.exists(Common.BASE_DIR + "qiuzhangxinxi.png");
            if (qiuzhangxinxi == null) {
                touxiang.click();
                qiuzhangxinxi = region.exists(Common.BASE_DIR + "qiuzhangxinxi.png");
            }

            if (qiuzhangxinxi == null) {
                return;
            }

            ;
//            ScreenImage simg = region.getScreen().capture();
//            TextRecognizer tr = TextRecognizer.getInstance();
            int num = getNumber(newRegion(region, new Rectangle(134, 382, 8, 14)));

            region.click(Common.CLOSE);
            Thread.sleep(1000L);

            if (!status.changeUser(num)) {
                new DengLu().Done(region, status, status.getWantUser());
                checkUser(region, status);
            } else {
                log.info("current user: {}, want user: {}, num: {}", status.getCurrentUser(), status.getWantUser(), num);
            }
        }
    }

    private boolean qiehuanzhanghao(Region region, Status status) throws FindFailed, InterruptedException {
        String loginName = status.getWantUser();

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
                    String text = getWord(x);
//                    log.info("{}, {}", text, text.length());
                    return lastChar.equals(text.substring(text.length() - 1));
                }).findFirst();

                // 点击账号
                if (firstqq.isPresent()) {
                    Match qq = firstqq.get();
                    log.info("登录: {}", getWord(qq));
                    qq.click();

                    // 进入部落
                    return jinrubuluo(region, status);
                } else {
                    region.click(baseDir + "qqdenglu.png");
                    Thread.sleep(1000L);

                    region.click(baseDir + "denglu.png");

                    // 进入部落
                    return jinrubuluo(region, status);
                }
            }
        }

        return false;
    }

    private boolean jinrubuluo(Region region, Status status) throws FindFailed, InterruptedException {
        if (!closeGongGaoLan(region)) {
            if (chongxindenglu(region, status)) {
                return true;
            }
        }

        Match jinrubuluo = region.exists(baseDir + "jinrubuluo.png", 60);
        if (jinrubuluo != null) {
            Thread.sleep(6000L);
            jinrubuluo.click();
            log.info("click jinrubuluo");

            Match dating = region.exists(Common.BASE_DIR + "building/building.png", 30);
            if (dating != null) {
                checkUser(region, status);
                log.info("login ok");
                return true;
            }
        }

        return false;
    }

    private boolean closeGongGaoLan(Region region) throws FindFailed, InterruptedException {
        Match gonggaolan = region.exists(baseDir + "gonggaolan.png", 10);
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
        status.setWantUser(loginName);
        log.info("reboot to user: {}", loginName);

        region.click(Common.MENU);

        Match peizhi = region.exists(baseDir + "peizhi.png");
        if (peizhi != null) {
            peizhi.click();

            Match tuichudenglu = region.exists(baseDir + "tuichudenglu.png");
            if (tuichudenglu != null) {
                tuichudenglu.click();

                return chongxindenglu(region, status);
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

            return jinrubuluo(region, status);
        }

        return false;
    }

    private boolean chongxindenglu(Region region, Status status) throws InterruptedException, FindFailed {
        Match qqhaoyouwan = region.exists(baseDir + "qqhaoyouwan.png", 5);
        if (qqhaoyouwan != null) {
            qqhaoyouwan.click();

            Thread.sleep(6000L);

            return qiehuanzhanghao(region, status);
        }

        return false;
    }
}
