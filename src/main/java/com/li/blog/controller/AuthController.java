package com.li.blog.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.li.blog.beans.User;
import com.li.blog.service.AuthService;
import com.li.blog.utils.PasswdEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author li
 * @since 2018-09-04
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private AuthService service;

    private static final String ERROR = "error";
    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("login", true);
        return "auth/login";
    }

    @GetMapping(value = "/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "用户名或密码错误！");
        return "auth/login";
    }

    @GetMapping(value = "/register")
    public String register(Model model) {
        model.addAttribute("register", true);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username")  @Validated String username,
                           @RequestParam("password") @Validated String password,
                           @RequestParam("repeatPassword") String repeatPassword,
                           @RequestParam("email")  @Validated String email,
                           @RequestParam("validationCode") int code,
                           Model model,
                           HttpSession session) {

        //检查用户名是否重复
        int count = service.selectCount(new EntityWrapper<User>().eq("username", username));
        if (count == 1) {
            model.addAttribute("UsernameExistError", "用户名已存在！");
            model.addAttribute("error", true);
        }
        //检查邮箱是否被注册
        count = service.selectCount(new EntityWrapper<User>().eq("email", email));
        if (count == 1) {
            model.addAttribute("EmailExistError", "邮箱已经被注册！");
            model.addAttribute("error", true);

        }
        //检查密码是否匹配
        if (!password.equals(repeatPassword)) {
            model.addAttribute("PassWordNotMatchError", "密码不匹配！");
            model.addAttribute("error", true);
        }
        //从当前会话中获取验证码并验证
        Object sessionCode = session.getAttribute("registerValidationCode");
        if (sessionCode == null || code != (int) sessionCode) {
            model.addAttribute("ValidationCodeError", "验证码不正确");
            model.addAttribute("error", true);
        }
        //如果有错误，返回注册页面
        if (model.containsAttribute(ERROR)) {
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("repeatPassword", repeatPassword);
            model.addAttribute("email", email);
            model.addAttribute("validationCode", code);
            return "auth/register";
        }
        boolean isOk = service.register(username, password, email);
        if (isOk) {
            //删除当前会话的验证码
            session.removeAttribute("registerValidationCode");
            return "redirect:login";
        } else {
            model.addAttribute("ServerError", "服务器繁忙，请稍后重试！");
            return "auth/register";
        }

    }


    @GetMapping("/forgetPassword")
    public String forgetPassword(Model model) {
        model.addAttribute("resetPassword", true);
        return "auth/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("repeatPassword") String repeatPassword,
                                @RequestParam("resetPasswordValidationCode") String resetPasswordValidationCode,
                                HttpSession session,
                                Model model) {

        email = email.toLowerCase();
        //验证邮箱是否存在
        User user = service.selectOne(new EntityWrapper<User>().eq("email", email));
        if (user == null) {
            model.addAttribute("EmailNotExist", "邮箱尚未注册！");
            model.addAttribute("error", true);
        }
        //验证密码两次输入是否一致
        if (!newPassword.equals(repeatPassword)) {
            model.addAttribute("PasswordNotMatch", "两次密码输入不匹配！");
            model.addAttribute("error", true);
        }
        //验证验证码是否正确
        String sessionCode = (String) session.getAttribute("resetPasswordValidationCode");
        if (sessionCode == null || sessionCode.equals(resetPasswordValidationCode)) {
            model.addAttribute("ValidationCodeError", "验证码错误！");
            model.addAttribute("error", true);
        }
        if (model.containsAttribute(ERROR)) {
            model.addAttribute("email", email);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("repeatPassword", repeatPassword);
            model.addAttribute("resetPasswordValidationCode", resetPasswordValidationCode);
            return "auth/resetPassword";
        }
        //将新密码保存到数据库
        if (user != null) {
            user.setPassword(PasswdEncoder.encodePassword(newPassword));
            boolean b = service.updateById(user);
            if (b) {
                model.addAttribute("ResetPasswordSuccess", "密码已重置！");
                session.removeAttribute("resetPasswordValidationCode");
                return "redirect:login";
            }
        }
        model.addAttribute("ServerError", "服务器繁忙，请稍后重试");
        return "auth/resetPassword";
    }
}

