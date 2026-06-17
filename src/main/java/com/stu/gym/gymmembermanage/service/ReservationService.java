package com.stu.gym.gymmembermanage.service;

import com.stu.gym.gymmembermanage.entity.Reservation;
import com.stu.gym.gymmembermanage.util.PageResult;
import java.util.List;

/**
 * 预约服务接口
 */
public interface ReservationService {

    /**
     * 查询所有预约记录
     */
    List<Reservation> getAllReservations();

    /**
     * 分页查询预约记录
     */
    PageResult<Reservation> getReservationsByPage(int pageNum, int pageSize);

    /**
     * 根据ID查询预约记录
     */
    Reservation getReservationById(Integer id);

    /**
     * 根据会员ID查询预约记录
     */
    List<Reservation> getReservationsByMemberId(Integer memberId);

    /**
     * 根据会员ID分页查询预约记录
     */
    PageResult<Reservation> getReservationsByMemberIdPage(Integer memberId, int pageNum, int pageSize);

    /**
     * 根据条件查询预约记录
     */
    List<Reservation> searchReservations(Integer memberId, Integer courseId, Integer status);

    /**
     * 根据条件分页查询预约记录
     */
    PageResult<Reservation> searchReservationsByPage(Integer memberId, Integer courseId, Integer status, int pageNum, int pageSize);

    /**
     * 预约课程
     */
    String makeReservation(Integer memberId, Integer courseId);

    /**
     * 取消预约
     */
    String cancelReservation(Integer reservationId, Integer memberId);

    /**
     * 管理员取消预约
     */
    boolean adminCancelReservation(Integer reservationId);

    /**
     * 删除预约记录
     */
    boolean deleteReservation(Integer id);

    /**
     * 统计预约总数
     */
    int countAll();

    /**
     * 统计有效预约数
     */
    int countActive();
}
