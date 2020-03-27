package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Unusedevice;
import com.nt.service_pfans.PFANS1000.UnusedeviceService;
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
@RequestMapping("/unusedevice")
public class Pfans1003Controller {
    @Autowired
    private UnusedeviceService unusedeviceService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{
            TokenModel tokenModel = tokenService.getToken(request);
            Unusedevice unusedevice = new Unusedevice();
            unusedevice.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(unusedeviceService.getUnusedevice(unusedevice));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Unusedevice unusedevice, HttpServletRequest request) throws Exception {
        if (unusedevice == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(unusedeviceService.One(unusedevice.getUnusedeviceid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateUnusedevice(@RequestBody Unusedevice unusedevice, HttpServletRequest request) throws Exception{
        if (unusedevice == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        unusedeviceService.update(unusedevice,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Unusedevice unusedevice, HttpServletRequest request) throws Exception {
        if (unusedevice == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        unusedeviceService.insert(unusedevice,tokenModel);
        return ApiResult.success();
    }

}
