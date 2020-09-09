package com.li.blog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.li.blog.beans.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2018-09-04
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /** 通过用户名查询用户
     * @param username  用户名
     * @return  用户对象
     */
    User selectByUsername(@Param("username") String username);

    /** 通过用户名查询用户和对应的文章列表
     * @param username  用户名
     * @return  用户对象
     */
    User selectUserWithArticles(@Param("username") String username);

}
