package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS3000.Stationery;
import com.nt.service_pfans.PFANS3000.StationeryService;
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
@RequestMapping("/stationery")
public class Pfans3004Controller {
    //查找
    @Autowired
    private StationeryService stationeryService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getStationery(HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Stationery stationery = new Stationery();
        stationery.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(stationeryService.getStationery(stationery));
    }

    @RequestMapping(value="/getStationerylist",method = {RequestMethod.POST})
    public ApiResult getStationerylist(@RequestBody Stationery stationery, HttpServletRequest request) throws Exception{
        if (stationery == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        stationery.setStatus(AuthConstants.APPROVED_FLAG_YES);
        stationery.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(stationeryService.getStationerylist(stationery));
    }

    @RequestMapping(value ="/one",method = { RequestMethod.POST} )
    public ApiResult one(@RequestBody Stationery stationery,HttpServletRequest request) throws Exception{
        if (stationery == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(stationeryService.One(stationery.getStationeryid()));
    }

    @RequestMapping(value="/create",method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Stationery stationery, HttpServletRequest request) throws Exception{
        if (stationery == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        stationeryService.insertStationery(stationery,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateStationery(@RequestBody Stationery stationery, HttpServletRequest request) throws Exception{
        if (stationery == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        stationeryService.updateStationery(stationery,tokenModel);
        return ApiResult.success();
    }

}
