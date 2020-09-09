package com.li.blog.config.security;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.li.blog.beans.User;
import com.li.blog.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @since 18-9-9 下午1:10
 **/
@Component
public class CustomPrincipalExtractor implements PrincipalExtractor {

    private final AuthService authService;

    private Logger logger = LoggerFactory.getLogger(CustomPrincipalExtractor.class);


    private static final String[] PRINCIPAL_KEYS = new String[] {"login", "id", "email" };

    @Autowired
    public CustomPrincipalExtractor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        //查看是否获取到邮件地址
        for (String key : PRINCIPAL_KEYS) {
            logger.info(key + "====> " + map.get(key));
        }
        //查询数据库是否已经保存该用户，如果没有，就新建用户并保存
        int i = authService.selectCount(new EntityWrapper<User>().eq("username", map.get("login")));
        if (i < 1) {
            String username = (String) map.get("login");
            String email = (String) map.get("email");
            User user = new User();
            user.setUsername(username);
            if (email != null) {
                user.setEmail(email.toLowerCase());
            }
            user.insert();
        }
        return map.get("login");
    }
}
