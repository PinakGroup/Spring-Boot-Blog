package com.li.blog.config.security;

import com.li.blog.beans.User;
import com.li.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author li
 * @version 1.0
 * @since 18-8-11 上午12:34
 **/
@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    /**
     *  管理员用户
     */
    private static final String ADMINISTRATOR = "li";
    @Override
    public UserDetails loadUserByUsername(String username){
        //根据用户名从数据库查询对应记录
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (ADMINISTRATOR.equals(user.getUsername())) {
            authorities.add(new SimpleGrantedAuthority("admin"));
        } else {
            authorities.add(new SimpleGrantedAuthority("user"));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }

}
