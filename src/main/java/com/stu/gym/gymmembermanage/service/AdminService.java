package com.stu.gym.gymmembermanage.service;

import com.stu.gym.gymmembermanage.entity.Admin;

/**
 * 管理员服务接口
 */
public interface AdminService {

    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码(明文)
     * @return 管理员信息，登录失败返回null
     */
    Admin login(String username, String password);

    /**
     * 根据ID查询管理员
     */
    Admin getAdminById(Integer id);

    /**
     * 根据用户名查询管理员
     */
    Admin getAdminByUsername(String username);
}
