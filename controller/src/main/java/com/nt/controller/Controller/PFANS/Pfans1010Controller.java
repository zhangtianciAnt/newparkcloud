package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Communication;

import com.nt.service_pfans.PFANS1000.CommunicationService;
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
@RequestMapping("/communication")
public class Pfans1010Controller {

    //查找信息发布
    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getCommunication(HttpServletRequest request)throws  Exception{


        TokenModel tokenModel = tokenService.getToken(request);
        Communication communication = new Communication();
        communication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(communicationService.getCommunication(communication));

    }

    @RequestMapping(value = "/selectCommunication", method = {RequestMethod.POST})
    public ApiResult selectCommunication(@RequestBody  Communication communication, HttpServletRequest request) throws Exception {
        if (communication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(communicationService.selectCommunication());
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Communication communication, HttpServletRequest request) throws Exception {
        if (communication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(communicationService.One(communication.getCommunication_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateCommunication(@RequestBody Communication communication, HttpServletRequest request) throws Exception{
        if (communication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        communicationService.updateCommunication(communication,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Communication communication, HttpServletRequest request) throws Exception {
        if (communication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        communicationService.insert(communication,tokenModel);
        return ApiResult.success();
    }








}
