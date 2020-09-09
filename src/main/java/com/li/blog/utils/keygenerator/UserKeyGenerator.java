package com.li.blog.utils.keygenerator;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author li
 * @version 1.0
 * @since 18-9-14 上午12:30
 **/
@Component
public class UserKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return "user-with-articles:id=" + params[0];
    }

}
