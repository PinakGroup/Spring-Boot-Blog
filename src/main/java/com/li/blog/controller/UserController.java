package com.li.blog.controller;

import com.li.blog.beans.User;
import com.li.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**用户主页控制器
 * @author li
 * @version 1.0
 * @since 18-8-9 下午3:28
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String userCenter(Model model,
                             Principal principal) {
        if (principal != null) {
            User user = userService.selectUserWithArticles(principal.getName());
            model.addAttribute("user", user);
            return "user/center";
        }
        return "auth/login";
    }

}
