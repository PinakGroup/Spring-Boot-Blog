package com.li.blog.controller;

import com.li.blog.beans.Article;
import com.li.blog.service.impl.SearchServiceImpl;
import com.li.blog.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author li
 * @version 1.0
 * @since 18-9-17 下午8:29
 **/
@Controller
@RequestMapping("/search")
public class SearchController {

    private final SearchServiceImpl searchService;

    @Autowired
    public SearchController(SearchServiceImpl searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/")
    public String search(String q, Integer page, Model model) {
        Page<Article> articlePage = searchService.multiMatchQuery(q, page, null);
        //设置页码导航范围
        List<Long> pageRange = PaginationUtil.getPageRange(page, articlePage.getTotalPages());
        model.addAttribute("page", articlePage);
        model.addAttribute("keyword", q);
        model.addAttribute("pageRange", pageRange);
        model.addAttribute("currentPage", page);
        return "article/search";
    }
}
