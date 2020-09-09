package com.li.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author li
 * @version 1.0
 * @since 18-9-8 上午12:26
 **/
@Service
public class EmailServiceImpl implements com.li.blog.service.EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public String sendEmail(String to) {
        String code = getCode();
        System.out.println(code);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("zulongli@aliyun.com");
        message.setTo(to);
        message.setSubject("浑蛋LiZL的博客");
        message.setText("您的邮件验证码为： " + code);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return code;
    }

    private String getCode() {
        int i = 100000;
        Random random = new Random();
        double aDouble = random.nextDouble();
        int code = (int) (aDouble * 1000000);
        if (code < i) {
            return "0" + code;
        }
        return code + "";
    }
}
