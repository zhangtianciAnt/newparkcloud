package com.nt.controller.Controller;


import com.nt.dao_BASF.EmailConfig;
import com.nt.dao_BASF.SendEmail;
import com.nt.dao_BASF.Switchnotifications;
import com.nt.service_BASF.EmailConfigServices;
import com.nt.service_BASF.SendEmailServices;
import com.nt.service_BASF.SwitchnotificationsServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/SENDEMAIL")
public class SendEmailController {
    @Autowired
    private SendEmailServices sendEmailServices;

    @Autowired
    private EmailConfigServices emailConfigServices;

    @Autowired
    private SwitchnotificationsServices switchnotificationsServices;

    @Autowired
    private TokenService tokenService;


    @RequestMapping(value = "/sendemail",method={RequestMethod.POST})
    public ApiResult SendEmail(@RequestBody SendEmail sendemail, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        List<EmailConfig> emailconfig =  emailConfigServices.get();
        sendemail.setUserName(emailconfig.get(0).getUsername());
        sendemail.setPassword(emailconfig.get(0).getPassword());
        sendemail.setHost(emailconfig.get(0).getHost());
        sendemail.setPort(emailconfig.get(0).getPort());
        sendemail.setFromAddress(emailconfig.get(0).getFromaddress());
        sendemail.setContextType(emailconfig.get(0).getContexttype());
        return ApiResult.success(sendEmailServices.sendmail(tokenModel,sendemail));
    }

    @RequestMapping(value = "/listswitch", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody Switchnotifications switchnotifications, HttpServletRequest request) throws Exception {
        return ApiResult.success(switchnotificationsServices.list(switchnotifications));
    }

    @RequestMapping(value = "/deleteswitch", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Switchnotifications switchnotifications, HttpServletRequest request) throws Exception {
        switchnotificationsServices.delete(switchnotifications);
        return ApiResult.success();
    }



}
