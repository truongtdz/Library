package com.build.core_restful.config;

import java.util.List;

import com.build.core_restful.domain.Permission;
import com.build.core_restful.domain.Role;
import com.build.core_restful.domain.User;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.system.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        if (httpMethod.equalsIgnoreCase("GET")) {
            if (pathMatcher.match("/api/v1/book/**", requestURI) ||
                pathMatcher.match("/api/v1/author/**", requestURI) ||
                pathMatcher.match("/api/v1/category/**", requestURI)) {
                return true;
            }
        }

        // check permission
        String email = JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "";
        if (email != null && !email.isEmpty()) {
            User user = this.userService.getUserByEmail(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                            && item.getMethod().equals(httpMethod));

                    if (isAllow == false) {
                        throw new NewException("Bạn không có quyền truy cập endpoint này.");
                    }
                } else {
                    throw new NewException("Bạn không có quyền truy cập endpoint này.");
                }
            }
        }

        return true;
    }
}

