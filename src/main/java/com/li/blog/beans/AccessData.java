package com.li.blog.beans;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 访问统计
 * </p>
 *
 * @author li
 * @since 2018-09-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_access_data")
public class AccessData extends Model<AccessData> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "access_id", type = IdType.AUTO)
    private Integer accessId;
    private Integer articleId;
    private Integer accessNum;
    private Date accessDate;
    @Override
    protected Serializable pkVal() {
        return this.accessId;
    }

}
