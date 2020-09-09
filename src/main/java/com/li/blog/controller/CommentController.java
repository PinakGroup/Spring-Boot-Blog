package com.li.blog.controller;


import com.li.blog.beans.Comment;
import com.li.blog.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author li
 * @since 2018-09-28
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentServiceImpl commentService;

    @Autowired
    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addComment(Comment comment) {
        Map<String, Object> map = new HashMap<>();
        //添加评论
        comment.setCommentTime(new Date());
        comment.setLastUpdate(new Date());
        if ("".equals(comment.getCommentContent().trim())) {
            map.put("success", false);
            map.put("message", "评论内容不能为空！");
        } else {
            boolean insert = commentService.insert(comment);
            if (insert) {
                map.put("comment", comment);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("message", "服务器繁忙，请稍后重试！");
            }
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> editComment(Comment comment) {
        Map<String, Object> map = new HashMap<>();
        //更新评论
        comment.setLastUpdate(new Date());
        boolean update = commentService.updateById(comment);
        if (update) {
            map.put("comment", comment);
            map.put("success", true);
        } else {
            map.put("success", false);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}/{articleId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("commentId") Integer commentId, @PathVariable("articleId") Integer articleId) {
        Map<String, Object> map = new HashMap<>();
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setArticleId(articleId);
        //删除评论
        boolean delete = commentService.deleteById(comment);
        if (delete) {
            map.put("success", true);
            map.put("message", "删除成功！");
        } else {
            map.put("success", false);
            map.put("message", "删除失败！请稍后重试");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<Object> replyComment() {
        return null;
    }

}

