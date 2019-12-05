package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Routing;
import com.nt.service_pfans.PFANS1000.RoutingService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/routing")
public class Pfans1016Controller {

    @Autowired
    private RoutingService routingService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getRouting(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Routing routing = new Routing();
        routing.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(routingService.getRouting(routing));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Routing routing, HttpServletRequest request) throws Exception {
        if (routing == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(routingService.One(routing.getRouting_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateFlexibleWork(@RequestBody Routing routing, HttpServletRequest request) throws Exception{
        if (routing == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        routingService.updateRouting(routing,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Routing routing, HttpServletRequest request) throws Exception {
        if (routing == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        routingService.insert(routing,tokenModel);
        return ApiResult.success();
    }





}
