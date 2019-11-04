package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Alarmreceipt;
import com.nt.service_BASF.BASF10108Services;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/BASF10108")
public class BASF10108Controller {

    @Autowired
    private BASF10108Services bASF10108Services;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getAlarmreceiptList", method = {RequestMethod.GET})
    public ApiResult getAlarmreceiptList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        String userid = tokenModel.getUserId();
        Alarmreceipt alarmreceipt = new Alarmreceipt();
        alarmreceipt.setOwner(userid);
        return ApiResult.success(bASF10108Services.getList(alarmreceipt));
    }
}
