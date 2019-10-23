package com.nt.controller.Controller.PFANS;

import com.nt.service_pfans.PFANS2000.FlexibleWorkService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.dao_Pfans.PFANS2000.FlexibleWork;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/flexiblework")
public class Pfans2014Controller {

    //查找信息发布
    @Autowired
    private FlexibleWorkService flexibleWorkService;

    @Autowired
    private  TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getFlexibleWork(HttpServletRequest request)throws  Exception{


            TokenModel tokenModel = tokenService.getToken(request);
            FlexibleWork flexiblework = new FlexibleWork();
            flexiblework.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(flexibleWorkService.getFlexibleWork(flexiblework));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody FlexibleWork flexiblework, HttpServletRequest request) throws Exception {
        if (flexiblework == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(flexibleWorkService.One(flexiblework.getFlexibleworkid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateFlexibleWork(@RequestBody FlexibleWork flexiblework, HttpServletRequest request) throws Exception{
        if (flexiblework == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        flexibleWorkService.updateFlexibleWork(flexiblework,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody FlexibleWork flexiblework, HttpServletRequest request) throws Exception {
        if (flexiblework == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        flexibleWorkService.insert(flexiblework,tokenModel);
        return ApiResult.success();
    }


}