package com.stu.gym.gymmembermanage.mapper;

import com.stu.gym.gymmembermanage.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员Mapper接口
 */
@Mapper
public interface AdminMapper {

    /**
     * 根据用户名查询管理员
     */
    Admin selectByUsername(@Param("username") String username);

    /**
     * 根据ID查询管理员
     */
    Admin selectById(@Param("id") Integer id);

    /**
     * 验证管理员登录
     */
    Admin selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
