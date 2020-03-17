package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.service_pfans.PFANS6000.DeleginformationService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.services.TokenService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/delegainformation")
public class Pfans6006Controller {

    @Autowired
    private DeleginformationService deleginformationservice;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult createDeleginformation(@RequestBody Delegainformation delegainformation, HttpServletRequest request) throws Exception {
        if (delegainformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        deleginformationservice.createDeleginformation(delegainformation, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getDelegainformation(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(deleginformationservice.getDelegainformation());
    }
}
