package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Security;
import com.nt.dao_Pfans.PFANS1000.Vo.SecurityVo;
import com.nt.service_pfans.PFANS1000.SecurityService;
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
@RequestMapping("/security")
public class Pfans1021Controller {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{

            TokenModel tokenModel = tokenService.getToken(request);
            Security security = new Security();
            security.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(securityService.getSecurity(security));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String securityid, HttpServletRequest request) throws Exception {
        if (securityid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(securityService.selectById(securityid));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody SecurityVo securityVo, HttpServletRequest request) throws Exception{
        if (securityVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        securityService.update(securityVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody SecurityVo securityVo, HttpServletRequest request) throws Exception {
        if (securityVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        securityService.insert(securityVo, tokenModel);
        return ApiResult.success();
    }

}
