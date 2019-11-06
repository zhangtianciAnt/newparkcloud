package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Alarmreceipt;
import com.nt.service_BASF.AlarmreceiptServices;
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
@RequestMapping("/BASF10108")
public class BASF10108Controller {

    @Autowired
    private AlarmreceiptServices alarmreceiptServices;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getAlarmreceiptList", method = {RequestMethod.GET})
    public ApiResult getAlarmreceiptList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(alarmreceiptServices.getList());
    }

    @RequestMapping(value = "/createAlarmreceipt", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Alarmreceipt alarmreceipt, HttpServletRequest request) throws Exception {
        if (alarmreceipt == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        alarmreceiptServices.insert(alarmreceipt, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/updateAlarmreceipt", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Alarmreceipt alarmreceipt, HttpServletRequest request) throws Exception {
        if (alarmreceipt == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        alarmreceiptServices.update(alarmreceipt, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectAlarmreceipt", method = {RequestMethod.GET})
    public ApiResult selectAlarmreceipt(String alarmreceiptid, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(alarmreceiptid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(alarmreceiptServices.select(alarmreceiptid));
    }
}
