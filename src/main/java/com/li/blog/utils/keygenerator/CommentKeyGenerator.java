package com.li.blog.utils.keygenerator;

import com.li.blog.beans.Comment;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author li
 * @version 1.0
 * @since 18-9-28 下午4:29
 **/
@Component
public class CommentKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        Comment comment = (Comment) params[0];
        return ":comment:id=" + comment.getArticleId();
    }

}
