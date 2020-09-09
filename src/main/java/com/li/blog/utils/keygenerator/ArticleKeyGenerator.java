package com.li.blog.utils.keygenerator;

import com.li.blog.beans.Article;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author li
 * @version 1.0
 * @since 18-9-11 上午9:48
 **/
@Component
public class ArticleKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        Article article = (Article) params[0];
        return ":article:id=" + article.getArticleId();
    }

}
