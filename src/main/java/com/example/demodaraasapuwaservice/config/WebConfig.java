package com.example.demodaraasapuwaservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders(HttpHeaders.CONTENT_TYPE,
                        HttpHeaders.CONTENT_DISPOSITION,
                        HttpHeaders.ORIGIN,
                        HttpHeaders.ACCEPT
                );
    }
}
