package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Routing;
import com.nt.dao_Pfans.PFANS1000.Vo.RoutingVo;
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

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String routing_id, HttpServletRequest request) throws Exception {
        if (routing_id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(routingService.selectById(routing_id));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody RoutingVo routingVo, HttpServletRequest request) throws Exception{
        if (routingVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        routingService.update(routingVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody RoutingVo routingVo, HttpServletRequest request) throws Exception {
        if (routingVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        routingService.insert(routingVo,tokenModel);
        return ApiResult.success();
    }
}
