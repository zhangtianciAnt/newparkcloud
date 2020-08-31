package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.utils.ApiResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname BASFTestController
 * @Description TODO 类的描述
 * @Date 2020/8/31 14:32
 * @Author skaixx
 */
@RestController
@RequestMapping("/test")
public class BASFTestController {

    @RequestMapping(value = "/test", method = {RequestMethod.POST})
    public ApiResult test(@RequestBody Object o, HttpServletRequest request) throws Exception {
        return ApiResult.success();
    }
}
