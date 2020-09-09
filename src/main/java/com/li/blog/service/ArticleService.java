package com.li.blog.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.li.blog.beans.Article;
import com.baomidou.mybatisplus.service.IService;

import java.security.Principal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2018-09-10
 */
public interface ArticleService extends IService<Article> {

    /** 根据id查询文章
     * @param article   包含id的文章对象
     * @return  文章
     */
    Article selectArticleById(Article article);

    /** 插入文章并缓存
     * @param article   文章对象
     * @param principal 用户信息
     * @return  文章
     */
    Article insertAndCache(Article article, Principal principal);

    /** 更新文章并缓存
     * @param article   文章
     * @return  文章
     */
    Article updateAndCache(Article article);

    /** 查找最新文章
     * @param pageNum   页码
     * @return  文章分页
     */
    Page<Article> selectNewestArticles(Integer pageNum);

    /** 查询分钟并更新阅读数
     * @param article   文章
     * @return  文章
     */
    Article selectAndUpdateReadNum(Article article);
}
