package com.peace.auto.bl.task;


import com.google.common.collect.Lists;
import com.peace.auto.bl.Status;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

enum GemType {
    GONG_JI,
    FANG_YU,
    TI_LI,
    JI_LI;

    public static GemType of(int x, int y) {
        if (x < 200 && y < 230) {
            return GONG_JI;
        }
        if (x > 200 && y < 230) {
            return FANG_YU;
        }
        if (x < 200 && y > 230) {
            return TI_LI;
        }
        return JI_LI;
    }
}

enum GenLevel {
    EIGHT(8, "8xin.png"),
    SEVEN(7, "7xin.png"),
    FIVE(5, "5xin.png"),
    FOUR(4, "4xin.png"),
    SIX(6, "6xin.png"),
    THREE(3, "3xin.png"),
    TWO(2, "2xin.png"),
    ONE(1, "1xin.png"),;

    private int level;
    private String pic;

    GenLevel(int level, String pic) {
        this.level = level;
        this.pic = pic;
    }

    public int getLevel() {
        return this.level;
    }

    public String getPic() {
        return this.pic;
    }
}

enum HunType {
    PAO("paoling.png"),
    MO("moling.png"),
    YONG("yongling.png"),
    GONG("jianling.png");

    private String pic;

    HunType(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return this.pic;
    }
}

enum WeaponType {
    XUE(368),
    JIA(302),
    KUN(236),
    ZHUI(170),
    WEAPON(104);

    private int x;

    WeaponType(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }
}

/**
 * Created by mind on 8/14/16.
 */
@Slf4j
public class BaoShi implements IDo {
    private static final int WU_QI_Y = 408;

    String baseDir = Common.BASE_DIR + "baoshi/";

    @Override
    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {

//        huoQuXinXi(region, status);

//        beiBaoHuoQu(region, status);

        for (int i = 0; i < 1; i++) {
            if (shengJi(region, status)) {
                if (!beiBaoHuoQu(region, status)) {
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean CanDo(Status status, String userName) {
        return true;
    }

    private boolean shengJi(Region region, Status status) throws FindFailed, InterruptedException {
        boolean ret = false;
        region.click(Common.MENU);

        Match baoshi = region.exists(baseDir + "baoshi.png", 3);
        if (baoshi != null) {
            baoshi.click();
            Thread.sleep(3000L);

            Match kongbaoshi = region.exists(baseDir + "kongbaoshi.png");
            if (kongbaoshi != null) {
                ret = true;
            } else {
                List<Gem> gems = status.listGemLevel().entrySet().stream().map(x -> new Gem(x.getKey(), x.getValue())).collect(Collectors.toList());
                Map<GemType, List<Gem>> gemTypeListMap = gems.stream().collect(Collectors.groupingBy(x -> x.gemType));

                for (GemType gemType : gemTypeListMap.keySet()) {
                    List<Gem> typedGem = gemTypeListMap.get(gemType).stream()
                            .sorted((x, y) -> x.weaponType.ordinal() - y.weaponType.ordinal())
                            .sorted((x, y) -> x.hunType.ordinal() - y.hunType.ordinal())
                            .sorted((x, y) -> x.level - y.level)
                            .collect(Collectors.toList());


                    Gem gem = typedGem.get(0);
                    region.click(baseDir + gem.hunType.getPic());
                    Thread.sleep(1000L);
                    doRobot(region, robot -> robot.touch(gem.weaponType.getX(), WU_QI_Y));
                    Thread.sleep(1000L);

                    ArrayList<Match> shengjis = Lists.newArrayList(region.findAll(baseDir + "shengji.png"));
                    for (Match shenji : shengjis) {
                        if (gemType == GemType.of(shenji.x, shenji.y)) {
                            shenji.click();
                            Thread.sleep(1000l);
                            break;
                        }
                    }

                    ArrayList<Match> kongcaos = Lists.newArrayList(region.findAll(baseDir + "kongcao.png"));
                    kongcaos.forEach(x -> log.info("{}", x));
                    if (kongcaos.size() < 8) {

                        Match hecheng = region.exists(baseDir + "hecheng.png");
                        if (hecheng != null) {
                            hecheng.click();
                            Thread.sleep(1000l);

                            Match jingyanyichu = region.exists(baseDir + "jingyanyichu.png");
                            if (jingyanyichu != null) {
                                region.click(Common.QUE_DING);
                                Thread.sleep(1000l);
                                // TODO
                            }
                        }
                    } else {
                        break;
                    }

                    region.click(baseDir + "fanhui.png");
                    Thread.sleep(1000L);
                }

                kongbaoshi = region.exists(baseDir + "kongbaoshi.png");
                if (kongbaoshi == null) {
                    ret = true;
                }
            }

            Thread.sleep(1000L);
            region.click(Common.CLOSE);
            Thread.sleep(1000L);
        }

        return ret;
    }

    private boolean beiBaoHuoQu(Region region, Status status) throws FindFailed, InterruptedException {
        boolean ret = false;
        region.click(Common.MENU);
        Match beibao = region.exists(baseDir + "beibao.png", 3);

        if (beibao != null) {
            beibao.click();
            Thread.sleep(3000L);

            Match zidongzhengli = region.exists(baseDir + "zidongzhengli.png");
            if (zidongzhengli != null) {
                zidongzhengli.click();
                Thread.sleep(1000L);
            }

            for (int i = 0; i < 3; i++) {
                move(region, new Location(200, 240), 1);
                Thread.sleep(1000L);
                Match exists = region.exists(baseDir + "2level.png", 1);
                if (exists != null) {
                    exists.click();
                    Thread.sleep(1000L);

                    Match tenGe = region.exists(baseDir + "shiyong10ge.png");
                    if (tenGe != null) {
                        tenGe.click();

                        Match queding = region.exists(Common.QUE_DING);
                        if (queding != null) {
                            queding.click();
                            Thread.sleep(1000l);
                            ret = true;
                        }
                    }
                    break;
                }
            }


            Thread.sleep(1000L);
            region.click(Common.CLOSE);
            Thread.sleep(1000L);
        }

        return ret;
    }

    private boolean huoQuXinXi(Region region, Status status) throws InterruptedException, FindFailed {
        region.click(Common.MENU);

        Match baoshi = region.exists(baseDir + "baoshi.png", 3);
        if (baoshi != null) {
            baoshi.click();
            Thread.sleep(3000L);

            for (HunType hunType : HunType.values()) {
                Match matchHun = region.exists(baseDir + hunType.getPic());
                if (matchHun != null) {
                    matchHun.click();
                    Thread.sleep(1000L);

                    for (WeaponType weaponType : WeaponType.values()) {
                        doRobot(region, robot -> robot.touch(weaponType.getX(), WU_QI_Y));
                        Thread.sleep(1000L);

                        log.info("{}, {}, {}", status.getCurrentUser(), hunType, weaponType);

                        int levelCount = 0;
                        for (GenLevel gemLevel : GenLevel.values()) {
                            Pattern picPattern = new Pattern(baseDir + gemLevel.getPic()).similar(0.9f);
                            if (region.exists(picPattern, 0.1) != null) {
                                ArrayList<Match> all = Lists.newArrayList(region.findAll(picPattern));
                                levelCount += all.size();
                                all.forEach(x -> {
                                    GemType gemType = GemType.of(x.getX(), x.getY());
                                    status.saveGemLevel(hunType.toString(), weaponType.toString(),
                                            gemType.toString(), gemLevel.getLevel());
                                    log.info("{}, {}, {}", gemType, gemLevel, x);
                                });
                            }

                            // fast break level detect
                            if (levelCount == 4) {
                                break;
                            }
                        }
                    }
                }
            }
            Thread.sleep(1000L);
            region.click(Common.CLOSE);
            return true;
        }

        return false;
    }
}

class Gem {
    HunType hunType;
    WeaponType weaponType;
    GemType gemType;
    int level;

    Gem(String key, int level) {
        this.level = level;
        String[] split = key.split(":");
        this.hunType = HunType.valueOf(split[0]);
        this.weaponType = WeaponType.valueOf(split[1]);
        this.gemType = GemType.valueOf(split[2]);
    }
}