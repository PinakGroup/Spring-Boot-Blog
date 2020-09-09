package com.li.blog.dto;

import lombok.*;

/**
 * @author li
 * @version 1.0
 * @since 18-9-9 下午9:13
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ResetPasswordDto {
    private String email;
    private String newPassword;
    private String repeatPassword;
    private Integer resetPasswordValidationCode;
}
