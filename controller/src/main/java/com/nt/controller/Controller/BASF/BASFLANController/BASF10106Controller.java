package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Emailmessage;
import com.nt.service_BASF.EmailMessageService;
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
@RequestMapping("/BASF10106")
public class BASF10106Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailMessageService emailMessageService;

    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult get(@RequestBody Emailmessage emailmessage) throws Exception {
        return ApiResult.success(emailMessageService.get(emailmessage));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody Emailmessage emailmessage, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        emailMessageService.insert(tokenModel, emailmessage);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Emailmessage emailmessage, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        emailMessageService.update(tokenModel, emailmessage);
        return ApiResult.success();
    }

    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody Emailmessage emailmessage, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        emailMessageService.del(tokenModel, emailmessage);
        return ApiResult.success();
    }
}
