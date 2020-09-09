package com.li.blog.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.li.blog.beans.Category;
import com.li.blog.mapper.CategoryMapper;
import com.li.blog.service.CategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2018-09-15
 */
@Service
@CacheConfig(cacheNames = "springboot-blog-mp-category")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Cacheable(keyGenerator = "categoryKeyGenerator")
    @Override
    public Category selectById(Serializable id) {
        return super.selectById(id);
    }

    @Override
    @Cacheable(keyGenerator = "categoryListKeyGenerator")
    public List<Category> selectList(Wrapper<Category> wrapper) {
        return super.selectList(wrapper);
    }

    @Caching(
            evict = {@CacheEvict(keyGenerator = "categoryListKeyGenerator")},
            cacheable = {@Cacheable(keyGenerator = "categoryKeyGenerator")}
    )
    @Override
    public boolean insert(Category entity) {
        return super.insert(entity);
    }
}
