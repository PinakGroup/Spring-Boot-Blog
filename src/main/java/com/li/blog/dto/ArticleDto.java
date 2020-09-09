package com.li.blog.dto;

import lombok.*;

/**
 * @author li
 * @version 1.0
 * @since 18-9-10 下午7:49
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ArticleDto {
    private String title;
    private String content;
}
