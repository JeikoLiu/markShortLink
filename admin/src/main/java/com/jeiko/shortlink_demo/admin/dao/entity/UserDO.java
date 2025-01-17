package com.jeiko.shortlink_demo.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jeiko.shortlink_demo.admin.common.database.BaseDO;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName t_user
 */
@Data
@TableName("t_user")
public class UserDO extends BaseDO implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间戳
     */
    private Long deletionTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}