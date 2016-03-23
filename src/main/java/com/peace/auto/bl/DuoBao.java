package com.peace.auto.bl;

import com.google.common.collect.Lists;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.List;
import java.util.Optional;

/**
 * Created by mind on 3/17/16.
 */
public class DuoBao implements IDo {
    String baseDir = Common.BASE_DIR + "duobao/";

    @Override
    public boolean Done(Region region, Status status) throws FindFailed, InterruptedException {
        return xunbao(region, false);
    }

    public boolean xunbao(Region region) throws InterruptedException, FindFailed {
        return xunbao(region, true);
    }

    private boolean xunbao(Region region, boolean keepXunbao) throws FindFailed, InterruptedException {
        region.click(Common.RI_CHANG);

        Thread.sleep(3000L);

        Match tiaozhan = region.exists(baseDir + "tiaozhan.png", 10);
        if (tiaozhan != null) {
            tiaozhan.click();

            Thread.sleep(3000L);

            Match duobao = region.exists(baseDir + "duobaoqibing.png");
            if (duobao != null) {
                duobao.click();

                region.click(baseDir + "duobaoqibingjingru.png");

                // 领取宝图
                Match baotu = region.exists(baseDir + "lingqubaotu.png");
                if (baotu != null) {
                    baotu.click();
                    Thread.sleep(1000L);

                    Match kaishi = region.exists(baseDir + "kaishixunbao.png");
                    if (kaishi != null) {
                        kaishi.click();
                    }
                }

                Thread.sleep(2000L);

                Match chazhao = region.exists(baseDir + "chazhaofangjian.png");
                if (chazhao != null) {
                    // 持续寻宝
                    if (keepXunbao) {

                    } else {
                        Match shurufangjian = region.exists(baseDir + "shurufangjianhaoma.png");
                        if (shurufangjian != null) {
                            shurufangjian.type("31");
                        }
                        chazhao.click();

                        Thread.sleep(2000L);
                        jiaru(region);
                    }
                }
            }

            Thread.sleep(3000L);
            region.click(Common.CLOSE);
        }

        Thread.sleep(500L);
        region.click(Common.CLOSE);

        return true;
    }

    private void jiaru(Region region) throws FindFailed {
        Match jiaru = region.exists(baseDir + "jiaru.png");
        if (jiaru != null) {
            List<Match> jiarus = Lists.newArrayList(region.findAll(baseDir + "jiaru.png"));

            Optional<Match> firstJiaru = jiarus.stream().sorted((x, y) -> x.getX() - y.getX()).sorted((x, y) -> x.getY() - y.getY()).findFirst();
            if (firstJiaru.isPresent()) {
                firstJiaru.get().click();
            }
        }
    }
}
