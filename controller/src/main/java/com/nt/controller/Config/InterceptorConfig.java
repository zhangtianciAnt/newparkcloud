package com.nt.controller.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //这里可以添加多个拦截器
        registry.addInterceptor(loginTimeInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/excel/**")
        ;
    }

    @Bean
    public LoginTimeInterceptor loginTimeInterceptor() {
        return new LoginTimeInterceptor();
    }
}
