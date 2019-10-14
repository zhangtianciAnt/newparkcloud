package com.nt.controller.Controller;

import com.nt.dao_Pfans.PFANS2000.Goalmanagement;
import com.nt.service_pfans.PFANS2000.GoalmanagementService;
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
@RequestMapping("/goalmanagement")
public class Pfans2023Controller {

    @Autowired
    private GoalmanagementService goalmanagementService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/createNewUser",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Goalmanagement goalmanagement, HttpServletRequest request) throws Exception {
        if (goalmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        goalmanagementService.insert(goalmanagement,tokenModel);
        return ApiResult.success();
    }

}
