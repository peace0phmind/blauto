package com.peace.auto.bl;

import com.peace.sikuli.monkey.AndroidScreen;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mind on 3/2/16.
 */
@Slf4j
public class Main {
    static List<IDo> tasks = Arrays.asList(
            new ShenShouWu(),

            new LianBingChang(),
            new ShiLianDong(),
            new ChuZheng(),

            new ShouGuFang(),
            new XunBao(),
            new Building(),
            new NongChang(),
            new ShengYu(),
            new TianSheng(),
            new LieChang(),
            new ShiChang(),

            new LianMeng(),
            new YingHun(),
            new HaoYou(),

            new JingJiChang(),
            new ShengLingQuan(),

            new Task(),
            new JiangLi()
    );

    static public void Do(Region region, List<IDo> dos, int times) {
        Do(region, dos, times, true, 3);
    }

    static public void Do(Region region, List<IDo> dos, int times, boolean reboot, int waitSeconds) {
        try {
            for (int i = 0; i < times; i++) {
                // 点击收起对话框
                Match duihua = region.exists(Common.BASE_DIR + "guanbiduihua.png");
                if (duihua != null && duihua.getScore() > 0.95) {
                    duihua.click();
                    Thread.sleep(1000L);
                }

                for (IDo iDo : dos) {
                    if (iDo.Done(region)) {
                        Thread.sleep(waitSeconds * 1000L);
                    }
                }

                if (reboot) {
                    new DengLu().Done(region);
                }
            }

            if (reboot) {
                IDo.setTodayFirstFinished();
            }
        } catch (FindFailed findFailed) {
            region.saveScreenCapture(".", "error");
            log.error("{}", findFailed);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }

    public static void main(String[] args) throws FindFailed, InterruptedException {
        Settings.OcrTextRead = true;
        AndroidScreen region = new AndroidScreen();

//        region.saveScreenCapture(".", "info");

//        ArrayList<Match> matches = Lists.newArrayList(region.findAll(new Pattern(Common.BASE_DIR + "denglu/peace.png").similar(0.5f)));
//        matches.forEach(x -> log.info("test: {}", x.text()));

        // qidong login
//        new DengLu().QiDong(region);

        // xiaohao renwu
//        new DengLu().Done(region);
        Do(region, tasks, 6);

//        Do(region, tasks, 6);

//        Do(region, tasks, 6);

//        Do(region, tasks, 6);

//        Do(region, tasks, 6);

        // 切换账号 到peace, 如果peace在最下面
//        new DengLu().similar(0.5f).Done(region);

        // peace tasks
//        Do(region, Arrays.asList(
//                new ShouGuFang(),
//                new Building(),
//                new NongChang(),
//                new ShengYu(),
//                new TianSheng(),
//                new JingJiChang(),
//                new ShengLingQuan(),
//                new Task()
//        ), 1, false, 3);

        // peace jingjichang
//        Do(region, Arrays.asList(new JingJiChang()), 10, false, 10 * 60);

//        Do(region, Arrays.asList(new DuoBao()), 6);

//        Do(region, Arrays.asList(new NongChang()), 1);

//        Do(region, Arrays.asList(new NongChang()), 1, false, 3);

        region.close();
    }
}
