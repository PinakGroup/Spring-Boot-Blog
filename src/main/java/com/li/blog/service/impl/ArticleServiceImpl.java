package com.li.blog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.li.blog.beans.Article;
import com.li.blog.beans.Category;
import com.li.blog.mapper.ArticleMapper;
import com.li.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author li
 * @since 2018-09-10
 */
@EnableAsync
@CacheConfig(cacheNames = "springboot-blog-mp-articles")
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    private final CategoryServiceImpl categoryService;

    private final AccessDataServiceImpl accessDataService;

    private final SearchServiceImpl searchService;

    private static final Integer PAGE_SIZE = 5;

    private static final Integer CONTENT_LENGTH = 300;


    @Autowired
    public ArticleServiceImpl(ArticleMapper articleMapper, CategoryServiceImpl categoryService, AccessDataServiceImpl accessDataService, SearchServiceImpl searchService) {
        this.articleMapper = articleMapper;
        this.categoryService = categoryService;
        this.accessDataService = accessDataService;
        this.searchService = searchService;
    }

    /**
     * 将文章列表的缓存清除并将插入返回的结果放入缓存中
     */
    @Override
    @Caching(
            evict = {@CacheEvict(value = "springboot-blog-mp-articles-list", allEntries = true)},
            put = {@CachePut(cacheNames = "springboot-blog-mp-articles", keyGenerator = "articleKeyGenerator")}
    )
    public Article insertAndCache(Article article, Principal principal) {
        article.setAuthor(principal.getName());
        article.setCreatedTime(new Date());
        article.setLastUpdate(new Date());
        article.setReadNum(0);
        article.setLikeNum(0);
        if (StringUtils.isEmpty(article.getSummary())) {
            if (article.getContent().length() > CONTENT_LENGTH) {
                article.setSummary(article.getContent().substring(0, 300));
            } else {
                article.setSummary(article.getContent());
            }
        }
        super.insert(article);
        Category category = categoryService.selectById(article.getCategoryId());
        article.setCategory(category);
        if (article.getArticleId() != null) {
            //异步插入到es中
            searchService.index(article);
        }
        return article;
    }

    /**
     * @param article 文章id
     * @return 带分类信息和评论的文章具体细节
     * 根据文章id查询文章及其分类信息和评论
     */
    @Override
    @Cacheable(keyGenerator = "articleKeyGenerator")
    public Article selectArticleById(Article article) {
        return articleMapper.selectArticleWithComments(article.getArticleId());
    }

    @Cacheable(keyGenerator = "articleKeyGenerator")
    public Article selectArticleWithCategoryById(Article article) {
        return articleMapper.selectArticleWithCategory(article.getArticleId());
    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "springboot-blog-mp-articles-list", allEntries = true)},
            put = {@CachePut(cacheNames = "springboot-blog-mp-articles", keyGenerator = "articleKeyGenerator")}
    )
    public Article updateAndCache(Article article) {
        article.setLastUpdate(new Date());
        super.updateById(article);
        Category category = categoryService.selectById(article.getCategoryId());
        article.setCategory(category);
        //异步更新es
        searchService.update(article);
        return article;
    }

    @Override
    @Cacheable(cacheNames = "springboot-blog-mp-articles-list", keyGenerator = "pageKeyGenerator")
    public Page<Article> selectNewestArticles(Integer pageNum) {
        Page<Article> page = new Page<>(pageNum, PAGE_SIZE);
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        ArrayList<String> list = new ArrayList<>();
        //按时间倒序排列
        list.add("created_time");
        list.add("last_update");
        wrapper.orderDesc(list);
        wrapper.setSqlSelect("article_id, title, author,read_num,like_num, summary");
        List<Article> articles = articleMapper.selectPage(page, wrapper);
        page.setRecords(articles);
        return page;
    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "springboot-blog-mp-articles-list", allEntries = true)},
            put = {@CachePut(cacheNames = "springboot-blog-mp-articles", keyGenerator = "articleKeyGenerator")}
    )
    public Article selectAndUpdateReadNum(Article article) {
        article = articleMapper.selectArticleWithComments(article.getArticleId());
        article.setReadNum(article.getReadNum() + 1);
        super.updateById(article);
        //异步更新es
        searchService.update(article);
        //更新访问记录
        accessDataService.updateData(article.getArticleId());
        return article;
    }

    public Page<Article> selectHotArticles(Integer days, Integer pageNum) {
        List<Integer> articleIds = accessDataService.selectHotData(days);
        Page<Article> articlePage = new Page<>(pageNum, 5);
        EntityWrapper<Article> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("article_id", articleIds);
        articlePage = super.selectPage(articlePage, entityWrapper);
        return articlePage;
    }

    @Caching(
            evict = {@CacheEvict(keyGenerator = "articleKeyGenerator")},
            put = {@CachePut(cacheNames = "springboot-blog-mp-articles", keyGenerator = "articleKeyGenerator")}
    )
    public void updateLikeNumById(Article article) {
        super.updateById(article);
        //异步更新es
        searchService.update(article);
    }

    @Caching(
            evict = {
                    @CacheEvict(keyGenerator = "articleKeyGenerator"),
                    @CacheEvict(value = "springboot-blog-mp-articles-list", allEntries = true)
            }
    )
    public boolean delete(Article article) {
        searchService.delete(article);
        return super.deleteById(article);
    }
}

