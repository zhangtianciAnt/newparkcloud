package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Cooperinterview;
import com.nt.service_pfans.PFANS6000.CooperinterviewService;
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
@RequestMapping("/cooperinterview")
public class Pfans6001Controller {

    @Autowired
    private CooperinterviewService cooperinterviewService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getcooperinterview(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Cooperinterview cooperinterview = new Cooperinterview();
        cooperinterview.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(cooperinterviewService.getcooperinterview(cooperinterview));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult getcooperinterviewApplyOne(@RequestBody Cooperinterview cooperinterview, HttpServletRequest request) throws Exception {
        if (cooperinterview == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(cooperinterviewService.getcooperinterviewApplyOne(cooperinterview.getCooperinterview_id()));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updatecooperinterviewApply(@RequestBody Cooperinterview cooperinterview, HttpServletRequest request) throws Exception {
        if (cooperinterview == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        cooperinterviewService.updatecooperinterviewApply(cooperinterview, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult createcooperinterviewApply(@RequestBody Cooperinterview cooperinterview, HttpServletRequest request) throws Exception {
        if (cooperinterview == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        cooperinterviewService.createcooperinterviewApply(cooperinterview, tokenModel);
        return ApiResult.success();
    }
}