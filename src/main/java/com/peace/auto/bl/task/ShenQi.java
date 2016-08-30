package com.peace.auto.bl.task;

import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mind on 4/30/16.
 */
@Slf4j
public class ShenQi implements IDo {

    private static final String baseDir = Common.BASE_DIR + "shenqi/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        Match bashou = region.exists(new Pattern(baseDir + "shengyu.png").similar(0.9f), 15);
        if (bashou != null) {
            move(bashou, bashou.getCenter().above(300), 1000);

            Match shenqi = region.exists(baseDir + "shenqi.png");
            if (shenqi != null) {
                shenqi.click();
                Thread.sleep(6000L);

                // 神器召唤
                if (status.canDo(Task.SHEN_QI)) {
                    Match zhaohuan = region.exists(baseDir + "zhaohuan.png");
                    if (zhaohuan != null) {
                        zhaohuan.click();
                        Thread.sleep(1000L);

                        Match mianfeizhaohuan = region.exists(baseDir + "mianfeizhaohuan.png");
                        if (mianfeizhaohuan != null) {
                            Thread.sleep(1000L);
                            mianfeizhaohuan.click();
                            status.Done(Task.SHEN_QI);

                            Thread.sleep(500L);
                            region.click(Common.QUE_DING);
                        }
                    }
                    Thread.sleep(500L);
                }

                // 神像升级
                if (status.canDo(Task.SHEN_XIANG_SHENG_JI)) {
                    Match shengji = region.exists(baseDir + "shengji.png");
                    if (shengji != null) {
                        shengji.click();

                        boolean bjb = false;
                        List<LocalDateTime> times = new ArrayList<>();
                        List<String> strings = Arrays.asList("jianling.png", "yongling.png", "moling.png", "paoling.png");
                        int i1 = LocalDateTime.now().getDayOfYear() % 4;
                        for (int i = 0; i < 4; i++) {
                            String shenxiang = strings.get((i + i1) % 4);
                            Match sx = region.exists(baseDir + shenxiang);
                            if (sx != null) {
                                sx.click();
                                Thread.sleep(500L);

                                // 免费倒计时不存在,则进行免费祭拜
                                Match daojishi = region.exists(baseDir + "mianfeidaojishi.png");
                                if (daojishi == null) {
                                    Match jb = region.exists(baseDir + "mianfeijibai.png");
                                    if (jb != null) {
                                        jb.click();
                                        log.info("祭拜 : {}", shenxiang);
                                        bjb = true;
                                    }

                                    Match gp = region.exists(baseDir + "gongpinjibai.png");
                                    if (gp != null) {
                                        for (int i = 0; i < 3; i++) {
                                            gp.click();
                                            Match cishu = region.exists(baseDir + "dadaomeiricishu.png");
                                            if (cishu != null) {
                                                region.click(Common.QUE_DING);
                                                break;
                                            }

                                            Match chongzhi = region.exists(baseDir + "chongzhi.png");
                                            if (chongzhi != null) {
                                                clickInside(region, Common.CLOSE);
                                                Thread.sleep(3000l);
                                                break;
                                            }
                                        }
                                    }
                                } else {
////                                    String sTime = getWord(daojishi.right(70), "1234567890:");
                                    String sTime = getTime(daojishi.right(70), Arrays.asList(new Color(52, 190, 30),
                                            new Color(52, 206, 29), new Color(51, 159, 31)));
                                    log.info("sTime: {}", sTime);
                                    String[] split = sTime.split(":");
                                    if (split.length == 3) {
                                        try {
                                            times.add(LocalDateTime.now().plusHours(Integer.parseInt(split[0].trim()))
                                                    .plusMinutes(Integer.parseInt(split[1].trim()))
                                                    .plusSeconds(Integer.parseInt(split[2].trim())));
                                        } catch (Exception e) {
                                            log.info("{}", e);
                                        }
                                    }
                                }
                            }
                        }

                        if (bjb) {
                            status.Done(Task.SHEN_XIANG_SHENG_JI);
                        } else {
//                            if (times.size() > 0) {
//                                LocalDateTime largerTime = times.stream().sorted((x, y) -> y.compareTo(x)).findFirst().get();
//                                log.info("Times: {}, largerTime: {}", times, largerTime);
//                                status.Done(Task.SHEN_XIANG_SHENG_JI, largerTime);
//                            }
                            status.Done(Task.SHEN_XIANG_SHENG_JI, Status.nextCheck());
                        }
                    }
                }

                Thread.sleep(500L);
                region.click(Common.CLOSE);
                Thread.sleep(500L);
            }

            for (int i = 0; i < 6; i++) {
                bashou = region.exists(new Pattern(baseDir + "shengyu.png").similar(0.9f), 15);
                if (bashou != null) {
                    move(bashou, bashou.getCenter().below(300), 1000);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.SHEN_QI, userName)
                && !status.canDo(Task.SHEN_XIANG_SHENG_JI, userName)) {
            return false;
        }

        return true;
    }
}
