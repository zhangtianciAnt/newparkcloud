package com.nt.controller.Controller.PlatformManagement.EnterpriseServices;

import com.nt.dao_ServiceProvider.OnlineApplication;
import com.nt.service_ServiceProvider.OnlineApplicationService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/*
    在线申请Controller
 */
@RestController
@RequestMapping("/onlineApplication")
public class OnlineController {
    @Autowired
    private OnlineApplicationService onlineApplicationService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/insertOnlineApplication", method = {RequestMethod.POST})
    public ApiResult insertPMInformationDelivery(HttpServletRequest request, @RequestBody OnlineApplication onlineApplication) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return onlineApplicationService.insert(onlineApplication, tokenModel);
        } catch (Exception ex) {
            return ApiResult.fail(ex.getMessage());
        }
    }
}
