package com.li.blog.utils.keygenerator;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author li
 * @version 1.0
 * @since 18-9-11 上午10:10
 **/
@Component
public class PageKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getName() + method.getName() +":pageNum===>"+ params[0];
    }

}
