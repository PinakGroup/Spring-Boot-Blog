package com.li.blog.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.li.blog.beans.Article;
import com.li.blog.beans.Category;
import com.li.blog.beans.Like;
import com.li.blog.service.CategoryService;
import com.li.blog.service.impl.ArticleServiceImpl;
import com.li.blog.service.impl.LikeServiceImpl;
import com.li.blog.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author li
 * @since 2018-09-10
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    private final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleServiceImpl articleService;

    private final CategoryService categoryService;

    private final LikeServiceImpl likeService;

    private static final String FALSE = "false";

    @Autowired
    public ArticleController(ArticleServiceImpl articleService, CategoryService categoryService, LikeServiceImpl likeService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.likeService = likeService;
    }


    @GetMapping("/list/{pageNum}")
    public String listArticles(@PathVariable Integer pageNum,
                               Model model,
                               Principal principal) {
        Page<Article> page = articleService.selectNewestArticles(pageNum);
        List<Long> pageRange = PaginationUtil.getPageRange(pageNum, page.getPages());
        model.addAttribute("page", page);
        model.addAttribute("loginUser", principal);
        model.addAttribute("pageRange", pageRange);
        model.addAttribute("currentPage", pageNum);
        return "article/article_list";
    }

    @GetMapping("/{id}")
    public String viewArticleDetail(@PathVariable Integer id,
                                    Model model,
                                    Principal principal,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        Article article = new Article();
        article.setArticleId(id);
        boolean isNewSession = true;
        //检查是否同一会话访问
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("isNewSessionToRead-" + id)) {
                    isNewSession = false;
                    break;
                }
            }
        }

        if (!isNewSession) {
            article = articleService.selectArticleById(article);
        } else {
            article = articleService.selectAndUpdateReadNum(article);
            Cookie cookie = new Cookie("isNewSessionToRead-" + id, "false");
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);
        }
        if (principal != null) {
            Like like = likeService.selectByArticleIdAndUsername(id, principal.getName());
            model.addAttribute("like", like);
        }

        model.addAttribute("article", article);
        model.addAttribute("loginUser", principal);
        return "article/article_detail";

    }

    @GetMapping("/new")
    public String writeNewArticle(Model model) {
        List<Category> categories = categoryService.selectList(null);
        model.addAttribute("categories", categories);
        return "article/article_new";
    }

    @PostMapping("/post")
    public String postArticle(@Valid Article article,
                              Errors errors,
                              Principal principal) {
        if (errors.hasErrors()) {
            return "redirect:/error";
        }
        try {
            article = articleService.insertAndCache(article, principal);
            return "redirect:/article/" + article.getArticleId();
        } catch (Exception e) {
            logger.error(e.toString());
            return "redirect:/error";
        }
    }

    @GetMapping("/edit/{articleId}")
    public String editArticle(@PathVariable Integer articleId,
                              Model model) {
        Article article = new Article();
        article.setArticleId(articleId);
        article = articleService.selectArticleWithCategoryById(article);
        List<Category> categories = categoryService.selectList(null);
        model.addAttribute("categories", categories);
        model.addAttribute("article", article);
        return "article/article_edit";
    }

    @PostMapping("/update/{articleId}")
    public String updateArticle(Article article) {

        try {
            article = articleService.updateAndCache(article);
            return "redirect:/article/" + article.getArticleId();
        } catch (Exception e) {
            logger.error(e.toString());
            return "redirect:/error";
        }
    }

    @GetMapping("/delete/{articleId}")
    public String deleteArticle(@PathVariable("articleId") Integer articleId) {
        Article article = new Article();
        article.setArticleId(articleId);
        articleService.delete(article);
        return "redirect:/";
    }


    @PostMapping("/updateReadNum")
    public ResponseEntity<Map<String, Object>> updateReadNum(@CookieValue("isNewSession") String isNewSession,
                                                             Article article,
                                                             HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        if (FALSE.equals(isNewSession)) {
            map.put("isSuccess", false);
            map.put("message", "不是新的回话。");
        } else {
            article = articleService.selectAndUpdateReadNum(article);
            if (article.getReadNum() != null) {
                map.put("isSuccess", true);
                map.put("message", "阅读数量更新成功！");
                map.put("readNum", article.getReadNum() + 1);
                Cookie cookie = new Cookie("isNewSession", "false");
                response.addCookie(cookie);
            } else {
                map.put("isSuccess", false);
                map.put("message", "阅读数量更新失败！");
            }
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/hot7/{pageNum}")
    public String hot7days(@PathVariable Integer pageNum,
                           Model model) {
        Page<Article> articlePage = articleService.selectHotArticles(7, pageNum);
        List<Long> pageRange = PaginationUtil.getPageRange(pageNum, articlePage.getPages());
        model.addAttribute("page", articlePage);
        model.addAttribute("pageRange", pageRange);
        model.addAttribute("currentPage", pageNum);
        return "article/hot7";
    }

    @GetMapping("/hot30/{pageNum}")
    public String hot30days(@PathVariable Integer pageNum,
                            Model model) {
        Page<Article> articlePage = articleService.selectHotArticles(30, pageNum);
        List<Long> pageRange = PaginationUtil.getPageRange(pageNum, articlePage.getPages());
        model.addAttribute("pageRange", pageRange);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("page", articlePage);
        return "article/hot30";
    }

    @PostMapping("/likeNum/{hasLike}")
    public ResponseEntity<Map<String, Object>> updateLikeNum(Article article,
                                                             @PathVariable boolean hasLike,
                                                             Principal principal) {
        Map<String, Object> map = new HashMap<>();
        Like like = likeService.selectByArticleIdAndUsername(article.getArticleId(), principal.getName());
        if (hasLike) {
            article.setLikeNum(article.getLikeNum() - 1);
            like.setLikeStatus("inactive");
        } else {
            article.setLikeNum(article.getLikeNum() + 1);
            like.setLikeStatus("active");
        }
        articleService.updateLikeNumById(article);
        boolean updateLike = likeService.updateById(like);
        if (updateLike) {
            map.put("success", true);
            map.put("likeNum", article.getLikeNum());
        } else {
            map.put("success", false);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}

