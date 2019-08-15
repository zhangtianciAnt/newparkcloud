/**
 * 服务商Controller
 */
package com.nt.controller.Controller.PlatformManagement.EnterpriseServices;

import com.alibaba.fastjson.JSONObject;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_ServiceProvider.ServiceProvider;
import com.nt.service_ServiceProvider.ServiceProviderService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/serviceProvider")
public class ServiceProviderController {

    @Autowired
    private ServiceProviderService serviceProviderService;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询服务商
     *
     * @param customerInfo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectServiceProvider", method = {RequestMethod.POST})
    public ApiResult selectServiceProvider(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) throws Exception {
        JSONObject obj = new JSONObject();
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(serviceProviderService.select(customerInfo, tokenModel));
        } catch (Exception ex) {
            obj.put("message", ex);
            return ApiResult.fail(obj);
        }
    }
}
