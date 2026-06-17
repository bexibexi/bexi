package com.stu.gym.gymmembermanage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AI大模型API调用工具类（小米MiMo）
 */
@Component
public class AiApiUtil {

    private static final Logger logger = LoggerFactory.getLogger(AiApiUtil.class);

    @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.api-url}")
    private String apiUrl;

    @Value("${ai.model}")
    private String model;

    @Value("${ai.max-tokens}")
    private Integer maxTokens;

    @Value("${ai.temperature}")
    private Double temperature;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 调用AI接口进行对话
     */
    public String chat(String systemPrompt, List<Map<String, String>> chatHistory, String userMessage) {
        try {
            // 构建messages数组
            JSONArray messages = new JSONArray();

            // 添加系统提示词
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                JSONObject systemMsg = new JSONObject();
                systemMsg.put("role", "system");
                systemMsg.put("content", systemPrompt);
                messages.add(systemMsg);
            }

            // 添加历史消息
            if (chatHistory != null) {
                for (Map<String, String> msg : chatHistory) {
                    JSONObject historyMsg = new JSONObject();
                    historyMsg.put("role", msg.get("role"));
                    historyMsg.put("content", msg.get("content"));
                    messages.add(historyMsg);
                }
            }

            // 添加用户当前消息
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);

            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", model);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("temperature", temperature);

            String jsonBody = requestBody.toJSONString();
            logger.info("AI请求地址: {}", apiUrl);
            logger.info("AI请求模型: {}", model);
            logger.info("AI请求体: {}", jsonBody);

            // 发送请求
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(
                            MediaType.parse("application/json"),
                            jsonBody
                    ))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                logger.info("AI响应状态码: {}", response.code());

                if (response.body() != null) {
                    String responseBody = response.body().string();
                    logger.info("AI响应内容: {}", responseBody);

                    if (response.isSuccessful()) {
                        JSONObject jsonResponse = JSON.parseObject(responseBody);
                        JSONArray choices = jsonResponse.getJSONArray("choices");
                        if (choices != null && !choices.isEmpty()) {
                            JSONObject firstChoice = choices.getJSONObject(0);
                            JSONObject message = firstChoice.getJSONObject("message");
                            return message.getString("content");
                        }
                    } else {
                        logger.error("AI请求失败，状态码: {}, 响应: {}", response.code(), responseBody);
                        return "抱歉，AI服务返回错误（状态码：" + response.code() + "），请稍后再试。";
                    }
                }
                return "抱歉，AI服务返回空响应，请稍后再试。";
            }
        } catch (IOException e) {
            logger.error("AI服务连接失败", e);
            return "抱歉，AI服务连接失败，请检查网络后重试。错误信息：" + e.getMessage();
        } catch (Exception e) {
            logger.error("AI请求异常", e);
            return "抱歉，处理您的请求时出现错误，请稍后再试。错误信息：" + e.getMessage();
        }
    }

    /**
     * 简单对话（无历史记录）
     */
    public String simpleChat(String systemPrompt, String userMessage) {
        return chat(systemPrompt, null, userMessage);
    }
}
