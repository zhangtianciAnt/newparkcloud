package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/companystatistics")
public class Pfans6009Controller {

    @Autowired
    private CompanyStatisticsService companyStatisticsService;


    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getCompanyReport1", method = {RequestMethod.GET})
    public ApiResult getCompanyReport1(HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getCosts(coststatistics));
    }


    @RequestMapping(value = "/getCompanyReport2", method = {RequestMethod.GET})
    public ApiResult getCompanyReport2(HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getWorkTimes(coststatistics));
    }

    @RequestMapping(value = "/getCompanyReport3", method = {RequestMethod.GET})
    public ApiResult getCompanyReport3(HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(companyStatisticsService.getWorkTimeInfos(coststatistics));
    }
}
