package com.ticketmasterdemo.demo.interceptor;

import java.io.IOException;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ticketmasterdemo.demo.util.JwtUtil;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token");
            return false;
        }
        try {
            String userId = JwtUtil.verifyToken(token.substring(7));
            boolean tokenAboutToExpire = JwtUtil.isTokenAboutToExpire(token.substring(7));
            if (tokenAboutToExpire) {
                String newToken = JwtUtil.generateToken(userId);
                response.setHeader("Authorization", "Bearer " + newToken);
                response.setHeader("Access-Control-Expose-Headers", "Authorization");
            }
            request.setAttribute("userId", userId);
            return true;
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }
}