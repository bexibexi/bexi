package com.stu.gym.gymmembermanage.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 课程推荐结果实体类
 */
@Data
public class CourseRecommend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Integer courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 教练姓名
     */
    private String coach;

    /**
     * 上课时间
     */
    private String scheduleTime;

    /**
     * 剩余名额
     */
    private Integer remainingSlots;

    /**
     * 推荐理由
     */
    private String recommendReason;

    /**
     * 推荐分数（0-100）
     */
    private Integer recommendScore;
}
