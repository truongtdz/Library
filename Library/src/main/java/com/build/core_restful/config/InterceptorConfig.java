package com.build.core_restful.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] publicUrl = {
                "/",
                "/api/v1/auth/login", "/api/v1/auth/register",
                "/api/v1/auth/refresh", "/api/v1/auth/logout",
                "/api/v1/auth/account",
        };

        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(publicUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Giả sử bạn lưu file vào thư mục D:/6-DoAn/BackEnd/uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/6-DoAn/BackEnd/uploads/");
    }
}
