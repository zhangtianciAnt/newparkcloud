package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemePlanVo;
import com.nt.service_pfans.PFANS1000.ThemePlanService;
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
@RequestMapping("/themeplan")
public class Pfans1040Controller {

    //收支データ
    @Autowired
    private ThemePlanService themePlanService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody ThemePlan themePlan, HttpServletRequest request) throws Exception {
        if (themePlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        themePlan.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(themePlanService.getList(themePlan));
    }

    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody ThemePlan themePlan, HttpServletRequest request) throws Exception {
        if (themePlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        themePlan.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(themePlanService.get(themePlan));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody ThemePlanVo themePlan, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        themePlanService.insert(themePlan, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody ThemePlanVo themePlan, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        themePlanService.update(themePlan, tokenModel);
        return ApiResult.success();
    }

}
