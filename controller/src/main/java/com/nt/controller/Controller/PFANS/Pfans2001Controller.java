package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Recruit;
import com.nt.service_pfans.PFANS2000.RecruitService;
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
@RequestMapping("/recruit")
public class Pfans2001Controller {

    @Autowired
    private RecruitService recruitService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getRecruit(HttpServletRequest request) throws Exception{


            TokenModel tokenModel = tokenService.getToken(request);
            Recruit recruit = new Recruit();
            recruit.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(recruitService.getRecruit(recruit));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Recruit recruit, HttpServletRequest request) throws Exception {
        if (recruit == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(recruitService.One(recruit.getRecruitid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateRecruit(@RequestBody Recruit recruit, HttpServletRequest request) throws Exception{
        if (recruit == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        recruitService.updateRecruit(recruit,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Recruit recruit, HttpServletRequest request) throws Exception {
        if (recruit == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        recruitService.insert(recruit,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getRecruitList", method = {RequestMethod.POST})
    public ApiResult getRecruitList(@RequestBody Recruit recruit, HttpServletRequest request) throws Exception {
        if (recruit == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(recruitService.getRecruitList(recruit, request));
    }
}
