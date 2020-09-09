package com.li.blog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.li.blog.beans.Like;
import com.li.blog.mapper.LikeMapper;
import com.li.blog.service.LikeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2018-09-20
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {
    public Like selectByArticleIdAndUsername(Integer articleId, String username) {
        Like like = super.selectOne(new EntityWrapper<Like>().eq("article_id", articleId).and().eq("username", username));
        if (like == null && username != null) {
            like = new Like();
            like.setArticleId(articleId);
            like.setUsername(username);
            like.setLikeStatus("");
            super.insert(like);
        }
        return like;
    }
}
