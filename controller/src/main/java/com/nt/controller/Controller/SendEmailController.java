package com.nt.controller.Controller;


import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
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
import org.springframework.web.socket.TextMessage;

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

    // websocket消息推送
    private WebSocket ws = new WebSocket();
    // WebSocketVow
    private WebSocketVo webSocketVo = new WebSocketVo();


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

    @RequestMapping(value = "/createswitch", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Switchnotifications switchnotifications, HttpServletRequest request) throws Exception {
        switchnotificationsServices.create(switchnotifications);
        return ApiResult.success();
    }

    @RequestMapping(value = "/deleteswitch", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Switchnotifications switchnotifications, HttpServletRequest request) throws Exception {
        switchnotificationsServices.delete(switchnotifications);
        Switchnotifications switchnotifications1 = new Switchnotifications();
        webSocketVo.setSwitchList(switchnotificationsServices.list(switchnotifications1));
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
        return ApiResult.success();
    }



}
