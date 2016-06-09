package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;

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

            Match qiuzhangxinxi = region.exists(Common.BASE_DIR + "qiuzhangxinxi.png", 6);
            if (qiuzhangxinxi == null) {
                touxiang.click();
                qiuzhangxinxi = region.exists(Common.BASE_DIR + "qiuzhangxinxi.png", 6);
            }

            if (qiuzhangxinxi == null) {
                log.error("get user info error.");
                return;
            }

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
        } else {
            QiDong(region, status, status.getWantUser());
        }
    }

    private boolean jinrubuluo(Region region, Status status) throws FindFailed, InterruptedException {
        Match jinrubuluo = region.exists(baseDir + "jinrubuluo.png", 6);
        int i = 0;
        while (jinrubuluo == null) {
            i++;
            Match close = region.exists(Common.CLOSE);
            if (close != null) {
                close.click();
                jinrubuluo = region.exists(baseDir + "jinrubuluo.png", 6);
            }

            if (i > 10) {
                return chongxindenglu(region, status);
            }
        }

        if (jinrubuluo != null) {
            jinrubuluo.click();
            log.info("click jinrubuluo");

            Match dating = region.exists(Common.BASE_DIR + "building/building.png", 30);
            if (dating != null) {
                checkUser(region, status);
                log.info("login ok");
                return true;
            }
        }

        log.info("jinrubuluo is null");

        return false;
    }

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        return Done(region, status, status.getNextLoginName());
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return true;
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

    private boolean QiDong(Region region, Status status, String loginName) throws FindFailed, InterruptedException {
        status.setWantUser(loginName);

        Match bl = region.exists(Common.BASE_DIR + "bl.png", 10);
        if (bl != null) {
            bl.click();

            Thread.sleep(10 * 1000L);

            Match close = region.exists(Common.CLOSE);
            while (close != null) {
                close.click();
                close = region.exists(Common.CLOSE);
            }

            return chongxindenglu(region, status);
        }

        return false;
    }

    private boolean chongxindenglu(Region region, Status status) throws InterruptedException, FindFailed {
        Match qqhaoyouwan = region.exists(baseDir + "qqhaoyouwan.png", 10);
        if (qqhaoyouwan != null) {
            qqhaoyouwan.click();

            Thread.sleep(3000L);

            qqhaoyouwan = region.exists(baseDir + "qqhaoyouwan.png", 3);
            if (qqhaoyouwan != null) {
                qqhaoyouwan.click();
            }

            String loginName = status.getWantUser();

            Match qiehuanzhanghao = region.exists(baseDir + "qiehuanzhanghao.png", 10);
            if (qiehuanzhanghao != null) {
                qiehuanzhanghao.click();

                Thread.sleep(5000L);
                // 在qq中切换账号
                Match inqiehuanzhanghao = region.exists(baseDir + "inqiehuanzhanghao.png", 20);
                if (inqiehuanzhanghao != null) {
                    Thread.sleep(3000L);

                    List<Match> qqs = Lists.newArrayList(region.findAll(new Pattern(baseDir + "peace.png").similar(similar)));

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
        }

        return false;
    }
}
