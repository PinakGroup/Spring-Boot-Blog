package com.li.blog.repository;

import com.li.blog.beans.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository repository;


    @Test
    public void findArticlesByTitleLikeOrContentLike() {
        Page<Article> articles = repository.findArticlesByTitleLikeOrContentLike("6666", "666", null);
        System.out.println(articles.getClass());
        System.out.println(articles.getTotalPages());
        System.out.println(articles.getTotalElements());
        articles.forEach(System.out::println);
    }



}