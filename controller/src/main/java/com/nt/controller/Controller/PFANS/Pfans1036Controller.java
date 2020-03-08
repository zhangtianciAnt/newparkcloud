package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessplanVo;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/businessplan")
public class Pfans1036Controller {
    @Autowired
    private BusinessplanService businessplanService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Businessplan businessplan = new Businessplan();
        businessplan.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(businessplanService.get(businessplan));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String businessplanid, HttpServletRequest request) throws Exception {
        if (businessplanid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessplanService.selectById(businessplanid));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult insertBusinessplanVo(@RequestBody Businessplan businessplan, HttpServletRequest request) throws Exception {
        if (businessplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessplanService.insertBusinessplan(businessplan, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateBusinessplanVo(@RequestBody BusinessplanVo businessplanVo, HttpServletRequest request) throws Exception {
        if (businessplanVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessplanService.updateBusinessplanVo(businessplanVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getpersonplan", method = {RequestMethod.GET})
    public ApiResult getPersonPlan(@RequestParam int year,@RequestParam String groupid, HttpServletRequest request) throws Exception {
        if (groupid == "") {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessplanService.getPersonPlan(year, groupid));
    }

}
