package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import com.nt.service_Auth.AuthService;
import com.nt.service_Org.DictionaryService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/getForSelect",method={RequestMethod.GET})
    public ApiResult getForSelect(String code, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(code)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        return ApiResult.success(dictionaryService.getForSelect(code));
    }
}
