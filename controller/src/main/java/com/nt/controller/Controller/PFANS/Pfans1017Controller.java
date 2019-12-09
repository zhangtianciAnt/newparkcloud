package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Psdcd;
import com.nt.service_pfans.PFANS1000.PsdcdService;
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
@RequestMapping("/psdcd")
public class Pfans1017Controller {

    @Autowired
    private PsdcdService psdcdService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getPsdcd(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Psdcd psdcd = new Psdcd();
        psdcd.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(psdcdService.getPsdcd(psdcd));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Psdcd psdcd, HttpServletRequest request) throws Exception {
        if (psdcd == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(psdcdService.One(psdcd.getPsdcd_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateFlexibleWork(@RequestBody Psdcd psdcd, HttpServletRequest request) throws Exception{
        if (psdcd == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        psdcdService.updatePsdcd(psdcd,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Psdcd psdcd, HttpServletRequest request) throws Exception {
        if (psdcd == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        psdcdService.insert(psdcd,tokenModel);
        return ApiResult.success();
    }
}
