package com.stu.gym.gymmembermanage.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理员实体类
 */
@Data
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码(MD5加密)
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 创建时间
     */
    private Date createTime;
}
