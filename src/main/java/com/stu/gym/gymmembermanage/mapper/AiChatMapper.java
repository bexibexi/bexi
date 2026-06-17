package com.stu.gym.gymmembermanage.mapper;

import com.stu.gym.gymmembermanage.entity.AiChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * AI聊天记录Mapper接口
 */
@Mapper
public interface AiChatMapper {

    /**
     * 插入聊天记录
     */
    int insert(AiChatMessage message);

    /**
     * 根据会员ID查询最近的聊天记录
     */
    List<AiChatMessage> selectRecentByMemberId(@Param("memberId") Integer memberId, @Param("limit") Integer limit);

    /**
     * 根据会员ID删除所有聊天记录
     */
    int deleteByMemberId(@Param("memberId") Integer memberId);

    /**
     * 统计会员聊天记录数
     */
    int countByMemberId(@Param("memberId") Integer memberId);
}
