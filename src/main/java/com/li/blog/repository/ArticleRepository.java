package com.li.blog.repository;

import com.li.blog.beans.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author li
 * @version 1.0
 * @since 18-9-17 下午1:34
 **/
@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, Integer> {
    /** 模糊查询
     * @param title 标题
     * @param content   内容
     * @param pageable 分页对象
     * @return  文章分页列表
     */
    Page<Article> findArticlesByTitleLikeOrContentLike(String title, String content, Pageable pageable);
}
