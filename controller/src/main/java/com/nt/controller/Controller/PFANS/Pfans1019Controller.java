package com.nt.controller.Controller.PFANS;


import com.nt.dao_Pfans.PFANS1000.Trialsoft;
import com.nt.service_pfans.PFANS1000.TrialsoftService;
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
@RequestMapping("/trialsoft")
public class Pfans1019Controller {

    @Autowired
    private TrialsoftService trialsoftService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getTrialsoft(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Trialsoft trialsoft = new Trialsoft();
        trialsoft.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(trialsoftService.getTrialsoft(trialsoft));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Trialsoft trialsoft, HttpServletRequest request) throws Exception {
        if (trialsoft == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(trialsoftService.One(trialsoft.getTrialsoft_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateFlexibleWork(@RequestBody Trialsoft trialsoft, HttpServletRequest request) throws Exception{
        if (trialsoft == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        trialsoftService.updateTrialsoft(trialsoft,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Trialsoft trialsoft, HttpServletRequest request) throws Exception {
        if (trialsoft == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        trialsoftService.insert(trialsoft,tokenModel);
        return ApiResult.success();
    }

}
