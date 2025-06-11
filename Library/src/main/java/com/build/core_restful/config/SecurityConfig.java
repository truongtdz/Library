package com.build.core_restful.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] publicUrl = {
            "/", "/v3/api-docs", 
            "/api/v1/auth/**", "/api/v1/upload",
            "/api/v1/order/rental/**", "/api/v1/cron/**",
    };

    private final String[] publicUrlMethodGet = {
            "/api/v1/book/**", "/api/v1/author/**",
            "/api/v1/category/**", "/api/v1/branch/**", 
            "/api/v1/review/**",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(publicUrl).permitAll()
                        .requestMatchers(HttpMethod.GET, publicUrlMethodGet).permitAll()
                        .anyRequest().permitAll()
                )
                .logout(logout -> logout.disable())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403")
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2
                                .jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
