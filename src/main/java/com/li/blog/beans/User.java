package com.li.blog.beans;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author li
 * @since 2018-09-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @javax.validation.constraints.NotBlank(message = "用户名不能为空！")
    private String username;
    @NotBlank(message = "密码不能为空！")
    private String password;
    @Email(message = "邮箱格式不正确！")
    private String email;
    private Date createdTime;

    @TableField(exist = false)
    private List<Article> articles;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
