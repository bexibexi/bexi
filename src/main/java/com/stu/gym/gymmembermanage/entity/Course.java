package com.stu.gym.gymmembermanage.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 课程实体类
 */
@Data
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Integer id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 教练姓名
     */
    private String coach;

    /**
     * 最大容纳人数
     */
    private Integer maxCapacity;

    /**
     * 当前预约人数
     */
    private Integer currentCount;

    /**
     * 课程时长(分钟)
     */
    private Integer duration;

    /**
     * 上课地点
     */
    private String location;

    /**
     * 上课时间
     */
    private Date scheduleTime;

    /**
     * 状态(0下架,1正常)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 获取状态文本
     */
    public String getStatusText() {
        if (status == null || status == 0) {
            return "已下架";
        }
        return "正常";
    }

    /**
     * 检查是否已满
     */
    public boolean isFull() {
        if (currentCount == null || maxCapacity == null) {
            return false;
        }
        return currentCount >= maxCapacity;
    }

    /**
     * 获取剩余名额
     */
    public int getRemainingSlots() {
        if (currentCount == null || maxCapacity == null) {
            return 0;
        }
        return maxCapacity - currentCount;
    }
}
