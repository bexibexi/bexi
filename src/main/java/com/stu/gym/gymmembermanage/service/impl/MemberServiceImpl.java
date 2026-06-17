package com.stu.gym.gymmembermanage.service.impl;

import com.stu.gym.gymmembermanage.entity.Member;
import com.stu.gym.gymmembermanage.mapper.MemberMapper;
import com.stu.gym.gymmembermanage.service.MemberService;
import com.stu.gym.gymmembermanage.util.MD5Util;
import com.stu.gym.gymmembermanage.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员服务实现类
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        String md5Password = MD5Util.md5(password);
        return memberMapper.selectByUsernameAndPassword(username, md5Password);
    }

    @Override
    public boolean register(Member member) {
        if (member == null || member.getUsername() == null || member.getPassword() == null) {
            return false;
        }
        Member existing = memberMapper.selectByUsername(member.getUsername());
        if (existing != null) {
            return false;
        }
        member.setPassword(MD5Util.md5(member.getPassword()));
        if (member.getStatus() == null) {
            member.setStatus(1);
        }
        if (member.getMemberType() == null) {
            member.setMemberType("普通会员");
        }
        return memberMapper.insert(member) > 0;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberMapper.selectAll();
    }

    @Override
    public PageResult<Member> getMembersByPage(int pageNum, int pageSize) {
        int totalCount = memberMapper.countAll();
        int offset = (pageNum - 1) * pageSize;
        List<Member> list = memberMapper.selectByPage(offset, pageSize);
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }

    @Override
    public Member getMemberById(Integer id) {
        return memberMapper.selectById(id);
    }

    @Override
    public Member getMemberByUsername(String username) {
        return memberMapper.selectByUsername(username);
    }

    @Override
    public List<Member> searchMembers(String realName, String phone, Integer status) {
        return memberMapper.selectByCondition(realName, phone, status);
    }

    @Override
    public PageResult<Member> searchMembersByPage(String realName, String phone, Integer status, int pageNum, int pageSize) {
        int totalCount = memberMapper.countByCondition(realName, phone, status);
        int offset = (pageNum - 1) * pageSize;
        List<Member> list = memberMapper.selectByConditionPage(realName, phone, status, offset, pageSize);
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }

    @Override
    public boolean addMember(Member member) {
        if (member == null || member.getUsername() == null || member.getPassword() == null) {
            return false;
        }
        Member existing = memberMapper.selectByUsername(member.getUsername());
        if (existing != null) {
            return false;
        }
        member.setPassword(MD5Util.md5(member.getPassword()));
        if (member.getStatus() == null) {
            member.setStatus(1);
        }
        return memberMapper.insert(member) > 0;
    }

    @Override
    public boolean updateMember(Member member) {
        if (member == null || member.getId() == null) {
            return false;
        }
        if (member.getPassword() != null && !member.getPassword().isEmpty()) {
            member.setPassword(MD5Util.md5(member.getPassword()));
        }
        return memberMapper.update(member) > 0;
    }

    @Override
    public boolean updateMemberStatus(Integer id, Integer status) {
        if (id == null || status == null) {
            return false;
        }
        return memberMapper.updateStatus(id, status) > 0;
    }

    @Override
    public boolean deleteMember(Integer id) {
        if (id == null) {
            return false;
        }
        return memberMapper.deleteById(id) > 0;
    }

    @Override
    public int countAll() {
        return memberMapper.countAll();
    }

    @Override
    public int countActive() {
        return memberMapper.countByStatus(1);
    }
}
