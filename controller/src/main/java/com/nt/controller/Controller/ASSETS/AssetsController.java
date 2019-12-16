package com.nt.controller.Controller.ASSETS;

import com.nt.dao_Assets.Assets;
import com.nt.service_Assets.AssetsService;
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
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    private AssetsService assetsService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/connection", method = {RequestMethod.GET})
    public ApiResult connection(String address, HttpServletRequest request) throws Exception {
        if (address == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        int count = 0;
        Boolean str = false;
        while (!str) {
            Thread.sleep(5 * 1000);
            count++;
            TokenModel tokenModel = tokenService.getToken(request);
            assetsService.connection(address, tokenModel);
            return ApiResult.success();
        }
        return ApiResult.success();
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Assets assets = new Assets();
        assets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(assetsService.list(assets));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Assets assets, HttpServletRequest request) throws Exception {
        if (assets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetsService.insert(assets, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody Assets assets, HttpServletRequest request) throws Exception {
        if (assets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetsService.update(assets, tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Assets assets, HttpServletRequest request) throws Exception {
        if (assets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(assetsService.One(assets.getAssets_id()));
    }

    @RequestMapping(value = "/importUser",method={RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(assetsService.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }


}
