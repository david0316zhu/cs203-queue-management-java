package com.ticketmasterdemo.demo.configure;

import com.ticketmasterdemo.demo.interceptor.JwtInterceptor;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigure implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Autowired
    WebConfigure(JwtInterceptor jwtInterceptor){
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/users/**")
                .addPathPatterns("/queues/**")
                .addPathPatterns("/events-register/**")
                .excludePathPatterns("/users/register");
    }
}