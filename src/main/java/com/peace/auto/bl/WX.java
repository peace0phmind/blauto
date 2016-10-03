package com.peace.auto.bl;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by mind on 8/7/16.
 */
@Slf4j
public class WX {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MjM5OTk3MDA4MQ==&uin=MTY5NTA2ODk4MA%3D%3D&key=8dcebf9e179c9f3a001f674f8d48e4f3741a2d04c0c59d71b28a531dac1306128f8b2d1668a0b0b164c121f9f9f735bd&devicetype=iPhone+OS9.3.3&version=16031712&lang=zh_CN&nettype=WIFI&fontScale=100&pass_ticket=GQIfdQaJd%2BNZH2VOv6iMn2a3PkKxB3cpLPcW%2BmggwNHaUM9xcIqRHq2Ru4qH%2F%2FeV").get();
        log.info("{}", Hashing.sha1().newHasher().putString(doc.toString(), Charsets.UTF_8).hash());

        Elements msg_title = doc.getElementsByClass("msg_title");

        msg_title.stream().forEach(x -> log.info("{}", x));

    }
}
