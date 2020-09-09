package com.li.blog.dto;

import lombok.*;

/**
 * @author li
 * @version 1.0
 * @since 18-9-9 下午7:13
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegisterDto {
    private String username;
    private String password;
    private String repeatPassword;
    private String email;
    private Integer validationCode;

}
