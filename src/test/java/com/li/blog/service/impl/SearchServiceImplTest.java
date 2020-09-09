package com.li.blog.service.impl;

import com.li.blog.beans.Article;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class SearchServiceImplTest {

    @Autowired
    private SearchServiceImpl searchService;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void index() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void update() {
    }

    @Test
    public void multiMatchQuery() {
    }

    @Test
    public void likeMatchQuery() {
        Page<Article> articlePage = searchService.likeMatchQuery("6666666", 1);
        System.out.println(articlePage.getClass());
        System.out.println("总页数: " + articlePage.getTotalPages() + " 总记录数: " + articlePage.getTotalElements());
        articlePage.forEach(System.out::println);
    }
}