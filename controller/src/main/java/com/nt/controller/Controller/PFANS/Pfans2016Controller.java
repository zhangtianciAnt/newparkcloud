package com.nt.controller.Controller.PFANS;

import cn.hutool.core.util.StrUtil;
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

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        AbNormal abNormal = new AbNormal();
        abNormal.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(abNormalService.list(abNormal));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        try{
            if (abNormal == null) {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
            TokenModel tokenModel = tokenService.getToken(request);
            //未承认
            abNormal.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
            abNormalService.insert(abNormal, tokenModel);
            return ApiResult.success();
        }catch(Exception e){
            return ApiResult.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        abNormalService.upd(abNormal, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(abNormalService.One(abNormal.getAbnormalid()));
    }

    @RequestMapping(value = "/cklength", method = {RequestMethod.POST})
    public ApiResult cklength(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(abNormalService.cklength(abNormal));
    }

    @RequestMapping(value = "/updateOvertime", method = {RequestMethod.POST})
    public ApiResult updateOvertime(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        abNormalService.updateOvertime(abNormal);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getSickleave", method = {RequestMethod.GET})
    public ApiResult getSickleave(String userid, HttpServletRequest request) throws Exception {
        if (userid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(abNormalService.getSickleave(userid));
    }


    @RequestMapping(value = "/selectAbNormalParent", method = {RequestMethod.GET})
    public ApiResult selectAbNormalParent(String userid, HttpServletRequest request) throws Exception {
        if (userid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(abNormalService.selectAbNormalParent(userid));
    }
}
