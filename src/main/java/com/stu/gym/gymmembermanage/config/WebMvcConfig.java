package com.stu.gym.gymmembermanage.config;

import com.stu.gym.gymmembermanage.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**", "/member/**")  // 拦截的路径
                .excludePathPatterns(                         // 排除的路径
                        "/login",
                        "/register",
                        "/doLogin",
                        "/doRegister",
                        "/logout",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }
}
