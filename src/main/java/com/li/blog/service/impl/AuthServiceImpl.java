package com.li.blog.service.impl;

import com.li.blog.beans.User;
import com.li.blog.mapper.UserMapper;
import com.li.blog.service.AuthService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.li.blog.utils.PasswdEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2018-09-04
 */
@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    @Override
    public boolean register(String username, String rawPassword, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswdEncoder.encodePassword(rawPassword));
        user.setEmail(email);
        boolean b = user.insert();
        System.out.println(user.getId());
        return b;
    }

}
