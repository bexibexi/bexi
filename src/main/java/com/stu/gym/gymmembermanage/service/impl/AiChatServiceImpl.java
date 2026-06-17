package com.stu.gym.gymmembermanage.service.impl;

import com.stu.gym.gymmembermanage.entity.AiChatMessage;
import com.stu.gym.gymmembermanage.entity.Course;
import com.stu.gym.gymmembermanage.entity.Member;
import com.stu.gym.gymmembermanage.entity.Reservation;
import com.stu.gym.gymmembermanage.mapper.AiChatMapper;
import com.stu.gym.gymmembermanage.mapper.CourseMapper;
import com.stu.gym.gymmembermanage.mapper.MemberMapper;
import com.stu.gym.gymmembermanage.mapper.ReservationMapper;
import com.stu.gym.gymmembermanage.service.AiChatService;
import com.stu.gym.gymmembermanage.util.AiApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 智能客服服务实现类
 */
@Service
public class AiChatServiceImpl implements AiChatService {

    @Autowired
    private AiApiUtil aiApiUtil;

    @Autowired
    private AiChatMapper aiChatMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    /**
     * 系统提示词
     */
    private static final String SYSTEM_PROMPT =
        "你是健身房智能客服助手，名叫'小健'。你的职责是帮助会员了解课程信息、预约规则、会员权益等。" +
        "请用友好、专业、简洁的语气回答问题。回答时使用中文。" +
        "如果用户询问预约课程，可以告诉他们如何操作。" +
        "如果用户询问课程信息，请根据提供的课程列表回答。" +
        "如果问题超出你的能力范围，请建议用户联系人工客服。";

    @Override
    public String chat(Integer memberId, String message) {
        // 1. 查询会员信息
        Member member = memberMapper.selectById(memberId);
        String memberName = member != null ? member.getRealName() : "会员";

        // 2. 查询课程信息作为上下文
        List<Course> courses = courseMapper.selectAllActive();
        String courseContext = buildCourseContext(courses);

        // 3. 查询会员的预约记录
        List<Reservation> reservations = reservationMapper.selectByMemberIdWithDetail(memberId);
        String reservationContext = buildReservationContext(reservations);

        // 4. 构建完整上下文
        String fullContext = "\n\n当前会员信息：姓名=" + memberName +
                "\n\n当前可预约课程：\n" + courseContext +
                "\n\n会员预约记录：\n" + reservationContext;

        // 5. 查询历史聊天记录
        List<AiChatMessage> history = aiChatMapper.selectRecentByMemberId(memberId, 10);
        List<Map<String, String>> chatHistory = new ArrayList<>();
        for (AiChatMessage msg : history) {
            Map<String, String> msgMap = new HashMap<>();
            msgMap.put("role", msg.getRole());
            msgMap.put("content", msg.getContent());
            chatHistory.add(msgMap);
        }

        // 6. 保存用户消息
        AiChatMessage userMessage = new AiChatMessage();
        userMessage.setMemberId(memberId);
        userMessage.setRole(AiChatMessage.ROLE_USER);
        userMessage.setContent(message);
        aiChatMapper.insert(userMessage);

        // 7. 调用AI获取回复
        String aiReply = aiApiUtil.chat(SYSTEM_PROMPT + fullContext, chatHistory, message);

        // 8. 保存AI回复
        AiChatMessage aiMessage = new AiChatMessage();
        aiMessage.setMemberId(memberId);
        aiMessage.setRole(AiChatMessage.ROLE_ASSISTANT);
        aiMessage.setContent(aiReply);
        aiChatMapper.insert(aiMessage);

        return aiReply;
    }

    @Override
    public List<AiChatMessage> getChatHistory(Integer memberId, Integer limit) {
        List<AiChatMessage> history = aiChatMapper.selectRecentByMemberId(memberId, limit);
        // 反转列表，使其按时间正序
        Collections.reverse(history);
        return history;
    }

    @Override
    public boolean clearChatHistory(Integer memberId) {
        return aiChatMapper.deleteByMemberId(memberId) > 0;
    }

    /**
     * 构建课程上下文信息
     */
    private String buildCourseContext(List<Course> courses) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        for (Course c : courses) {
            sb.append("- ").append(c.getName())
              .append("，教练：").append(c.getCoach())
              .append("，时间：").append(c.getScheduleTime() != null ? sdf.format(c.getScheduleTime()) : "待定")
              .append("，地点：").append(c.getLocation())
              .append("，剩余名额：").append(c.getRemainingSlots())
              .append("/").append(c.getMaxCapacity())
              .append("\n");
        }
        return sb.toString();
    }

    /**
     * 构建预约记录上下文信息
     */
    private String buildReservationContext(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            return "暂无预约记录";
        }
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int count = 0;
        for (Reservation r : reservations) {
            if (count >= 5) break; // 只取最近5条
            sb.append("- ").append(r.getCourseName())
              .append("，状态：").append(r.getStatusText())
              .append("，预约时间：").append(r.getCreateTime() != null ? sdf.format(r.getCreateTime()) : "")
              .append("\n");
            count++;
        }
        return sb.toString();
    }
}
