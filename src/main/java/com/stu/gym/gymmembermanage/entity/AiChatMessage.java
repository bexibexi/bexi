package com.stu.gym.gymmembermanage.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * AI聊天消息实体类
 */
@Data
public class AiChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Integer id;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 角色(user/assistant)
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 角色常量
     */
    public static final String ROLE_USER = "user";
    public static final String ROLE_ASSISTANT = "assistant";
}
