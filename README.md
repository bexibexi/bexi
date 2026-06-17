# 健身房会员与课程预约管理系统

## 项目简介

这是一个基于 SpringBoot 的前后端一体化健身房管理平台，支持管理员和会员两种角色，实现会员管理、课程管理、课程预约等核心功能。

## 技术栈

- **后端框架**: SpringBoot 2.6.13
- **持久层**: MyBatis 2.2.2
- **数据库**: MySQL 5.5
- **模板引擎**: Thymeleaf
- **前端框架**: Bootstrap 5.1.3
- **图标库**: Font Awesome 6.0.0
- **工具库**: Lombok

## 功能模块

### 管理员功能
- 管理员登录
- 会员信息管理（增删改查）
- 课程管理（增删改查）
- 预约记录查看和管理
- 数据统计

### 会员功能
- 会员注册
- 会员登录
- 课程浏览
- 课程预约/取消预约
- 我的预约查看
- 个人中心（修改个人信息）

## 项目结构

```
gym-member-manage/
├── pom.xml                                          # Maven 配置文件
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/stu/gym/gymmembermanage/
│   │   │       ├── GymMemberManageApplication.java  # 启动类
│   │   │       ├── config/                          # 配置类
│   │   │       ├── controller/                      # 控制器层
│   │   │       ├── entity/                          # 实体类
│   │   │       ├── mapper/                          # MyBatis Mapper接口
│   │   │       ├── service/                         # 服务层
│   │   │       ├── interceptor/                     # 拦截器
│   │   │       └── util/                            # 工具类
│   │   └── resources/
│   │       ├── application.yml                      # 应用配置文件
│   │       ├── mapper/                              # MyBatis XML映射文件
│   │       ├── static/                              # 静态资源
│   │       └── templates/                           # Thymeleaf模板
│   └── sql/
│       └── gym_db.sql                               # 数据库脚本
└── README.md
```

## 快速开始

### 1. 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.5+

### 2. 数据库配置

1. 创建数据库并导入数据：

```bash
mysql -u root -p < src/main/sql/gym_db.sql
```

2. 修改数据库配置（`src/main/resources/application.yml`）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gym_db?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: 123456
```

### 3. 运行项目

方式一：使用 Maven 命令
```bash
mvn spring-boot:run
```

方式二：打包后运行
```bash
mvn clean package
java -jar target/gym-member-manage-1.0.0.jar
```

### 4. 访问系统

- 访问地址：http://localhost:8080
- 管理员登录：
  - 用户名：admin
  - 密码：123456
- 会员登录：
  - 用户名：zhangsan
  - 密码：123456

## 测试账号

### 管理员账号
| 用户名 | 密码 |
|--------|------|
| admin | 123456 |
| admin01 | 123456 |

### 会员账号
| 用户名 | 密码 | 姓名 |
|--------|------|------|
| zhangsan | 123456 | 张三 |
| lisi | 123456 | 李四 |
| wangwu | 123456 | 王五 |

## 注意事项

1. 首次运行前请确保已创建数据库并导入数据
2. 默认端口为 8080，可在 application.yml 中修改
3. 密码使用 MD5 加密存储
4. 会员预约课程时会检查课程容量

## 许可证

MIT License
