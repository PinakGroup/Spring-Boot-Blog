package com.li.blog.utils;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author li
 * @version 1.0
 * @date 18-8-10 下午11:23
 **/
public class PasswdEncoder {
    private static PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static String encodePassword(String rawPassword) {
        return pe.encode(rawPassword);
    }

}
