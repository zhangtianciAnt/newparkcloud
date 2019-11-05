package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Emailmessage;
import com.nt.service_BASF.BASF10106Service;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 邮件通知
 */
@RestController
@RequestMapping("/email")
public class BASF10106Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BASF10106Service basf10106Service;

    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult get(@RequestBody Emailmessage emailmessage, HttpServletRequest request) throws Exception {
        return ApiResult.success(basf10106Service.get(emailmessage));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody Emailmessage emailmessage, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        int result = basf10106Service.insert(tokenModel, emailmessage);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01);
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Emailmessage emailmessage, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        int result = basf10106Service.update(tokenModel, emailmessage);
        if (result > 0) {
            return ApiResult.success(MsgConstants.INFO_01);
        } else {
            return ApiResult.fail(MsgConstants.ERROR_01);
        }
    }
}
