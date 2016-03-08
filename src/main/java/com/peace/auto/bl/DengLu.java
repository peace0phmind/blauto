package com.peace.auto.bl;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

/**
 * Created by mind on 3/7/16.
 */
public class DengLu implements IDo {
    String baseDir = Common.BASE_DIR + "denglu/";

    public void Do(Region region) throws FindFailed, InterruptedException {
        for (int i = 0; i < 100; i++) {
            Match dengdai = region.exists(new Pattern(baseDir + "dengdai.png").similar(0.5f));
            if (dengdai != null) {
                dengdai.highlight(3);
            }
        }
    }
}
