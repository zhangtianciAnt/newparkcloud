package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.service_pfans.PFANS2000.CasgiftApplyService;
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
@RequestMapping("/casgiftapply")
public class Pfans2022Controller {

    @Autowired
    private CasgiftApplyService casgiftapplyService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getCasgiftApply(HttpServletRequest request) throws Exception{


            TokenModel tokenModel = tokenService.getToken(request);
            CasgiftApply casgiftapply = new CasgiftApply();
            casgiftapply.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(casgiftapplyService.getCasgiftApply(casgiftapply));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception {
        if (casgiftapply == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(casgiftapplyService.One(casgiftapply.getCasgiftapplyid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateCasgiftApply(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception{
        if (casgiftapply == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.updateCasgiftApply(casgiftapply,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception {
        if (casgiftapply == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insert(casgiftapply,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getCasgiftApplyList", method = {RequestMethod.POST})
    public ApiResult getCasgiftApplyList(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception {
        if (casgiftapply == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(casgiftapplyService.getCasgiftApplyList(casgiftapply, request));
    }
}
