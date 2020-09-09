package com.li.blog.service.impl;

import com.li.blog.beans.Comment;
import com.li.blog.mapper.CommentMapper;
import com.li.blog.service.CommentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2018-09-28
 */
@Service
@CacheConfig(cacheNames = "springboot-blog-mp-articles")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    @CacheEvict(keyGenerator = "commentKeyGenerator")
    public boolean insert(Comment comment) {
        return super.insert(comment);
    }

    @Override
    @CacheEvict(keyGenerator = "commentKeyGenerator")
    public boolean updateById(Comment comment) {
        return super.updateById(comment);
    }

    @CacheEvict(keyGenerator = "commentKeyGenerator")
    public boolean deleteById(Comment comment) {
        return super.deleteById(comment.getCommentId());
    }

}
