package com.nt.controller.Controller.PFANS;
import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.service_pfans.PFANS6000.VariousfundsService;
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
@RequestMapping("/variousfunds")
public class Pfans6007Controller {

    @Autowired
    private VariousfundsService variousfundsService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getvariousfunds(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Variousfunds variousfunds = new Variousfunds();
        variousfunds.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(variousfundsService.getvariousfunds(variousfunds));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult getexpatriatesinforApplyOne(@RequestBody Variousfunds variousfunds, HttpServletRequest request) throws Exception {
        if (variousfunds == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(variousfundsService.getvariousfundsApplyOne(variousfunds.getVariousfunds_id()));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateexpatriatesinforApply(@RequestBody Variousfunds variousfunds, HttpServletRequest request) throws Exception {
        if (variousfunds == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        variousfundsService.updatevariousfundsApply(variousfunds,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult createexpatriatesinforApply(@RequestBody Variousfunds variousfunds, HttpServletRequest request) throws Exception {
        if (variousfunds == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        variousfundsService.createvariousfundsApply(variousfunds,tokenModel);
        return ApiResult.success();
    }
}
