package com.li.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @author li
 * 开启缓存 @EnableCaching
 * 开启第三方登录 @EnableOAuth2Client
 * 开启异步任务 @EnableAsync
 */
@SpringBootApplication(scanBasePackages = "com.li.blog")
@EnableCaching
@EnableOAuth2Client
@EnableAsync
public class BlogApplication {

    public static void main(String[] args) {
        //解决因为elasticsearch启动异常的问题
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(BlogApplication.class, args);
    }
}
