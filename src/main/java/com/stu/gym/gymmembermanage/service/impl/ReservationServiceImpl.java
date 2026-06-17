package com.stu.gym.gymmembermanage.service.impl;

import com.stu.gym.gymmembermanage.entity.Course;
import com.stu.gym.gymmembermanage.entity.Reservation;
import com.stu.gym.gymmembermanage.mapper.CourseMapper;
import com.stu.gym.gymmembermanage.mapper.ReservationMapper;
import com.stu.gym.gymmembermanage.service.ReservationService;
import com.stu.gym.gymmembermanage.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 预约服务实现类
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Reservation> getAllReservations() {
        return reservationMapper.selectAllWithDetail();
    }

    @Override
    public PageResult<Reservation> getReservationsByPage(int pageNum, int pageSize) {
        int totalCount = reservationMapper.countAll();
        int offset = (pageNum - 1) * pageSize;
        List<Reservation> list = reservationMapper.selectByPage(offset, pageSize);
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }

    @Override
    public Reservation getReservationById(Integer id) {
        return reservationMapper.selectByIdWithDetail(id);
    }

    @Override
    public List<Reservation> getReservationsByMemberId(Integer memberId) {
        return reservationMapper.selectByMemberIdWithDetail(memberId);
    }

    @Override
    public PageResult<Reservation> getReservationsByMemberIdPage(Integer memberId, int pageNum, int pageSize) {
        int totalCount = reservationMapper.countByMemberId(memberId);
        int offset = (pageNum - 1) * pageSize;
        List<Reservation> list = reservationMapper.selectByMemberIdPage(memberId, offset, pageSize);
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }

    @Override
    public List<Reservation> searchReservations(Integer memberId, Integer courseId, Integer status) {
        return reservationMapper.selectByCondition(memberId, courseId, status);
    }

    @Override
    public PageResult<Reservation> searchReservationsByPage(Integer memberId, Integer courseId, Integer status, int pageNum, int pageSize) {
        int totalCount = reservationMapper.countByCondition(memberId, courseId, status);
        int offset = (pageNum - 1) * pageSize;
        List<Reservation> list = reservationMapper.selectByConditionPage(memberId, courseId, status, offset, pageSize);
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }

    @Override
    @Transactional
    public String makeReservation(Integer memberId, Integer courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            return "课程不存在";
        }
        if (course.getStatus() == null || course.getStatus() == 0) {
            return "该课程已下架";
        }
        int currentCount = courseMapper.countValidReservations(courseId);
        if (currentCount >= course.getMaxCapacity()) {
            return "该课程名额已满";
        }
        Reservation existing = reservationMapper.selectByMemberAndCourse(memberId, courseId);
        if (existing != null) {
            return "您已预约该课程，请勿重复预约";
        }
        Reservation reservation = new Reservation();
        reservation.setMemberId(memberId);
        reservation.setCourseId(courseId);
        reservation.setStatus(1);
        int result = reservationMapper.insert(reservation);
        if (result <= 0) {
            return "预约失败，请稍后重试";
        }
        return "预约成功";
    }

    @Override
    @Transactional
    public String cancelReservation(Integer reservationId, Integer memberId) {
        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null) {
            return "预约记录不存在";
        }
        if (!reservation.getMemberId().equals(memberId)) {
            return "无权取消他人的预约";
        }
        if (reservation.getStatus() == null || reservation.getStatus() != 1) {
            return "该预约无法取消";
        }
        int result = reservationMapper.cancelReservation(reservationId);
        if (result <= 0) {
            return "取消失败，请稍后重试";
        }
        return "取消成功";
    }

    @Override
    @Transactional
    public boolean adminCancelReservation(Integer reservationId) {
        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null || reservation.getStatus() != 1) {
            return false;
        }
        int result = reservationMapper.cancelReservation(reservationId);
        return result > 0;
    }

    @Override
    public boolean deleteReservation(Integer id) {
        if (id == null) {
            return false;
        }
        return reservationMapper.deleteById(id) > 0;
    }

    @Override
    public int countAll() {
        return reservationMapper.countAll();
    }

    @Override
    public int countActive() {
        return reservationMapper.countByStatus(1);
    }
}
