package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.dao_Pfans.PFANS8000.MonthlyRate;
import com.nt.service_pfans.PFANS8000.InformationDeliveryService;
import com.nt.service_pfans.PFANS8000.MonthlyRateService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/monthlyrate")
public class Pfans8011Controller {

    @Autowired
    private MonthlyRateService monthlyrateservice;
    @Autowired
    private TokenService tokenService;

    @GetMapping("list")
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(monthlyrateservice.getdatalist());
    }

    @PostMapping("create")
    public ApiResult create(@RequestBody List<MonthlyRate> monthlyrate, HttpServletRequest request) throws Exception {
        if (monthlyrate == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        monthlyrateservice.CreateData(monthlyrate,tokenModel);
        return ApiResult.success();
    }

    @PostMapping("update")
    public ApiResult update(@RequestBody List<MonthlyRate> monthlyrate, HttpServletRequest request) throws Exception {
        if (monthlyrate == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        monthlyrateservice.UpdateData(monthlyrate,tokenModel);
        return ApiResult.success();
    }

    @PostMapping("slectlist")
    public ApiResult slectlist(@RequestBody MonthlyRate monthlyrate, HttpServletRequest request) throws Exception {
        if (monthlyrate == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(monthlyrateservice.slectlist(monthlyrate));
    }


}
