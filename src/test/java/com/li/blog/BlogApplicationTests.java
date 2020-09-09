package com.li.blog;

import com.li.blog.beans.*;
import com.li.blog.mapper.UserMapper;
import com.li.blog.service.ArticleService;
import com.li.blog.service.impl.LikeServiceImpl;
import com.li.blog.utils.BeanUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest()
public class BlogApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SqlSessionFactory factory;

    @Autowired
    private LikeServiceImpl likeService;

    @Test
    public void insert() {
        User user = new User();
        user.setUsername("tom");
        user.setPassword("tompwd");
        user.setEmail("tom@qq.com");
        int i = userMapper.insert(user);
        System.out.println(i);
    }

    @Test
    public void getCode() {
        Random random = new Random();
        double num = random.nextDouble();
        System.out.println(num * 1000000);
    }

    @Test
    public void cache() {

        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectByUsername("li");
        User user1 = mapper.selectByUsername("li");
        System.out.println(user);
        System.out.println(user1);
    }

    @Test
    public void date() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date date = calendar.getTime();
        System.out.println(date);
    }

    @Test
    public void update() {
        Like like = likeService.selectByArticleIdAndUsername(6, "li");
        System.out.println(like);
        like.setLikeStatus("inactive");
        boolean b = likeService.updateById(like);
        System.out.println(b);
    }


    @Test
    public void reflection() {
        Article article = new Article();
        article.setArticleId(1);
        article.setLikeNum(2);
        article.setReadNum(3);
        article.setTitle("主题");
        Category category = new Category();
        category.setCategoryName("spring");
        article.setCategory(category);

        Article repo = new Article();
        repo.setCategory(new Category());
        BeanUtil.transferValue(article, repo, new ArrayList<>());
        System.out.println(repo);

    }



}
