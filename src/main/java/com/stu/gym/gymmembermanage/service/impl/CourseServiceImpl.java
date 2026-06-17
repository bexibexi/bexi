package com.stu.gym.gymmembermanage.service.impl;

import com.stu.gym.gymmembermanage.entity.Course;
import com.stu.gym.gymmembermanage.mapper.CourseMapper;
import com.stu.gym.gymmembermanage.service.CourseService;
import com.stu.gym.gymmembermanage.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程服务实现类
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.selectAll();
    }

    @Override
    public PageResult<Course> getCoursesByPage(int pageNum, int pageSize) {
        int totalCount = courseMapper.countAll();
        int offset = (pageNum - 1) * pageSize;
        List<Course> list = courseMapper.selectByPage(offset, pageSize);
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }

    @Override
    public List<Course> getAllActiveCourses() {
        return courseMapper.selectAllActive();
    }

    @Override
    public PageResult<Course> getActiveCoursesByPage(int pageNum, int pageSize) {
        int totalCount = courseMapper.countByStatus(1);
        int offset = (pageNum - 1) * pageSize;
        List<Course> list = courseMapper.selectActiveByPage(offset, pageSize);
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseMapper.selectById(id);
    }

    @Override
    public List<Course> searchCourses(String name, String coach, Integer status) {
        return courseMapper.selectByCondition(name, coach, status);
    }

    @Override
    public PageResult<Course> searchCoursesByPage(String name, String coach, Integer status, int pageNum, int pageSize) {
        int totalCount = courseMapper.countByCondition(name, coach, status);
        int offset = (pageNum - 1) * pageSize;
        List<Course> list = courseMapper.selectByConditionPage(name, coach, status, offset, pageSize);
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }

    @Override
    public int countValidReservations(Integer courseId) {
        if (courseId == null) {
            return 0;
        }
        return courseMapper.countValidReservations(courseId);
    }

    @Override
    public boolean addCourse(Course course) {
        if (course == null || course.getName() == null) {
            return false;
        }
        if (course.getStatus() == null) {
            course.setStatus(1);
        }
        return courseMapper.insert(course) > 0;
    }

    @Override
    public boolean updateCourse(Course course) {
        if (course == null || course.getId() == null) {
            return false;
        }
        return courseMapper.update(course) > 0;
    }

    @Override
    public boolean updateCourseStatus(Integer id, Integer status) {
        if (id == null || status == null) {
            return false;
        }
        return courseMapper.updateStatus(id, status) > 0;
    }

    @Override
    public boolean deleteCourse(Integer id) {
        if (id == null) {
            return false;
        }
        return courseMapper.deleteById(id) > 0;
    }

    @Override
    public int countAll() {
        return courseMapper.countAll();
    }

    @Override
    public int countActive() {
        return courseMapper.countByStatus(1);
    }
}
