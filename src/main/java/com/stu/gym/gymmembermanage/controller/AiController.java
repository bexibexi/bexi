package com.stu.gym.gymmembermanage.controller;

import com.stu.gym.gymmembermanage.entity.AiChatMessage;
import com.stu.gym.gymmembermanage.entity.CourseRecommend;
import com.stu.gym.gymmembermanage.entity.Member;
import com.stu.gym.gymmembermanage.service.AiChatService;
import com.stu.gym.gymmembermanage.service.AiRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI功能API控制器
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiChatService aiChatService;

    @Autowired
    private AiRecommendService aiRecommendService;

    /**
     * 发送消息并获取AI回复
     */
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestParam String message, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }

        try {
            String reply = aiChatService.chat(member.getId(), message);
            result.put("code", 200);
            result.put("reply", reply);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "AI服务暂时不可用，请稍后再试");
        }

        return result;
    }

    /**
     * 获取聊天历史记录
     */
    @GetMapping("/chat/history")
    public Map<String, Object> getChatHistory(@RequestParam(defaultValue = "20") Integer limit,
                                               HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }

        List<AiChatMessage> history = aiChatService.getChatHistory(member.getId(), limit);
        result.put("code", 200);
        result.put("data", history);
        return result;
    }

    /**
     * 清空聊天记录
     */
    @PostMapping("/chat/clear")
    public Map<String, Object> clearChatHistory(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }

        boolean success = aiChatService.clearChatHistory(member.getId());
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "聊天记录已清空" : "清空失败");
        return result;
    }

    /**
     * 获取智能推荐课程
     */
    @GetMapping("/recommend")
    public Map<String, Object> getRecommend(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("memberUser");
        if (member == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }

        try {
            List<CourseRecommend> recommends = aiRecommendService.recommendCourses(member.getId());
            String reason = aiRecommendService.getRecommendReason(member.getId());
            result.put("code", 200);
            result.put("data", recommends);
            result.put("reason", reason);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "推荐服务暂时不可用，请稍后再试");
        }

        return result;
    }
}
