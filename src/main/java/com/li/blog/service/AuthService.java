package com.li.blog.service;

import com.li.blog.beans.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2018-09-04
 */
public interface AuthService extends IService<User> {
    /** 注册
     * @param username  用户名
     * @param rawPassword   密码
     * @param email 邮箱
     * @return  成功或失败
     */
    boolean register(String username, String rawPassword, String email);

}
