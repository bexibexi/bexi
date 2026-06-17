package com.stu.gym.gymmembermanage.service;

import com.stu.gym.gymmembermanage.entity.Member;
import com.stu.gym.gymmembermanage.util.PageResult;
import java.util.List;

/**
 * 会员服务接口
 */
public interface MemberService {

    /**
     * 会员登录
     */
    Member login(String username, String password);

    /**
     * 会员注册
     */
    boolean register(Member member);

    /**
     * 查询所有会员
     */
    List<Member> getAllMembers();

    /**
     * 分页查询会员
     */
    PageResult<Member> getMembersByPage(int pageNum, int pageSize);

    /**
     * 根据ID查询会员
     */
    Member getMemberById(Integer id);

    /**
     * 根据用户名查询会员
     */
    Member getMemberByUsername(String username);

    /**
     * 根据条件查询会员
     */
    List<Member> searchMembers(String realName, String phone, Integer status);

    /**
     * 根据条件分页查询会员
     */
    PageResult<Member> searchMembersByPage(String realName, String phone, Integer status, int pageNum, int pageSize);

    /**
     * 添加会员
     */
    boolean addMember(Member member);

    /**
     * 更新会员信息
     */
    boolean updateMember(Member member);

    /**
     * 更新会员状态
     */
    boolean updateMemberStatus(Integer id, Integer status);

    /**
     * 删除会员
     */
    boolean deleteMember(Integer id);

    /**
     * 统计会员总数
     */
    int countAll();

    /**
     * 统计正常状态会员数
     */
    int countActive();
}
