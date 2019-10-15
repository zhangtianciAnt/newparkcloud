package com.nt.controller.Controller;

import com.nt.dao_Pfans.PFANS5000.Logmanagement;
import com.nt.service_pfans.PFANS5000.LogmanagementService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/logmanagement")
public class Pfans5008Controller {

    @Autowired
    private LogmanagementService logmanagementService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/createNewUser",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Logmanagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        logmanagementService.insert(logmanagement,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getDataList", method={RequestMethod.GET})
    public ApiResult getDataList(HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(logmanagementService.getDataList());
    }

}
