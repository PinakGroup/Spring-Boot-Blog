package com.li.blog.service.impl;

import com.li.blog.beans.User;
import com.li.blog.mapper.UserMapper;
import com.li.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author li
 * @version 1.0
 * @since 18-9-14 上午12:27
 **/
@Service
@CacheConfig(cacheNames = "springboot-blog-mp-user")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User userInfo(Integer id) {
        return null;
    }


    @Override
    @Cacheable(keyGenerator = "userKeyGenerator")
    public User selectUserWithArticles(String username) {
        return userMapper.selectUserWithArticles(username);
    }

    @Override
    public Integer updateEmail(Integer id) {
        return null;
    }
}
