package com.stu.gym.gymmembermanage.mapper;

import com.stu.gym.gymmembermanage.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 预约记录Mapper接口
 */
@Mapper
public interface ReservationMapper {

    /**
     * 查询所有预约记录(关联查询会员和课程信息)
     */
    List<Reservation> selectAllWithDetail();

    /**
     * 分页查询预约记录
     */
    List<Reservation> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 根据ID查询预约记录
     */
    Reservation selectById(@Param("id") Integer id);

    /**
     * 根据ID查询预约记录(关联查询)
     */
    Reservation selectByIdWithDetail(@Param("id") Integer id);

    /**
     * 根据会员ID查询预约记录
     */
    List<Reservation> selectByMemberId(@Param("memberId") Integer memberId);

    /**
     * 根据会员ID分页查询预约记录
     */
    List<Reservation> selectByMemberIdPage(@Param("memberId") Integer memberId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 根据会员ID查询预约记录(关联查询)
     */
    List<Reservation> selectByMemberIdWithDetail(@Param("memberId") Integer memberId);

    /**
     * 根据课程ID查询预约记录
     */
    List<Reservation> selectByCourseId(@Param("courseId") Integer courseId);

    /**
     * 查询会员是否已预约某课程(状态为已预约)
     */
    Reservation selectByMemberAndCourse(@Param("memberId") Integer memberId, @Param("courseId") Integer courseId);

    /**
     * 根据条件查询预约记录
     */
    List<Reservation> selectByCondition(@Param("memberId") Integer memberId, @Param("courseId") Integer courseId, @Param("status") Integer status);

    /**
     * 根据条件分页查询预约记录
     */
    List<Reservation> selectByConditionPage(@Param("memberId") Integer memberId, @Param("courseId") Integer courseId,
                                            @Param("status") Integer status, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 统计条件查询的预约记录数
     */
    int countByCondition(@Param("memberId") Integer memberId, @Param("courseId") Integer courseId, @Param("status") Integer status);

    /**
     * 统计会员预约数
     */
    int countByMemberId(@Param("memberId") Integer memberId);

    /**
     * 插入预约记录
     */
    int insert(Reservation reservation);

    /**
     * 更新预约状态
     */
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 取消预约
     */
    int cancelReservation(@Param("id") Integer id);

    /**
     * 删除预约记录
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 统计预约总数
     */
    int countAll();

    /**
     * 统计有效预约数(状态为已预约)
     */
    int countByStatus(@Param("status") Integer status);
}
