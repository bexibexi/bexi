package com.stu.gym.gymmembermanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 健身房会员与课程预约管理系统 启动类
 */
@SpringBootApplication
@MapperScan("com.stu.gym.gymmembermanage.mapper")
public class GymMemberManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymMemberManageApplication.class, args);
        System.out.println("=============================================");
        System.out.println("   健身房会员与课程预约管理系统启动成功!");
        System.out.println("   访问地址: http://localhost:8080");
        System.out.println("=============================================");
    }
}
