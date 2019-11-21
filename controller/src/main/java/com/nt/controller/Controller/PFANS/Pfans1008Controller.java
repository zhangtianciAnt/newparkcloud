package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.service_pfans.PFANS1000.SoftwaretransferService;
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
@RequestMapping("/softwaretransfer")
public class Pfans1008Controller {
    //查找信息发布
    @Autowired
    private SoftwaretransferService softwaretransferService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{

            TokenModel tokenModel = tokenService.getToken(request);
            Softwaretransfer softwaretransfer = new Softwaretransfer();
            softwaretransfer.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(softwaretransferService.getSoftwaretransfer(softwaretransfer));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Softwaretransfer softwaretransfer, HttpServletRequest request) throws Exception {
        if (softwaretransfer == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(softwaretransferService.One(softwaretransfer.getSoftwaretransferid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateSoftwaretransfer(@RequestBody Softwaretransfer softwaretransfer, HttpServletRequest request) throws Exception{
        if (softwaretransfer == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        softwaretransferService.updateSoftwaretransfer(softwaretransfer,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Softwaretransfer softwaretransfer, HttpServletRequest request) throws Exception {
        if (softwaretransfer == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        softwaretransferService.insert(softwaretransfer,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getSoftwaretransferList", method = {RequestMethod.POST})
    public ApiResult getSoftwaretransferList(@RequestBody Softwaretransfer softwaretransfer, HttpServletRequest request) throws Exception {
        if (softwaretransfer == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(softwaretransferService.getSoftwaretransferList(softwaretransfer, request));
    }
}
