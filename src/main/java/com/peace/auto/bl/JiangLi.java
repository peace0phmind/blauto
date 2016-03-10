package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.Iterator;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class JiangLi implements IDo {
    String baseDir = Common.BASE_DIR + "jiangli/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        region.click(baseDir + "jiangli.png");

        Match injiangli = region.exists(baseDir + "injiangli.png", 20);
        if (injiangli == null) {
            return;
        }

        if (!isTodayFirstFinished()) {
            Match qiandao = region.exists(baseDir + "qiandao.png", 1);
            if (qiandao != null) {
                qiandao.click();
            }

            Match lianxuqiandaojiangli = region.exists(baseDir + "lingquqiandaojiangli.png", 1);
            if (lianxuqiandaojiangli != null) {
                lianxuqiandaojiangli.click();
            }

            // 连续登陆
            region.click(baseDir + "lianxudenglu.png");

            Match lianxulingqu = region.exists(baseDir + "lianxulingqu.png", 1);
            if (lianxulingqu != null) {
                lianxulingqu.click();
            }

            Match lingqu = region.exists(baseDir + "lingqu.png", 0.5);
            if (lingqu != null) {
                lingqu.click();
            }

            lingqu = region.exists(baseDir + "lingqu.png", 0.5);
            if (lingqu != null) {
                lingqu.click();
            }
        }

        // 活跃度
        region.click(baseDir + "huoyuedu.png");
        Match lingqujiangli = region.exists(baseDir + "lingqujiangli.png", 1);
        if (lingqujiangli != null) {
            while (isButtonEnable(lingqujiangli, 5, 5)) {
                lingqujiangli.click();
            }
        }

        region.click(Common.CLOSE);
    }
}
