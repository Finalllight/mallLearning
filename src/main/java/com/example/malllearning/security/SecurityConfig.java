package com.example.malllearning.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityErrorHandler securityErrorHandler;

    public SecurityConfig(SecurityErrorHandler securityErrorHandler) {
        this.securityErrorHandler = securityErrorHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ==================== CORS ====================
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ==================== CSRF ====================
                // 前后端分离项目（Session + Cookie），可以先关闭 CSRF
                // 后续学习阶段可以开启 CSRF Token 保护
                .csrf(csrf -> csrf.disable())

                // ==================== 路径权限规则 ====================
                .authorizeHttpRequests(auth -> auth
                        // --- 公开接口 ---
                        .requestMatchers(HttpMethod.POST, "/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/coupons").permitAll()

                        // --- Swagger 文档 ---
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()

                        // --- 管理员接口 ---
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- 其余所有 /api/** 接口需登录 ---
                        .requestMatchers("/api/**").authenticated()

                        // --- 其他放行（如静态资源） ---
                        .anyRequest().permitAll()
                )

                // ==================== Session 管理 ====================
                .sessionManagement(session -> session
                        .maximumSessions(1)                    // 同一用户最多1个会话
                        .maxSessionsPreventsLogin(false)        // 新登录踢掉旧会话
                )

                // ==================== 错误处理 ====================
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(securityErrorHandler)   // 401
                        .accessDeniedHandler(securityErrorHandler)        // 403
                )

                // ==================== 禁用默认的表单登录和HTTP Basic ====================
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // ==================== 退出登录 ====================
                .logout(logout -> logout
                        .logoutUrl("/api/users/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":0,\"message\":\"success\"}");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
