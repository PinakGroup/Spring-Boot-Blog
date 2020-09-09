package com.li.blog.beans;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.*;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文章类型
 * </p>
 *
 * @author li
 * @since 2018-09-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_article")
@Document(indexName = "springboot-blog-mp", type = "article", shards = 1, replicas = 0, refreshInterval = "-1")
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;
    @NotBlank
    private String title;
    private String summary;
    @NotBlank
    private String content;
    /**
     * 作者名
     */
    private String author;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private Date lastUpdate;
    /**
     *  阅读统计
     */
    private Integer readNum;
    /**
     * 乐观锁
     */
    @Version
    private Integer version;
    /**
     *  逻辑删除
     */
    @TableLogic
    private Integer logicFlag;
    private Integer categoryId;
    /**
     * 点赞数量
     */
    private Integer likeNum;

    @TableField(exist = false)
    private Category category;

    /**
     * 文章的所有评论
     */
    @TableField(exist = false)
    private List<Comment> comments;


    @Override
    protected Serializable pkVal() {
        return this.articleId;
    }

}
