package com.example.malllearning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    private final LoginInterceptor loginInterceptor;
    private final AdminInterceptor adminInterceptor;

    public WebMvcConfig(LoginInterceptor loginInterceptor, AdminInterceptor adminInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.adminInterceptor = adminInterceptor;
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:5174")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 公共排除路径
        String[] excludePaths = {
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**"
        };

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/cart/**", "/api/orders/**",
                        "/api/users/balance", "/api/users/logout",
                        "/api/coupons/my", "/api/coupons/my/**",
                        "/api/coupons/*/claim")
                .excludePathPatterns(excludePaths);

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns(excludePaths);
    }
}
