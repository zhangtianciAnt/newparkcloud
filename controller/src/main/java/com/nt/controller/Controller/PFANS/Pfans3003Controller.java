package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS3000.BusinessCard;
import com.nt.service_pfans.PFANS3000.BusinessCardService;
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
@RequestMapping("/businesscard")
public class Pfans3003Controller {

    @Autowired
    private BusinessCardService businesscardService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getBusinessCard(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        BusinessCard businesscard = new BusinessCard();
        businesscard.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(businesscardService.getBusinessCard(businesscard));
    }

    @RequestMapping(value = "/getBusinessCardlist", method = {RequestMethod.POST})
    public ApiResult getBusinessCardlist(@RequestBody BusinessCard businesscard, HttpServletRequest request) throws Exception {
        if (businesscard == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businesscard.setStatus(AuthConstants.APPROVED_FLAG_YES);
        businesscard.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(businesscardService.getBusinessCardlist(businesscard));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody BusinessCard businesscard, HttpServletRequest request) throws Exception {
        if (businesscard == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(businesscardService.One(businesscard.getBusinesscardid()));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody BusinessCard businesscard, HttpServletRequest request) throws Exception {
        if (businesscard == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businesscardService.insertBusinessCard(businesscard, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateBusinessCard(@RequestBody BusinessCard businesscard, HttpServletRequest request) throws Exception {
        if (businesscard == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businesscardService.updateBusinessCard(businesscard, tokenModel);
        return ApiResult.success();
    }

}
