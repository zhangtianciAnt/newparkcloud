package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.service_pfans.PFANS2000.AbNormalService;
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
@RequestMapping("/abNormal")
public class Pfans2016Controller {

    @Autowired
    private AbNormalService abNormalService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception{

        try {
            TokenModel tokenModel = tokenService.getToken(request);
            AbNormal abNormal = new AbNormal();
            abNormal.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            abNormal.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(abNormalService.list(abNormal));

        } catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "/createNewUser",method={RequestMethod.POST})
    public ApiResult create(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        abNormalService.insert(abNormal,tokenModel);
        return ApiResult.success();
    }

}
