package com.li.blog.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.li.blog.beans.Article;
import com.li.blog.beans.Category;
import com.li.blog.beans.User;
import com.li.blog.service.ArticleService;
import com.li.blog.service.AuthService;
import com.li.blog.service.CategoryService;
import com.li.blog.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * 主页控制器
 * @author li
 */
@Controller
public class MainController {

    private final AuthService service;

    private final EmailService emailService;

    private final ArticleService articleService;

    private final CategoryService categoryService;

    private static final String RESET_PASSWORD_VALIDATION_CODE_KEY = "resetPasswordValidationCode";

    private static final String REGISTER_VALIDATION_CODE_KEY = "registerValidationCode";

    @Autowired
    public MainController(AuthService service, EmailService emailService, ArticleService articleService, CategoryService categoryService) {
        this.service = service;
        this.emailService = emailService;
        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/")
    public String root(Model model) {
        Page<Article> page = articleService.selectNewestArticles(1);
        Page<Category> categories = categoryService.selectPage(new Page<>(1, 5));
        model.addAttribute("page", page);
        model.addAttribute("categories", categories.getRecords());
        return "index";
    }

    @GetMapping(value = "/index")
    public String index() {
        return "redirect:/";
    }

    @GetMapping("/exception")
    public String exception() throws Exception {
        throw new Exception();
    }

    @GetMapping("/runtimeException")
    public String runtimeException() {
        throw new RuntimeException();
    }

    @ResponseBody
    @PostMapping("/sendValidationCode")
    public ResponseEntity<Map<String, Object>> sendValidationCode(@RequestParam("email") String email,
                                                                  @RequestParam("codeForWhat") String codeForWhat,
                                                                  HttpSession session) {
        email = email.toLowerCase();
        Map<String, Object> map = new HashMap<>(4);
        if (RESET_PASSWORD_VALIDATION_CODE_KEY.equals(codeForWhat)) {
            //修改密码的验证码，首先验证邮箱是否存在
            int count = service.selectCount(new EntityWrapper<User>().eq("email", email));
            if (count < 1) {
                map.put("isSuccess", false);
                map.put("tips", "该邮箱未注册！");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } else if (REGISTER_VALIDATION_CODE_KEY.equals(codeForWhat)) {
            //注册的验证码，首先验证邮箱是否存在
            int count = service.selectCount(new EntityWrapper<User>().eq("email", email));
            if (count > 0) {
                map.put("isSuccess", false);
                map.put("tips", "该邮箱已注册！");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
        String code = emailService.sendEmail(email);
        if (code != null) {
            map.put("isSuccess", true);
            session.setAttribute(codeForWhat, code);
            //设置有效期为十分钟
            session.setMaxInactiveInterval(60 * 10);
            map.put("tips", "验证码已发送！十分钟内有效！");
        } else {
            map.put("isSuccess", false);
            map.put("tips", "服务器错误，请稍后重试！");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
