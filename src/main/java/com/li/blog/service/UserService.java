package com.li.blog.service;

import com.li.blog.beans.User;

/**
 * @author li
 * @version 1.0
 * @since 18-9-14 上午12:04
 **/
public interface UserService {

    /** 根据用户id获取用户信息
     * @param id    用户id
     * @return  用户对象
     */
    User userInfo(Integer id);

    /** 根据用户名获取用户及其文章
     * @param username  用户名
     * @return  用户对象
     */
    User selectUserWithArticles(String username);

    /** 更改邮箱
     * @param id    用户id
     * @return  数据库更新记录数
     */
    Integer updateEmail(Integer id);



}
