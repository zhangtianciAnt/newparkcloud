package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
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
@RequestMapping("/loanapplication")
public class Pfans1006Controller {

    @Autowired
    private LoanApplicationService loanapplicationService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getLoanapplication(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        LoanApplication loanapplication = new LoanApplication();
        loanapplication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(loanapplicationService.getLoanApplication(loanapplication));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws Exception {
        if (loanapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(loanapplicationService.One(loanapplication.getLoanapplication_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateFlexibleWork(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws Exception{
        if (loanapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        loanapplicationService.updateLoanApplication(loanapplication,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws Exception {
        if (loanapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        loanapplicationService.insert(loanapplication,tokenModel);
        return ApiResult.success();
    }
}
