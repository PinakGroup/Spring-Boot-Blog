package com.li.blog.service;

/**
 * @author li
 */
public interface EmailService {
    /** 发送邮件
     * @param to    收件地址
     * @return  验证码
     */
    String sendEmail(String to);
}
