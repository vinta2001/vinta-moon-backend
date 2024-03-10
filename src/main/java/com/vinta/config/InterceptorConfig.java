package com.vinta.config;

import com.vinta.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    protected AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(authenticationInterceptor());
        interceptorRegistration.excludePathPatterns("**/login", "**/register");
    }
}
