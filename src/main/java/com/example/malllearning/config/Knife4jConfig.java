package com.example.malllearning.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("用户管理系统 API 文档")
                        .version("1.0")
                        .description("这是一个 Spring Boot 前后端分离项目的接口文档")
                        .contact(new Contact().name("开发者").email("dev@example.com"))
                );
    }
}
