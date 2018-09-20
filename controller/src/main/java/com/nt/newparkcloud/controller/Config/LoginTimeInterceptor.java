package com.nt.newparkcloud.controller.Config;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginTimeInterceptor extends HandlerInterceptorAdapter {
    private int startTime;
    private int endTime;
    //依赖注入,请看配置文件
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
    //在控制器执行前调用
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        return true;  //通过拦截器，继续执行请求
    }
}
