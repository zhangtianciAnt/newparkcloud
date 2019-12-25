package com.nt.controller.Controller;


import com.nt.dao_BASF.SendEmail;
import com.nt.service_BASF.SendEmailServices;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/SENDEMAIL")
public class SendEmailController {
    @Autowired
    private SendEmailServices sendEmailServices;

    @Autowired
    private TokenService tokenService;

    @Value("${mailUserName}")String mailUserName;
    @Value("${mailPassword}")String mailPassword;
    @Value("${mailHost}")String mailHost;
    @Value("${mailPort}")Integer mailPort;
    @Value("${mailFromAddress}")String mailFromAddress;

    @RequestMapping(value = "/sendemail",method={RequestMethod.POST})
    public ApiResult SendEmail(@RequestBody SendEmail sendemail, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        sendemail.setUserName(mailUserName);
        sendemail.setPassword(mailPassword);
        sendemail.setHost(mailHost);
        sendemail.setPort(mailPort);
        sendemail.setFromAddress(mailFromAddress);
        return ApiResult.success(sendEmailServices.sendmail(tokenModel,sendemail));
    }



}
