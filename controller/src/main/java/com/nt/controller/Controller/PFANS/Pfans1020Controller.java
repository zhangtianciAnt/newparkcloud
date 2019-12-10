package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Outside;
import com.nt.dao_Pfans.PFANS1000.Vo.OutsideVo;
import com.nt.service_pfans.PFANS1000.OutsideService;
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
@RequestMapping("/outside")
public class Pfans1020Controller {

    @Autowired
    private OutsideService outsideService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Outside outside =new Outside();
        outside.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(outsideService.getOutside(outside));
    }

    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody OutsideVo outsideVo, HttpServletRequest request) throws Exception {
        if (outsideVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        outsideService.insert(outsideVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String outsideid, HttpServletRequest request) throws Exception {
        if (outsideid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(outsideService.selectById(outsideid));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody OutsideVo outsideVo, HttpServletRequest request) throws Exception {
        if (outsideVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        outsideService.update(outsideVo, tokenModel);
        return ApiResult.success();

    }
}
