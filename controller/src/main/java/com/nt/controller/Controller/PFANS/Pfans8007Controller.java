package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS8000.WorkingDay;

import com.nt.service_pfans.PFANS8000.WorkingDayService;

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
@RequestMapping("/workingday")
public class Pfans8007Controller {
    @Autowired
    private WorkingDayService workingdayService;
    @Autowired
    private TokenService tokenService;
    @RequestMapping(value = "/createNewUser", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody WorkingDay workingday, HttpServletRequest request) throws Exception {
        if (workingday == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        workingdayService.insert(workingday, tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody WorkingDay workingday, HttpServletRequest request) throws Exception {
        if (workingday == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        workingdayService.deletete(workingday, tokenModel);
        return ApiResult.success();
    }



    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody WorkingDay workingday,  HttpServletRequest request) throws Exception {
        if (workingday == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        workingday.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(workingdayService.getList(workingday));
    }
}

