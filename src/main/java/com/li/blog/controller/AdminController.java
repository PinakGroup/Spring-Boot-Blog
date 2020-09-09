package com.li.blog.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.li.blog.beans.User;
import com.li.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**后台管理控制器
 * @author li
 * @version 1.0
 * @date 18-8-9 下午3:28
 **/
@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private final AuthService authService;

    @Autowired
    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 返回后台管理页面
     */
    @GetMapping
    public ModelAndView admin(Model model) {
        return new ModelAndView("admin/admin", "model", model);
    }

    @GetMapping("/listUsers/{pageNum}")
    public String listUsers(Model model,
                            @PathVariable(required = false) int pageNum) throws RuntimeException{

        if (pageNum < 1) {
            pageNum = 1;
        }
        int pageSize = 2;
        Page<User> userPage = new Page<>(pageNum, pageSize);
        Page<User> page = authService.selectPage(userPage);
        page.getCurrent();
        model.addAttribute("page", page);
        return "admin/userList";
    }


}
