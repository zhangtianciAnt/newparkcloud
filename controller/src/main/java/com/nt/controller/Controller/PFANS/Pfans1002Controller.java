package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
import com.nt.service_pfans.PFANS1000.BusinessService;
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
@RequestMapping("/business")
public class Pfans1002Controller {
    @Autowired
    private BusinessService businessService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(String businesstype,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Business business = new Business();
        business.setBusinesstype(businesstype);
        business.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(businessService.get(business));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String businessid, HttpServletRequest request) throws Exception {
        if (businessid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessService.selectById(businessid));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult insertBusinessVo(@RequestBody BusinessVo businessVo, HttpServletRequest request) throws Exception {
        if (businessVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessService.insertBusinessVo(businessVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateBusinessVo(@RequestBody BusinessVo businessVo, HttpServletRequest request) throws Exception {
        if (businessVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessService.updateBusinessVo(businessVo, tokenModel);
        return ApiResult.success();
    }

}
