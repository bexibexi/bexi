package com.stu.gym.gymmembermanage.service.impl;

import com.stu.gym.gymmembermanage.entity.Admin;
import com.stu.gym.gymmembermanage.mapper.AdminMapper;
import com.stu.gym.gymmembermanage.service.AdminService;
import com.stu.gym.gymmembermanage.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员服务实现类
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        // 将密码MD5加密后验证
        String md5Password = MD5Util.md5(password);
        return adminMapper.selectByUsernameAndPassword(username, md5Password);
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminMapper.selectById(id);
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }
}
