package com.stu.gym.gymmembermanage.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 预约记录实体类
 */
@Data
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    private Integer id;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 课程ID
     */
    private Integer courseId;

    /**
     * 状态(0已取消,1已预约,2已完成)
     */
    private Integer status;

    /**
     * 预约时间
     */
    private Date createTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    // ========== 关联查询字段 ==========

    /**
     * 会员姓名(关联查询)
     */
    private String memberName;

    /**
     * 会员用户名(关联查询)
     */
    private String memberUsername;

    /**
     * 课程名称(关联查询)
     */
    private String courseName;

    /**
     * 教练姓名(关联查询)
     */
    private String courseCoach;

    /**
     * 上课时间(关联查询)
     */
    private Date courseScheduleTime;

    /**
     * 上课地点(关联查询)
     */
    private String courseLocation;

    /**
     * 获取状态文本
     */
    public String getStatusText() {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "已取消";
            case 1:
                return "已预约";
            case 2:
                return "已完成";
            default:
                return "未知";
        }
    }

    /**
     * 获取状态样式类
     */
    public String getStatusClass() {
        if (status == null) {
            return "secondary";
        }
        switch (status) {
            case 0:
                return "danger";
            case 1:
                return "success";
            case 2:
                return "info";
            default:
                return "secondary";
        }
    }
}
