package com.li.blog.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.li.blog.beans.Article;
import com.li.blog.beans.Category;
import com.li.blog.service.ArticleService;
import com.li.blog.service.impl.CategoryServiceImpl;
import com.li.blog.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author li
 * @since 2018-09-15
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryServiceImpl categoryService;
    private final ArticleService articleService;
    private static final int PAGE_SIZE = 5;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService, ArticleService articleService) {
        this.categoryService = categoryService;
        this.articleService = articleService;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCategory(Category category) {
        Map<String, Object> map = new HashMap<>();
        try {
            categoryService.insert(category);
            map.put("success", true);
            map.put("cid", category.getCategoryId());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/list")
    public String categoryList(Model model) {
        Page<Category> categoryPage = categoryService.selectPage(new Page<>(1, 10));
        model.addAttribute("categoryPage", categoryPage);
        return "article/category";
    }

    @GetMapping("/{categoryId}/{pageNum}")
    public String category(@PathVariable Integer categoryId,
                           @PathVariable Integer pageNum,
                           Model model) {
        Page<Article> articlePage = new Page<>(pageNum, PAGE_SIZE);
        articlePage = articleService.selectPage(articlePage, new EntityWrapper<Article>().eq("category_id", categoryId));
        List<Long> pageRange = PaginationUtil.getPageRange(pageNum, articlePage.getPages());
        Category category = categoryService.selectById(categoryId);
        model.addAttribute("category", category);
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageRange", pageRange);
        return "article/category";
    }


}

