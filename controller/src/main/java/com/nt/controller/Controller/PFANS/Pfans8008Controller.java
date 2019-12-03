package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.service_pfans.PFANS8000.InformationDeliveryService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/informationdelivery")
public class Pfans8008Controller {
    //查找信息发布
    @Autowired
    private InformationDeliveryService informationService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getInformation(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(informationService.getInformation(tokenModel));
    }


    @RequestMapping(value = "/getListType", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        InformationDelivery informationDelivery=new InformationDelivery();
        informationDelivery.setOwners(tokenModel.getOwnerList());
        informationDelivery.setAvailablestate("0");
        return ApiResult.success(informationService.getListType(informationDelivery));
    }





    @RequestMapping(value = "/getone", method = {RequestMethod.GET})
    public ApiResult getOneInformation(@RequestParam String information, HttpServletRequest request) throws Exception {
        if (information.isEmpty()) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(informationService.getOneInformation(information, tokenModel));
    }


    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insertInformation(@RequestBody InformationDelivery informationDelivery, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(informationDelivery)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        informationService.insertInformation(informationDelivery, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody InformationDelivery informationDelivery, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(informationDelivery)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        informationService.updateInformation(informationDelivery, tokenModel);
        return ApiResult.success();
    }

}
