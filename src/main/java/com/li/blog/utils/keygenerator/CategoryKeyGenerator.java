package com.li.blog.utils.keygenerator;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author li
 * @version 1.0
 * @since 18-9-16 下午7:35
 **/
@Component
public class CategoryKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return ":category:id=" + params[0];
    }

}
