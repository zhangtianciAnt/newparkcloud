package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS6000.SupplierinforService;
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
@RequestMapping("/supplierinfor")
public class Pfans6003Controller {

    @Autowired
    private SupplierinforService supplierinforService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getsupplierinfor(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Supplierinfor supplierinfor = new Supplierinfor();
        supplierinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(supplierinforService.getsupplierinfor(supplierinfor));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult getsupplierinforApplyOne(@RequestBody Supplierinfor supplierinfor, HttpServletRequest request) throws Exception {
        if (supplierinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(supplierinforService.getsupplierinforApplyOne(supplierinfor.getSupplierinfor_id()));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updatesupplierinforApply(@RequestBody Supplierinfor supplierinfor, HttpServletRequest request) throws Exception {
        if (supplierinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        supplierinforService.updatesupplierinforApply(supplierinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult createsupplierinforApply(@RequestBody Supplierinfor supplierinfor, HttpServletRequest request) throws Exception {
        if (supplierinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        supplierinforService.createsupplierinforApply(supplierinfor, tokenModel);
        return ApiResult.success();
    }
}