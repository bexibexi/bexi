package com.stu.gym.gymmembermanage.controller;

import com.stu.gym.gymmembermanage.entity.Course;
import com.stu.gym.gymmembermanage.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程API控制器(提供AJAX接口)
 */
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取所有上架课程
     */
    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<Course> courses = courseService.getAllActiveCourses();
        result.put("code", 200);
        result.put("data", courses);
        return result;
    }

    /**
     * 获取课程详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        Course course = courseService.getCourseById(id);
        if (course != null) {
            result.put("code", 200);
            result.put("data", course);
        } else {
            result.put("code", 404);
            result.put("message", "课程不存在");
        }
        return result;
    }
}
