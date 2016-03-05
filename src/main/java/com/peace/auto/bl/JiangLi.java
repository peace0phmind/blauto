package com.peace.auto.bl;

import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import java.util.Iterator;

/**
 * Created by mind on 3/3/16.
 */
@Slf4j
public class JiangLi {
    static public void Do(Region region) {
        String baseDir = Common.BASE_DIR + "jiangli/";

        try {
            region.click(baseDir + "jiangli.png");

            Match injiangli = region.exists(baseDir + "injiangli.png", 20);
            if (injiangli == null) {
                return;
            }

            Match qiandao  = region.exists(baseDir + "qiandao.png");
            if (qiandao != null) {
                qiandao.click();
            }

            Match lianxuqiandaojiangli = region.exists(baseDir + "lingquqiandaojiangli.png");
            if (lianxuqiandaojiangli != null) {
                lianxuqiandaojiangli.click();
            }


            // 连续登陆
            region.click(baseDir + "lianxudenglu.png");
            Thread.sleep(500L);

            Match lianxulingqu = region.exists(baseDir + "lianxulingqu.png");
            if (lianxulingqu != null) {
                lianxulingqu.click();
            }

            Iterator<Match> all = region.findAll(baseDir + "lingqu.png");
            while (all.hasNext()) {
                all.next().click();
            }

            // 活跃度
            region.click(baseDir + "huoyuedu.png");
            Thread.sleep(500L);

            Match lingqujiangli = region.exists(baseDir + "lingqujiangli.png");
            if (lingqujiangli != null) {
                for (int i = 0; i < 6; i++) {
                    lingqujiangli.click();
                }
            }

            region.click(Common.CLOSE);
            Thread.sleep(500L);
        } catch (FindFailed findFailed) {
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
