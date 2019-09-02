package com.nt.controller.Controller.PlatformManagement.EnterpriseServices;

import com.alibaba.fastjson.JSONObject;
import com.nt.dao_ServiceProvider.PMInformationDelivery;
import com.nt.service_ServiceProvider.PMInformationDeliveryService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 平台管理信息发布
 */
@RestController
@RequestMapping("/PMInformationDelivery")
public class PMInformationDeliveryController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PMInformationDeliveryService pmInformationDeliveryService;

    @RequestMapping(value = "/insertPMInformationDelivery", method = {RequestMethod.POST})
    public ApiResult insertPMInformationDelivery(HttpServletRequest request, @RequestBody PMInformationDelivery pmInformationDelivery) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return pmInformationDeliveryService.insert(pmInformationDelivery, tokenModel);
        } catch (Exception ex) {
            return ApiResult.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/selectPMInformationDelivery", method = {RequestMethod.POST})
    public ApiResult selectPMInformationDelivery(HttpServletRequest request, @RequestBody PMInformationDelivery pmInformationDelivery) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            pmInformationDelivery.setTenantid(tokenModel.getTenantId());
            List<PMInformationDelivery> pmInformationDeliveries = pmInformationDeliveryService.select(pmInformationDelivery);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pmInformationDelivery", pmInformationDeliveries);
            return ApiResult.success(jsonObject);
        } catch (Exception ex) {
            return ApiResult.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/wxSelectPMInformationDeliveryById", method = {RequestMethod.POST})
    public ApiResult wxSelectPMInformationDeliveryById(HttpServletRequest request, @RequestBody PMInformationDelivery pmInformationDelivery) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            List<PMInformationDelivery> pmInformationDeliveries = pmInformationDeliveryService.selectByid(tokenModel);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pmInformationDelivery", pmInformationDeliveries);
            return ApiResult.success(jsonObject);
        } catch (Exception ex) {
            return ApiResult.fail(ex.getMessage());
        }
    }

    @RequestMapping(value = "/updatePMInformationDelivery", method = {RequestMethod.POST})
    public ApiResult updatePMInformationDelivery(HttpServletRequest request, @RequestBody PMInformationDelivery pmInformationDelivery) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return pmInformationDeliveryService.update(pmInformationDelivery, tokenModel);
        } catch (Exception ex) {
            return ApiResult.fail(ex.getMessage());
        }
    }
}
