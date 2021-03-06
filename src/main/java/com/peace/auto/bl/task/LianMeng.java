package com.peace.auto.bl.task;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.peace.auto.bl.Status;
import com.peace.auto.bl.Task;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by mind on 3/4/16.
 */
@Slf4j
public class LianMeng implements IDo {
    private static Random random = new Random();
    String baseDir = Common.BASE_DIR + "lianmeng/";

    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        region.click(Common.MENU);

        Match lianmeng = region.exists(baseDir + "lianmeng.png", 3);
        if (lianmeng != null) {
            lianmeng.click();

            // 供奉
            if (status.canDo(Task.LIAN_MENG_GONG_FENG)) {
                Match lianmenggongfeng = region.exists(baseDir + "lianmenggongfeng.png", 30);
                if (lianmenggongfeng != null) {
                    lianmenggongfeng.click();

                    Match shuijin = region.exists(baseDir + "shuijin.png", 5);
                    if (shuijin != null) {
                        Thread.sleep(1000L);
                        shuijin.below().click(baseDir + "gongfeng.png");
                        Thread.sleep(1000L);
                        region.click(Common.CLOSE);

                        status.Done(Task.LIAN_MENG_GONG_FENG);
                    }
                }
            }

            // 南蛮
            if (status.canDo(Task.LIAN_MENG_NAN_MAN)) {
                Match nanman = region.exists(baseDir + "nanman.png", 3);
                if (nanman != null) {
                    nanman.click();

                    Thread.sleep(3000L);

                    // 领取奖励
                    Match lingqujiangli = region.exists(baseDir + "lingqujiangli.png", 5);
                    if (lingqujiangli != null) {
                        lingqujiangli.click();
                        Thread.sleep(500L);
                    }

                    Match baoming = region.exists(baseDir + "nanmanbaoming.png", 5);
                    if (baoming != null) {
                        baoming.click();
                    }

                    Thread.sleep(2000L);
                    region.click(Common.CLOSE);

                    status.Done(Task.LIAN_MENG_NAN_MAN);
                }
            }

            // 南蛮开始
            if (status.canDo(Task.LIAN_MENG_NAN_MAN_KAI_SHI) && LocalTime.now().isAfter(LocalTime.of(20, 0))) {
                Match nanman = region.exists(baseDir + "nanman.png", 3);
                if (nanman != null) {
                    nanman.click();

                    Thread.sleep(3000L);

                    Match kaishizhandou = region.exists(baseDir + "kaishizhandou.png", 5);
                    if (kaishizhandou != null) {
                        kaishizhandou.click();
                    }

                    Thread.sleep(2000L);
                    region.click(Common.CLOSE);

                    status.Done(Task.LIAN_MENG_NAN_MAN_KAI_SHI);
                }
            }

            // 联盟战
            if (status.canDo(Task.LIAN_MENG_LIAN_MENG_ZHAN)) {
                Match lianmengzhan = region.exists(baseDir + "lianmengzhan.png");
                if (lianmengzhan != null) {
                    lianmengzhan.click();

                    // ok
                    Match inmengzhan = region.exists(baseDir + "inlianmengzhan.png", 20);
                    if (inmengzhan != null) {
                        // 领取奖励
                        Match mengzhanlingqujiangli = region.exists(baseDir + "mengzhanlingqujiangli.png");
                        if (mengzhanlingqujiangli != null) {
                            mengzhanlingqujiangli.click();
                            Thread.sleep(1000L);
                        }

                        Match jinruzhanchang = region.exists(baseDir + "jinruzhanchang.png");
                        if (jinruzhanchang != null) {
                            jinruzhanchang.click();

                            Match queding = region.exists(Common.QUE_DING);
                            if (queding != null) {
                                queding.click();
                                status.Done(Task.LIAN_MENG_LIAN_MENG_ZHAN);
                            } else {
                                // 联盟尚未报名
                                Match lianmengshangweibaom = region.exists(new Pattern(baseDir + "lianmengshangweibaoming.png").similar(0.9f));
                                if (lianmengshangweibaom != null) {
                                    status.Done(Task.LIAN_MENG_LIAN_MENG_ZHAN);
                                } else {
                                    // 尚未报名则进行报名
                                    Match shangweibaom = region.exists(baseDir + "shangweibaoming.png", 6);
                                    if (shangweibaom != null) {
                                        Match baomingcansai = region.exists(baseDir + "baomingcansai.png");
                                        if (baomingcansai != null) {
                                            Thread.sleep(1000L);
                                            baomingcansai.click();
                                            Thread.sleep(1000L);

                                            Iterator<Match> all = region.findAll(new Pattern(baseDir + "mengzhanbaomin.png").similar(0.95f));
                                            ArrayList<Match> allBaoMin = Lists.newArrayList(all);

                                            log.info("{}", allBaoMin);
                                            allBaoMin.get(random.nextInt(allBaoMin.size())).click();
                                            status.Done(Task.LIAN_MENG_LIAN_MENG_ZHAN);

                                            Thread.sleep(1000L);
                                            region.click(Common.CLOSE);
                                        } else {
                                            status.Done(Task.LIAN_MENG_LIAN_MENG_ZHAN);
                                        }
                                    }
                                }

                                // 已被淘汰
                                Match yibeitaotai = region.exists(baseDir + "yibeitaotai.png");
                                if (yibeitaotai != null) {
                                    status.Done(Task.LIAN_MENG_LIAN_MENG_ZHAN);
                                }

                                // 本届排名则进入获取声望奖励
                                Match benjiepaiming = region.exists(baseDir + "benjiepaihang.png");
                                if (benjiepaiming != null) {
                                    benjiepaiming.click();
                                    Thread.sleep(1000L);

                                    mengzhanlingqujiangli = region.exists(baseDir + "mengzhanlingqujiangli.png");

                                    region.saveScreenCapture(".", "lianmeng");
                                    if (mengzhanlingqujiangli != null) {
                                        mengzhanlingqujiangli.click();

                                        Match lingquguojiangli = region.exists(baseDir + "lingquguojiangli.png");
                                        if (lingquguojiangli != null) {
                                            region.click(Common.QUE_DING);
                                        }

                                        status.Done(Task.LIAN_MENG_LIAN_MENG_ZHAN);
                                    }

                                    Thread.sleep(1000L);
                                    clickInside(region, Common.CLOSE);
                                }

                                Thread.sleep(1000L);
                                region.click(Common.CLOSE);
                            }
                        } else {
                            // 联盟报名则忽略此次操作
                            Match lianmengbaoming = region.exists(baseDir + "lianmengbaoming.png", 1);
                            if (lianmengbaoming != null) {
                                status.Done(Task.LIAN_MENG_LIAN_MENG_ZHAN);
                            }
                        }

                        Thread.sleep(1000L);
                        region.click(Common.CLOSE);
                    }
                }
            }

            if (status.canDo(Task.LIAN_MENG_QING_QIU_JUAN_KA) || status.canDo(Task.LIAN_MENG_JUAN_KA)) {
                Match juanka = region.exists(baseDir + "juanka.png");
                if (juanka != null) {
                    juanka.click();

                    Thread.sleep(3000L);

                    // 请求捐卡
                    if (status.canDo(Task.LIAN_MENG_QING_QIU_JUAN_KA)) {
                        Match qingqiujuanzeng = region.exists(baseDir + "qingqiujuanzeng.png");
                        if (qingqiujuanzeng == null) {
                            status.Done(Task.LIAN_MENG_QING_QIU_JUAN_KA, Status.nextCheck());
                        } else {
                            qingqiujuanzeng.click();
                            Thread.sleep(3000L);

                            Match left = region.exists(baseDir + "left.png");
                            if (left != null) {
                                for (int i = 0; i < 3; i++) {
                                    Match mengji = region.exists(baseDir + "mengji.png");
                                    if (mengji != null) {
                                        mengji.click();
                                        Thread.sleep(3000L);
                                        status.Done(Task.LIAN_MENG_QING_QIU_JUAN_KA);
                                        break;
                                    }

                                    move(left, left.getCenter().offset(-800, 0), 1000);
                                }
                            }
                        }
                    }

                    // 捐卡
                    if (status.canDo(Task.LIAN_MENG_JUAN_KA)) {
                        Match juanzeng = region.exists(baseDir + "juanzeng.png");
                        if (juanzeng == null) {
                            status.Done(Task.LIAN_MENG_JUAN_KA, Status.nextCheck());
                        } else {
                            for (int i = 0; i < 4; i++) {
                                juanzeng.click();
                                Thread.sleep(1000L);
                            }

                            status.Done(Task.LIAN_MENG_JUAN_KA);
                        }
                    }
                }
            }

            // 福利
            if (status.canDo(Task.LIAN_MENG_FU_LI)) {
                Match fuli = region.exists(baseDir + "fuli.png", 3);
                if (fuli != null) {
                    fuli.click();

                    Match lingqu = region.exists(baseDir + "lingqu.png", 5);
                    // 每天领取一次福利,并且进行一次捐赠
                    if (lingqu != null && isButtonEnable(lingqu, 5, 5)) {
                        lingqu.click();

                        Match gongxiandubugou = region.exists(baseDir + "gongxiandubugou.png");
                        if (gongxiandubugou != null) {
                            Thread.sleep(1000L);
                            region.click(Common.QUE_DING);
                            Thread.sleep(1000L);

                            juanxian(region, 1);
                            Thread.sleep(3000L);

                            fuli.click();
                            Thread.sleep(2000L);
                            lingqu.click();
                        }

                        status.Done(Task.LIAN_MENG_FU_LI);
                    }
                }
            }
        }

        Thread.sleep(2000L);

        region.click(Common.CLOSE);
        Thread.sleep(1000L);

        region.click(Common.MENU1);

        return true;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        if (!status.canDo(Task.LIAN_MENG_GONG_FENG, userName)
                && !status.canDo(Task.LIAN_MENG_QING_QIU_JUAN_KA, userName)
                && !status.canDo(Task.LIAN_MENG_JUAN_KA, userName)
                && !status.canDo(Task.LIAN_MENG_NAN_MAN, userName)
                && (!status.canDo(Task.LIAN_MENG_NAN_MAN_KAI_SHI, userName) || LocalTime.now().isBefore(LocalTime.of(20, 0)))
                && !status.canDo(Task.LIAN_MENG_FU_LI, userName)
                && !status.canDo(Task.LIAN_MENG_LIAN_MENG_ZHAN, userName)) {
            return false;
        }

        return true;
    }

    private void juanxian(Region region, int iRenLi) throws FindFailed, InterruptedException {
        Match juanxian = region.exists(baseDir + "juanxian.png");
        if (juanxian != null) {
            juanxian.click();

            Match renli = region.exists(baseDir + "renliinput.png", 5);
            if (renli != null) {
                renli.type(String.valueOf(iRenLi));

                region.click(baseDir + "juanxiananniu.png");
                Thread.sleep(500L);
            }
        }
    }
}
