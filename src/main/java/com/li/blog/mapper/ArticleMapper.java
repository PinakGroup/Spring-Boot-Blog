package com.li.blog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.li.blog.beans.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2018-09-10
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    /** 根据文章id查询文章及其对应的分类
     * @param id    文章id
     * @return  文章对象
     */
    Article selectArticleWithCategory(@Param("id") Integer id);

    /** 根据文章id查询文章及其对应的评论
     * @param id    文章id
     * @return  文章对象
     */
    Article selectArticleWithComments(@Param("id") Integer id);
}
