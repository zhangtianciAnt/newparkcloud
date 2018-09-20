package com.nt.newparkcloud.controller.Config;


import com.nt.newparkcloud.controller.ControllerApplication;
import com.nt.newparkcloud.utils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//全局接口调用异常配置

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(ControllerApplication.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error(e.getMessage());
        return ApiResult.fail("操作异常，请稍后重试！");
    }
}
