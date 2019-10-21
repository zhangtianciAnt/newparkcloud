package com.nt.controller.Controller;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.service_pfans.PFANS8000.InformationDeliveryService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/informationdelivery")
public class InformationDeliveryController {
    //查找信息发布
    @Autowired
    private InformationDeliveryService informationService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getInformation(HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
       return ApiResult.success(informationService.getInformation(tokenModel));
    }

    @RequestMapping(value="/getone",method = {RequestMethod.GET})
    public ApiResult getOneInformation(@RequestParam String information, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(informationService.getOneInformation(information,tokenModel));
    }


    @RequestMapping(value="/insert",method = {RequestMethod.POST})
    public ApiResult insertInformation(@RequestBody InformationDelivery informationDelivery, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        informationService.insertInformation(informationDelivery,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody InformationDelivery informationDelivery, HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        informationService.updateInformation(informationDelivery,tokenModel);
        return ApiResult.success();
    }

}
