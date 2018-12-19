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
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/getCurrentUserAccount")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/information/getInfoByType")
                .excludePathPatterns("/information/getInfoById")
                .excludePathPatterns("/weChat/getUser")
                .excludePathPatterns("/user/updUserInfo")
                .excludePathPatterns("/user/getWxById")
                .excludePathPatterns("/information/addActivity")
                .excludePathPatterns("/ServiceCategory/getwxservicecategory")
                .excludePathPatterns("/weChat/getWeChatUserInfo");
    }

    @Bean
    public LoginTimeInterceptor loginTimeInterceptor() {
        return new LoginTimeInterceptor();
    }
}
