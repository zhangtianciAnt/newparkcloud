package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.dao_Pfans.PFANS3000.Vo.JapanCondominiumVo;
import com.nt.service_pfans.PFANS3000.JapanCondominiumService;
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
@RequestMapping("/japancondominium")
public class Pfans3007Controller {

    @Autowired
    private JapanCondominiumService japancondominiumService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getJapanCondominium(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        JapanCondominium japancondominium = new JapanCondominium();
        japancondominium.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(japancondominiumService.getJapanCondominium(japancondominium));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String japancondominiumid, HttpServletRequest request) throws Exception {
        if (japancondominiumid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(japancondominiumService.selectById(japancondominiumid));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult insertJapanCondominiumVo(@RequestBody JapanCondominiumVo japancondominiumVo, HttpServletRequest request) throws Exception {
        if (japancondominiumVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        japancondominiumService.insertJapanCondominiumVo(japancondominiumVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateJapanCondominiumVo(@RequestBody JapanCondominiumVo japancondominiumVo, HttpServletRequest request) throws Exception {
        if (japancondominiumVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        japancondominiumService.updateJapanCondominiumVo(japancondominiumVo, tokenModel);
        return ApiResult.success();
    }

}
