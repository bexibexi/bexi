package com.stu.gym.gymmembermanage.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        // 检查管理员是否已登录
        Object adminUser = session.getAttribute("adminUser");
        if (adminUser != null) {
            return true;
        }

        // 检查会员是否已登录
        Object memberUser = session.getAttribute("memberUser");
        if (memberUser != null) {
            return true;
        }

        // 未登录，重定向到登录页面
        response.sendRedirect("/login");
        return false;
    }
}
