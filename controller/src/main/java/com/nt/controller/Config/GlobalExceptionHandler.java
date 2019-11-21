package com.nt.controller.Config;



import cn.hutool.core.util.StrUtil;
import com.nt.controller.Start;
import com.nt.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//全局接口调用异常配置

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(Start.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error(e.getMessage());
        try {
            if(e instanceof LogicalException){
                return ApiResult.fail(e.getMessage());
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.EXCEPTION_ERR_O1, RequestUtils.CurrentLocale(req)));
            }

        } catch (Exception e1) {
            return ApiResult.fail();
        }
    }
}
