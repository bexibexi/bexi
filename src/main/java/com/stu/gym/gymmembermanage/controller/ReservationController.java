package com.stu.gym.gymmembermanage.controller;

import com.stu.gym.gymmembermanage.entity.Member;
import com.stu.gym.gymmembermanage.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 预约API控制器(提供AJAX接口)
 */
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * 预约课程
     */
    @PostMapping("/make")
    public Map<String, Object> makeReservation(@RequestParam Integer courseId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }
        String message = reservationService.makeReservation(member.getId(), courseId);
        if ("预约成功".equals(message)) {
            result.put("code", 200);
        } else {
            result.put("code", 400);
        }
        result.put("message", message);
        return result;
    }

    /**
     * 取消预约
     */
    @PostMapping("/cancel")
    public Map<String, Object> cancelReservation(@RequestParam Integer reservationId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }
        String message = reservationService.cancelReservation(reservationId, member.getId());
        if ("取消成功".equals(message)) {
            result.put("code", 200);
        } else {
            result.put("code", 400);
        }
        result.put("message", message);
        return result;
    }
}
