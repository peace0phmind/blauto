package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.peace.auto.bl.common.Devices.status;

/**
 * Created by mind on 3/7/16.
 */
@Slf4j
public class DengLu implements IDo {
    String baseDir = Common.BASE_DIR + "denglu/";

    private float similar = 0.5f;

    private Properties properties = new Properties();

    public DengLu() {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("password.properties"));
        } catch (IOException e) {
            log.error("Load password error. {}", e);
        }
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public void checkAndChangeUser(Region region, Status status, String userName) throws FindFailed, InterruptedException {
        log.debug("userName: {}", userName);
        status.setWantUser(userName);

        if (checkUser(region, status)) {
            return;
        } else {
            for (int i = 0; i < 10; i++) {
                boolean bSuccessToDengLu = false;
                Match touxiang = region.exists(Common.BASE_DIR + "touxiang.png");
                if (touxiang != null) {
                    // 在游戏主界面,重新登录
                    bSuccessToDengLu = tuiChu(region);
                } else {
                    bSuccessToDengLu = qiDong(region);
                }

                if (bSuccessToDengLu) {
                    if (dengLu(region, status)) {
                        // 检查是否登录成功?成功则返回
                        Match dating = region.exists(Common.BASE_DIR + "building/building.png", 30);
                        Thread.sleep(6000l);
                        if (dating != null && checkUser(region, status)) {
                            log.info("login ok");
                            return;
                        }
                    }
                }

                // 登录失败,则退出软件重新登录
                doRobot(region, (robot) -> {
                    try {
                        robot.pressBack();
                        Thread.sleep(1000L);
                        region.saveScreenCapture(".", "tuichu");

                        Match querentuichu = region.exists(baseDir + "quedingtuichu.png");
                        if (querentuichu == null) {
                            robot.pressBack();
                            Thread.sleep(1000L);
                            region.saveScreenCapture(".", "tuichu");
                        }

                        querentuichu = region.exists(baseDir + "quedingtuichu.png");
                        if (querentuichu == null) {
                            robot.pressBack();
                            Thread.sleep(1000L);
                            region.saveScreenCapture(".", "tuichu");
                        }

                        region.click(baseDir + "queren.png");
                        Thread.sleep(5000L);
                    } catch (Exception e) {
                        log.error("{}", e);
                    }
                });

            }

            // 多次都没有成功,退出系统
            log.error("login error, want user: {}, current user:{}", status.getWantUser(), status.getCurrentUser());
            System.exit(-1);
        }
    }

    private boolean checkUser(Region region, Status status) throws FindFailed, InterruptedException {
//        newRegion(region, new Rectangle(134, 385, 8, 14)).saveScreenCapture(".", "ttt");

        Match touxiang = region.exists(Common.BASE_DIR + "touxiang.png");
        if (touxiang != null) {
            touxiang.click();
            Thread.sleep(1000L);

            Match qiuzhangxinxi = region.exists(Common.BASE_DIR + "qiuzhangxinxi.png", 6);
            log.info("{}", qiuzhangxinxi);
            if (qiuzhangxinxi == null) {
                Thread.sleep(6000L);
                touxiang.click();
                qiuzhangxinxi = region.exists(Common.BASE_DIR + "qiuzhangxinxi.png", 6);
            }

            if (qiuzhangxinxi == null) {
                log.error("get user info error.");
                return false;
            }

//            ScreenImage simg = region.getScreen().capture();
//            TextRecognizer tr = TextRecognizer.getInstance();
            int num = getNumber(newRegion(region, new Rectangle(134, 385, 8, 14)));

            region.click(Common.CLOSE);
            Thread.sleep(1000L);

            return status.changeUser(num);
        }

        return false;
    }

    private boolean jinrubuluo(Region region, Status status) throws FindFailed, InterruptedException {
        Match tianjiazhanghao = region.exists(new Pattern(baseDir + "tianjiazhanghao.png").similar(0.95f), 6);
        log.debug("{}", tianjiazhanghao);
        if (tianjiazhanghao != null && tianjiazhanghao.getScore() > 0.95f) {

            Match username = region.exists(baseDir + "username.png");
            Match password = region.exists(baseDir + "password.png");
            log.debug("{}", username);
            log.debug("{}", password);

            if (username != null) {
                username.type(properties.getProperty(String.format("%s.username", status.getWantUser())));
                Thread.sleep(3000L);
            }

            if (password != null) {
                password.click();
                Thread.sleep(1000L);

                Match cleanPassword = region.exists(baseDir + "cleanpassword.png");
                if (cleanPassword != null) {
                    cleanPassword.click();
                }

                password.type(properties.getProperty(String.format("%s.password", status.getWantUser())));
                Thread.sleep(2000L);
            }

            region.click(baseDir + "tianjiazhanghaodenglu.png");
            Thread.sleep(3000L);
        }

        Match jinrubuluo = region.exists(baseDir + "jinrubuluo.png", 30);
        int i = 0;
        while (jinrubuluo == null) {
            i++;
            Match close = region.exists(Common.CLOSE);
            if (close != null) {
                close.click();
                jinrubuluo = region.exists(baseDir + "jinrubuluo.png", 6);
            }

            if (i > 10) {
                return false;
            }
        }

        if (jinrubuluo != null) {
            jinrubuluo.click();
            log.info("click jinrubuluo");
            return true;
        }

        log.info("jinrubuluo is null");

        return false;
    }

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        throw new UnsupportedOperationException();
    }

    public boolean CanDo(Status status, String userName) {
        throw new UnsupportedOperationException();
    }

    private boolean tuiChu(Region region) throws FindFailed, InterruptedException {
        region.click(Common.MENU);

        Match peizhi = region.exists(baseDir + "peizhi.png");
        log.debug("{}", peizhi);
        if (peizhi != null) {
            peizhi.click();

            Match tuichudenglu = region.exists(baseDir + "tuichudenglu.png");
            log.debug("{}", tuichudenglu);
            if (tuichudenglu != null) {
                tuichudenglu.click();
                Thread.sleep(1000L);
                return true;
            }
        }

        return false;
    }

    private boolean qiDong(Region region) throws FindFailed, InterruptedException {
        Match bl = region.exists(Common.BASE_DIR + "bl.png", 10);
        log.debug("{}", bl);
        if (bl != null) {
            bl.click();

            Thread.sleep(10 * 1000L);

            Match close = region.exists(Common.CLOSE, 10);
            while (close != null) {
                log.debug("{}", close);
                close.click();
                close = region.exists(Common.CLOSE);
            }

            return true;
        }

        return false;
    }

    private boolean dengLu(Region region, Status status) throws InterruptedException, FindFailed {
        Match qqhaoyouwan = region.exists(baseDir + "qqhaoyouwan.png", 10);
        log.debug("{}", qqhaoyouwan);
        if (qqhaoyouwan != null) {
            qqhaoyouwan.click();

            Thread.sleep(3000L);

            qqhaoyouwan = region.exists(baseDir + "qqhaoyouwan.png", 3);
            log.debug("{}", qqhaoyouwan);
            if (qqhaoyouwan != null) {
                qqhaoyouwan.click();
            }

            // 如果默认登录,则跳转到切换账号
            Match tianjiazhanghao = region.exists(new Pattern(baseDir + "tianjiazhanghao.png").similar(0.95f));
            log.debug("{}", tianjiazhanghao);
            if (tianjiazhanghao != null) {
                region.click(baseDir + "qqdenglu.png");
                Thread.sleep(1000L);
            }

            isNetworkOk(region);

            String loginName = status.getWantUser();

            Match qiehuanzhanghao = region.exists(baseDir + "qiehuanzhanghao.png", 10);
            log.debug("{}", qiehuanzhanghao);
            if (qiehuanzhanghao != null) {
                qiehuanzhanghao.click();
                Thread.sleep(5000L);
                // 在qq中切换账号
                Match inqiehuanzhanghao = region.exists(baseDir + "inqiehuanzhanghao.png", 20);
                log.debug("{}", inqiehuanzhanghao);
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

                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(3000L);
                            if (isNetworkOk(region)) { // 进入部落
                                return jinrubuluo(region, status);
                            }
                            Thread.sleep(1000L);
                            qq.click();
                        }
                    } else {
                        region.click(baseDir + "qqdenglu.png");
                        Thread.sleep(1000L);

                        region.click(baseDir + "denglu.png");

                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(3000L);
                            if (isNetworkOk(region)) { // 进入部落
                                return jinrubuluo(region, status);
                            }
                            Thread.sleep(1000L);
                            region.click(baseDir + "denglu.png");
                        }
                    }
                }

                return false;
            }
        }

        return false;
    }

    private boolean isNetworkOk(Region region) throws FindFailed, InterruptedException {
        Match zhengzaihuoququanxian = region.exists(new Pattern(baseDir + "zhengzaihuoququanxian.png").similar(0.9f), 1);
        log.debug("{}", zhengzaihuoququanxian);
        if (zhengzaihuoququanxian != null) {
            doRobot(region, (robot) -> {
                log.info("use robot press back for zhengzaihuoququanxian");
                robot.pressBack();
            });

            return false;
        }

        Match zhengzaidenglu = region.exists(new Pattern(baseDir + "zhengzaidenglu.png").similar(0.9f), 1);
        log.debug("{}", zhengzaidenglu);
        if (zhengzaidenglu != null) {
            doRobot(region, (robot) -> {
                log.info("use robot press back for zhengzaidenglu");
                robot.pressBack();
            });

            return false;
        }

        Match wangluoyichang = region.exists(baseDir + "wangluoyichangqingshaohouchongshi.png", 1);
        log.debug("{}", wangluoyichang);
        if (wangluoyichang != null) {
            region.click(baseDir + "wangluoyichangqueding.png");

            return false;
        }

        return true;
    }
}
