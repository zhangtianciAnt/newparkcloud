package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Global;
import com.nt.service_pfans.PFANS1000.GlobalService;
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
@RequestMapping("/global")
public class Pfans1018Controller {

    @Autowired
    private GlobalService globalService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getglobal(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Global global = new Global();
        global.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(globalService.getglobal(global));
    }

    @RequestMapping(value="/one",method={RequestMethod.POST})
    public ApiResult  getglobalApplyOne(@RequestBody Global global,HttpServletRequest request)throws Exception{
        if(global == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(globalService.getglobalApplyOne(global.getGlobal_id()));
    }

    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    public ApiResult updateglobalApply(@RequestBody Global global, HttpServletRequest request)throws Exception{
        if (global == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        globalService.updateglobalApply(global,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult createglobalApply(@RequestBody Global global, HttpServletRequest request) throws Exception {
        if (global == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        globalService.createglobalApply(global,tokenModel);
        return ApiResult.success();
    }
}