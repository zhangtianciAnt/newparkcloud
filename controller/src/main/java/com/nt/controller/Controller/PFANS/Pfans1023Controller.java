package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Confidential;
import com.nt.service_pfans.PFANS1000.ConfidentialService;
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
@RequestMapping("/confidential")
public class Pfans1023Controller {
    @Autowired
    private ConfidentialService confidentialService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{

            TokenModel tokenModel = tokenService.getToken(request);
            Confidential confidential = new Confidential();
            confidential.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(confidentialService.getConfidential(confidential));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Confidential confidential, HttpServletRequest request) throws Exception {
        if (confidential == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(confidentialService.One(confidential.getConfidentialid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Confidential confidential, HttpServletRequest request) throws Exception{
        if (confidential == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        confidentialService.update(confidential,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody Confidential confidential, HttpServletRequest request) throws Exception {
        if (confidential == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        confidentialService.insert(confidential,tokenModel);
        return ApiResult.success();
    }
}
