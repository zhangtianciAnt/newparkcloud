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
                .excludePathPatterns("/user/getCurrentUserAccount")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/BASF10702/checkblack")
                .excludePathPatterns("/BASF10702/create")
                .excludePathPatterns("/BASF10702/updategps")
                .excludePathPatterns("/BASF10702/updateouttime")
                .excludePathPatterns("/BASF10702/createBlack")
                .excludePathPatterns("/dictionary/getHomePage")
                .excludePathPatterns("/BASF10105/linkagelist")
                .excludePathPatterns("/BASF30000/login")
                .excludePathPatterns("/BASF30000/saveChatHistory")
                .excludePathPatterns("/BASF30000/getChatHistory")
                .excludePathPatterns("/BASF10302/import")
                .excludePathPatterns("/BASF10302/createPimsPoint")
                .excludePathPatterns("/BASF10302/getPimsPoint")
                .excludePathPatterns("/pims/getData")
                .excludePathPatterns("/pims/getAlarm")
                .excludePathPatterns("/AI/sendAIMessage")
                .excludePathPatterns("/BASF10204/sendGps")
                .excludePathPatterns("/BASF10204/update")
                .excludePathPatterns("/BASF10204/list")
                .excludePathPatterns("/BASF10702/blacklist")
                .excludePathPatterns("/BASF10702/setCarInfoList")
        ;
    }

    @Bean
    public LoginTimeInterceptor loginTimeInterceptor() {
        return new LoginTimeInterceptor();
    }
}
