package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.GoalManagement;
import com.nt.service_pfans.PFANS2000.GoalManagementService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/goalmanagement")
public class Pfans2023Controller {

    @Autowired
    private GoalManagementService goalmanagementService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody GoalManagement goalmanagement, HttpServletRequest request) throws Exception {
        if (goalmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        GoalManagement log=goalmanagementService.One(goalmanagement.getGoalmanagement_id());
        return ApiResult.success(log);
    }

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult List(@RequestBody GoalManagement goalManagement, HttpServletRequest request) throws Exception {
        if (goalManagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        goalManagement.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(goalmanagementService.list(goalManagement));
    }


    @RequestMapping(value="/updateInfo",method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody GoalManagement goalManagement, HttpServletRequest request) throws Exception{
        if (goalManagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        goalmanagementService.upd(goalManagement,tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/createNewUser",method={RequestMethod.POST})
    public ApiResult create(@RequestBody GoalManagement goalmanagement, HttpServletRequest request) throws Exception {
        if (goalmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        goalmanagementService.insert(goalmanagement,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/downLoad1", method = {RequestMethod.POST})
    public void downLoad1(@RequestBody GoalManagement goalmanagement, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        GoalManagement gmt = goalmanagementService.One(goalmanagement.getGoalmanagement_id());
        Map<String, Object> data = new HashMap<>();
        data.put("gmt", gmt);
    }
}
