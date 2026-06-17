package com.stu.gym.gymmembermanage.service;

import com.stu.gym.gymmembermanage.entity.CourseRecommend;
import java.util.List;

/**
 * 智能推荐服务接口
 */
public interface AiRecommendService {

    /**
     * 为会员推荐课程
     *
     * @param memberId 会员ID
     * @return 推荐课程列表
     */
    List<CourseRecommend> recommendCourses(Integer memberId);

    /**
     * 获取推荐理由
     *
     * @param memberId 会员ID
     * @return 推荐理由文本
     */
    String getRecommendReason(Integer memberId);
}
