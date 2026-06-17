package com.stu.gym.gymmembermanage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stu.gym.gymmembermanage.entity.Course;
import com.stu.gym.gymmembermanage.entity.CourseRecommend;
import com.stu.gym.gymmembermanage.entity.Reservation;
import com.stu.gym.gymmembermanage.mapper.CourseMapper;
import com.stu.gym.gymmembermanage.mapper.ReservationMapper;
import com.stu.gym.gymmembermanage.service.AiRecommendService;
import com.stu.gym.gymmembermanage.util.AiApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能推荐服务实现类
 */
@Service
public class AiRecommendServiceImpl implements AiRecommendService {

    @Autowired
    private AiApiUtil aiApiUtil;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public List<CourseRecommend> recommendCourses(Integer memberId) {
        // 1. 查询会员的历史预约记录
        List<Reservation> history = reservationMapper.selectByMemberIdWithDetail(memberId);

        // 2. 查询所有可预约课程
        List<Course> allCourses = courseMapper.selectAllActive();

        // 3. 构建AI提示词
        String prompt = buildRecommendPrompt(history, allCourses);

        // 4. 调用AI获取推荐结果
        String aiResponse = aiApiUtil.simpleChat(
            "你是一个健身课程推荐专家。请根据用户的历史预约记录，从可选课程中推荐最合适的课程。" +
            "请严格按照JSON格式返回结果，不要添加任何其他文字。",
            prompt
        );

        // 5. 解析AI返回的推荐结果
        List<CourseRecommend> recommends = parseRecommendResult(aiResponse, allCourses);

        // 如果AI返回结果解析失败，返回默认推荐
        if (recommends.isEmpty()) {
            recommends = getDefaultRecommend(allCourses);
        }

        return recommends;
    }

    @Override
    public String getRecommendReason(Integer memberId) {
        // 查询会员的历史预约记录
        List<Reservation> history = reservationMapper.selectByMemberIdWithDetail(memberId);

        if (history.isEmpty()) {
            return "您还没有预约记录，快去浏览课程开始您的健身之旅吧！";
        }

        // 构建提示词
        StringBuilder prompt = new StringBuilder();
        prompt.append("根据以下用户预约记录，用一句话总结用户的健身偏好，并给出一句鼓励的话：\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Reservation r : history) {
            prompt.append("- ").append(r.getCourseName())
                  .append("（").append(r.getCourseCoach()).append("）")
                  .append("，状态：").append(r.getStatusText()).append("\n");
        }

        return aiApiUtil.simpleChat("你是一个健身教练，请用友好、鼓励的语气回答。", prompt.toString());
    }

    /**
     * 构建推荐提示词
     */
    private String buildRecommendPrompt(List<Reservation> history, List<Course> allCourses) {
        StringBuilder sb = new StringBuilder();
        sb.append("请根据用户的历史预约记录，从可选课程中推荐3个最适合的课程。\n\n");

        sb.append("用户历史预约记录：\n");
        if (history.isEmpty()) {
            sb.append("暂无预约记录（新用户）\n");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Reservation r : history) {
                sb.append("- ").append(r.getCourseName())
                  .append("，教练：").append(r.getCourseCoach())
                  .append("，状态：").append(r.getStatusText()).append("\n");
            }
        }

        sb.append("\n可选课程列表：\n");
        for (Course c : allCourses) {
            sb.append("- 课程ID:").append(c.getId())
              .append("，名称：").append(c.getName())
              .append("，教练：").append(c.getCoach())
              .append("，剩余名额：").append(c.getRemainingSlots())
              .append("，描述：").append(c.getDescription() != null ? c.getDescription().substring(0, Math.min(50, c.getDescription().length())) : "")
              .append("\n");
        }

        sb.append("\n请以JSON数组格式返回推荐结果，每个推荐包含以下字段：\n");
        sb.append("[{\"courseId\": 课程ID, \"reason\": \"推荐理由\", \"score\": 推荐分数(0-100)}]\n");
        sb.append("只返回JSON数组，不要添加任何其他文字。");

        return sb.toString();
    }

    /**
     * 解析AI返回的推荐结果
     */
    private List<CourseRecommend> parseRecommendResult(String aiResponse, List<Course> allCourses) {
        List<CourseRecommend> recommends = new ArrayList<>();
        try {
            // 提取JSON部分
            String jsonStr = aiResponse.trim();
            int startIdx = jsonStr.indexOf("[");
            int endIdx = jsonStr.lastIndexOf("]");
            if (startIdx >= 0 && endIdx > startIdx) {
                jsonStr = jsonStr.substring(startIdx, endIdx + 1);
            }

            JSONArray jsonArray = JSON.parseArray(jsonStr);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                CourseRecommend recommend = new CourseRecommend();
                recommend.setCourseId(obj.getInteger("courseId"));
                recommend.setRecommendReason(obj.getString("reason"));
                recommend.setRecommendScore(obj.getInteger("score"));

                // 查找课程详情
                for (Course c : allCourses) {
                    if (c.getId().equals(recommend.getCourseId())) {
                        recommend.setCourseName(c.getName());
                        recommend.setCoach(c.getCoach());
                        recommend.setScheduleTime(c.getScheduleTime() != null ? sdf.format(c.getScheduleTime()) : "待定");
                        recommend.setRemainingSlots(c.getRemainingSlots());
                        break;
                    }
                }

                if (recommend.getCourseName() != null) {
                    recommends.add(recommend);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommends;
    }

    /**
     * 获取默认推荐（当AI返回结果解析失败时）
     */
    private List<CourseRecommend> getDefaultRecommend(List<Course> allCourses) {
        List<CourseRecommend> recommends = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        int count = 0;
        for (Course c : allCourses) {
            if (count >= 3) break;
            if (c.getRemainingSlots() > 0) {
                CourseRecommend recommend = new CourseRecommend();
                recommend.setCourseId(c.getId());
                recommend.setCourseName(c.getName());
                recommend.setCoach(c.getCoach());
                recommend.setScheduleTime(c.getScheduleTime() != null ? sdf.format(c.getScheduleTime()) : "待定");
                recommend.setRemainingSlots(c.getRemainingSlots());
                recommend.setRecommendReason("热门课程，名额有限，先到先得！");
                recommend.setRecommendScore(80 - count * 5);
                recommends.add(recommend);
                count++;
            }
        }
        return recommends;
    }
}
