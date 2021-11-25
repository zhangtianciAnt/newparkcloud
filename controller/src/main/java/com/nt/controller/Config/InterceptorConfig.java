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
                .excludePathPatterns("/file/**")
                .excludePathPatterns("/assets/connection")
                .excludePathPatterns("/assets/scanOne")
                .excludePathPatterns("/assets/scanList")
                .excludePathPatterns("/role/getAppData")
                .excludePathPatterns("/auth/getAttendance")
                .excludePathPatterns("/auth/updateAnnualLeaveBefore")
                .excludePathPatterns("/auth/creatAnnualLeaveAn")
                .excludePathPatterns("/userTools/**")
                .excludePathPatterns("/report/**")
                .excludePathPatterns("/incomeexpenditure/getradio")
                .excludePathPatterns("/companyprojects/report")
                .excludePathPatterns("/seal/changeSealWorkFlowInfo")
                .excludePathPatterns("/pjExternalInjection/getTableinfoReport")
                .excludePathPatterns("/departmentaccount/getTable1051infoReport")
                .excludePathPatterns("/departmentalinside/getTableinfoReport")
                .excludePathPatterns("/departmental/getTable1050infoReport")
                .excludePathPatterns("/attendance/getTable2010infoReported")
        ;
    }

    @Bean
    public LoginTimeInterceptor loginTimeInterceptor() {
        return new LoginTimeInterceptor();
    }
}
