package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.service_pfans.PFANS1000.NapalmService;
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

import javax.servlet.http.HttpServletRequest;

public class pfans1031Controller {
    @Autowired
    private NapalmService napalmService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Napalm napalm = new Napalm();
        napalm.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(napalmService.get(napalm));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Napalm napalm, HttpServletRequest request) throws Exception {
        if (napalm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(napalmService.One(napalm.getNapalm_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Napalm napalm, HttpServletRequest request) throws Exception{
        if (napalm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        napalmService.update(napalm,tokenModel);
        return ApiResult.success();
    }

}
