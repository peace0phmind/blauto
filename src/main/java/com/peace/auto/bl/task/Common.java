package com.peace.auto.bl.task;

import org.sikuli.script.Pattern;

/**
 * Created by mind on 3/3/16.
 */
public interface Common {
    String BASE_DIR = "src/main/resources/images/";

    String QUE_DING = BASE_DIR + "queding.png";

    Pattern CLOSE = new Pattern(BASE_DIR + "close.png").similar(0.90f);

    String HUI_CHENG = BASE_DIR + "huicheng.png";

    Pattern MENU = new Pattern(BASE_DIR + "menu.png").similar(0.95f);

    String MENU1 = BASE_DIR + "menu1.png";

    String RI_CHANG = BASE_DIR + "richang.png";

    String QU_XIAO = BASE_DIR + "quxiao.png";

    Pattern RONG_YAO = new Pattern(BASE_DIR + "huoderongyao.png").similar(0.8f);
}
