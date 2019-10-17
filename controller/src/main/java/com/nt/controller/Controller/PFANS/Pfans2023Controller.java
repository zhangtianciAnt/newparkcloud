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

@RestController
@RequestMapping("/goalmanagement")
public class Pfans2023Controller {

    @Autowired
    private GoalManagementService goalmanagementService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception{

        try {
            TokenModel tokenModel = tokenService.getToken(request);
            GoalManagement goalmanagement = new GoalManagement();
            goalmanagement.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            goalmanagement.setTenantid(tokenModel.getTenantId());
            goalmanagement.setOwners(tokenModel.getOwnerList());
            goalmanagement.setIds(tokenModel.getIdList());
            return ApiResult.success(goalmanagementService.list(goalmanagement));

        } catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }
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

}
