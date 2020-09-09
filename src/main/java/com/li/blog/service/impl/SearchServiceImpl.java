package com.li.blog.service.impl;

import com.li.blog.beans.Article;
import com.li.blog.repository.ArticleRepository;
import com.li.blog.service.SearchService;
import com.li.blog.utils.BeanUtil;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author li
 * @version 1.0
 * @since 18-10-22 下午11:53
 **/
@Service
public class SearchServiceImpl implements SearchService {
    private final ArticleRepository articleRepository;
    private static final int PAGE_SIZE = 5;


    @Autowired
    public SearchServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Async
    public void index(Article article) {
        articleRepository.index(article);
    }

    @Async
    public void delete(Article article) {
        articleRepository.delete(article);
    }

    @Async
    public void update(Article article) {
        Optional<Article> repositoryOne = articleRepository.findById(article.getArticleId());
        if (!repositoryOne.isPresent()) {
            return;
        }
        //将article的非空属性值传到repositoryOne中
        List<String> ignoreFields = new ArrayList<>();
        ignoreFields.add("comments");
        ignoreFields.add("logicFlag");
        BeanUtil.transferValue(article, repositoryOne.get(), ignoreFields);
        //更新到es
        articleRepository.index(repositoryOne.get());
    }

    /**
     * 多字段分页精准搜索
     *
     * @param keyword 关键词
     * @param page    页码
     * @param type    搜索类型 见{@link MultiMatchQueryBuilder.Type}
     * @param fields  字段
     */
    public Page<Article> multiMatchQuery(String keyword, Integer page,Type type, String... fields) {

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        if (fields.length < 1) {
            fields = new String[]{"title", "content", "author", "category"};
        }
        if (type == null) {
            type = Type.BEST_FIELDS;
        }
        MultiMatchQueryBuilder queryBuilder =
                new MultiMatchQueryBuilder(keyword, fields)
                        .type(type);
        Page<Article> articlePage = articleRepository.search(queryBuilder, pageable);
        return new PageImpl<>(articlePage.getContent(), pageable, articlePage.getTotalElements());

    }


    /**
     * 文章标题和内容分页模糊搜索
     *  无法解决空格问题
     * @param keyword 关键词
     * @param page    页码
     */
    public Page<Article> likeMatchQuery(String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        Page<Article> articlePage = articleRepository.findArticlesByTitleLikeOrContentLike(keyword, keyword, pageable);
        return new PageImpl<>(articlePage.getContent(), pageable, articlePage.getTotalElements());
    }

}
