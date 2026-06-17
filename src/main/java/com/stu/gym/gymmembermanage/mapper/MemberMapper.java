package com.stu.gym.gymmembermanage.mapper;

import com.stu.gym.gymmembermanage.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 会员Mapper接口
 */
@Mapper
public interface MemberMapper {

    /**
     * 查询所有会员
     */
    List<Member> selectAll();

    /**
     * 分页查询会员
     */
    List<Member> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 根据ID查询会员
     */
    Member selectById(@Param("id") Integer id);

    /**
     * 根据用户名查询会员
     */
    Member selectByUsername(@Param("username") String username);

    /**
     * 根据条件查询会员列表
     */
    List<Member> selectByCondition(@Param("realName") String realName, @Param("phone") String phone, @Param("status") Integer status);

    /**
     * 根据条件分页查询会员列表
     */
    List<Member> selectByConditionPage(@Param("realName") String realName, @Param("phone") String phone,
                                       @Param("status") Integer status, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 统计条件查询的会员数
     */
    int countByCondition(@Param("realName") String realName, @Param("phone") String phone, @Param("status") Integer status);

    /**
     * 验证会员登录
     */
    Member selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 插入会员
     */
    int insert(Member member);

    /**
     * 更新会员信息
     */
    int update(Member member);

    /**
     * 更新会员状态
     */
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 删除会员
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 统计会员总数
     */
    int countAll();

    /**
     * 统计正常状态会员数
     */
    int countByStatus(@Param("status") Integer status);
}
