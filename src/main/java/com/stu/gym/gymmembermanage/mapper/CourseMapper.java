package com.stu.gym.gymmembermanage.mapper;

import com.stu.gym.gymmembermanage.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper {

    /**
     * 查询所有课程
     */
    List<Course> selectAll();

    /**
     * 分页查询课程
     */
    List<Course> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 查询所有上架课程
     */
    List<Course> selectAllActive();

    /**
     * 分页查询上架课程
     */
    List<Course> selectActiveByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 根据ID查询课程
     */
    Course selectById(@Param("id") Integer id);

    /**
     * 根据条件查询课程列表
     */
    List<Course> selectByCondition(@Param("name") String name, @Param("coach") String coach, @Param("status") Integer status);

    /**
     * 根据条件分页查询课程列表
     */
    List<Course> selectByConditionPage(@Param("name") String name, @Param("coach") String coach,
                                       @Param("status") Integer status, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 统计条件查询的课程数
     */
    int countByCondition(@Param("name") String name, @Param("coach") String coach, @Param("status") Integer status);

    /**
     * 统计指定课程的有效预约数
     */
    int countValidReservations(@Param("courseId") Integer courseId);

    /**
     * 插入课程
     */
    int insert(Course course);

    /**
     * 更新课程信息
     */
    int update(Course course);

    /**
     * 更新课程状态
     */
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 删除课程
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 统计课程总数
     */
    int countAll();

    /**
     * 统计上架课程数
     */
    int countByStatus(@Param("status") Integer status);
}
