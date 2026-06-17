package com.stu.gym.gymmembermanage.service;

import com.stu.gym.gymmembermanage.entity.Course;
import com.stu.gym.gymmembermanage.util.PageResult;
import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService {

    /**
     * 查询所有课程
     */
    List<Course> getAllCourses();

    /**
     * 分页查询课程
     */
    PageResult<Course> getCoursesByPage(int pageNum, int pageSize);

    /**
     * 查询所有上架课程
     */
    List<Course> getAllActiveCourses();

    /**
     * 分页查询上架课程
     */
    PageResult<Course> getActiveCoursesByPage(int pageNum, int pageSize);

    /**
     * 根据ID查询课程
     */
    Course getCourseById(Integer id);

    /**
     * 根据条件查询课程
     */
    List<Course> searchCourses(String name, String coach, Integer status);

    /**
     * 根据条件分页查询课程
     */
    PageResult<Course> searchCoursesByPage(String name, String coach, Integer status, int pageNum, int pageSize);

    /**
     * 统计指定课程的有效预约数
     */
    int countValidReservations(Integer courseId);

    /**
     * 添加课程
     */
    boolean addCourse(Course course);

    /**
     * 更新课程信息
     */
    boolean updateCourse(Course course);

    /**
     * 更新课程状态
     */
    boolean updateCourseStatus(Integer id, Integer status);

    /**
     * 删除课程
     */
    boolean deleteCourse(Integer id);

    /**
     * 统计课程总数
     */
    int countAll();

    /**
     * 统计上架课程数
     */
    int countActive();
}
