package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.service_pfans.PFANS1000.AssetinformationService;
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
@RequestMapping("/assetinformation")
public class Pfans1007Controller {
    //查找信息发布
    @Autowired
    private AssetinformationService assetinformationService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{

            TokenModel tokenModel = tokenService.getToken(request);
            Assetinformation assetinformation = new Assetinformation();
            assetinformation.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(assetinformationService.getAssetinformation(assetinformation));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Assetinformation assetinformation, HttpServletRequest request) throws Exception {
        if (assetinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(assetinformationService.One(assetinformation.getAssetinformationid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateAssetinformation(@RequestBody Assetinformation assetinformation, HttpServletRequest request) throws Exception{
        if (assetinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetinformationService.updateAssetinformation(assetinformation,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Assetinformation assetinformation, HttpServletRequest request) throws Exception {
        if (assetinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetinformationService.insert(assetinformation,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getAssetinformationList", method = {RequestMethod.POST})
    public ApiResult getAssetinformationList(@RequestBody Assetinformation assetinformation, HttpServletRequest request) throws Exception {
        if (assetinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(assetinformationService.getAssetinformationList(assetinformation, request));
    }
}
