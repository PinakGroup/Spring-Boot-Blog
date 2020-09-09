package com.li.blog.service.impl;

import com.li.blog.beans.Article;
import com.li.blog.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author li
 * @version 1.0
 * @since 18-9-16 下午3:52
 **/
@Service
@CacheConfig(cacheNames = "springboot-blog-mp-articles")
class  ReadNumService{

    private final ArticleMapper articleMapper;

    @Autowired
    public ReadNumService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Cacheable(keyGenerator = "articleKeyGenerator")
    public Article selectById(Article article) {
        return articleMapper.selectById(article.getArticleId());
    }

}
