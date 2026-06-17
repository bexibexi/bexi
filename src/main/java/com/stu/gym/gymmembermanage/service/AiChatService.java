package com.stu.gym.gymmembermanage.service;

import com.stu.gym.gymmembermanage.entity.AiChatMessage;
import java.util.List;

/**
 * 智能客服服务接口
 */
public interface AiChatService {

    /**
     * 发送消息并获取AI回复
     *
     * @param memberId 会员ID
     * @param message  用户消息
     * @return AI回复内容
     */
    String chat(Integer memberId, String message);

    /**
     * 获取聊天历史记录
     *
     * @param memberId 会员ID
     * @param limit    获取条数
     * @return 聊天记录列表
     */
    List<AiChatMessage> getChatHistory(Integer memberId, Integer limit);

    /**
     * 清空聊天记录
     *
     * @param memberId 会员ID
     * @return 是否成功
     */
    boolean clearChatHistory(Integer memberId);
}
