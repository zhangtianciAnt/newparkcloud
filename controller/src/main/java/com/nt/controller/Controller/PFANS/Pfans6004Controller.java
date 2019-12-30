package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
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
@RequestMapping("/expatriatesinfor")
public class Pfans6004Controller {

    @Autowired
    private ExpatriatesinforService expatriatesinforService;

    @Autowired
    private SupplierinforService supplierinforService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getexpatriatesinfor(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        expatriatesinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(expatriatesinforService.getexpatriatesinfor(expatriatesinfor));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult getexpatriatesinforApplyOne(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception {
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(expatriatesinforService.getexpatriatesinforApplyOne(expatriatesinfor.getExpatriatesinfor_id()));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateexpatriatesinforApply(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception {
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        expatriatesinforService.updateexpatriatesinforApply(expatriatesinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult createexpatriatesinforApply(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception {
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        expatriatesinforService.createexpatriatesinforApply(expatriatesinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getSupplierNameList", method = {RequestMethod.POST})
    public ApiResult getSupplierNameList(@RequestBody Supplierinfor supplierinfor, HttpServletRequest request) throws Exception {
        if (supplierinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(supplierinforService.getSupplierNameList(supplierinfor, request));
    }
}