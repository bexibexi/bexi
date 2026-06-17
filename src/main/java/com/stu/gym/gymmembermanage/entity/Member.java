package com.stu.gym.gymmembermanage.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 会员实体类
 */
@Data
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    private Integer id;

    /**
     * 登录用户名
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
     * 性别(0未知,1男,2女)
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 会员类型
     */
    private String memberType;

    /**
     * 状态(0禁用,1正常)
     */
    private Integer status;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 获取性别文本
     */
    public String getGenderText() {
        if (gender == null) {
            return "未知";
        }
        switch (gender) {
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "未知";
        }
    }

    /**
     * 获取状态文本
     */
    public String getStatusText() {
        if (status == null || status == 0) {
            return "禁用";
        }
        return "正常";
    }
}
