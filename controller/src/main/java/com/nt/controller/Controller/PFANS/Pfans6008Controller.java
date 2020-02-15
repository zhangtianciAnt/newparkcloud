package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
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
@RequestMapping("/coststatistics")
public class Pfans6008Controller {

    @Autowired
    private CoststatisticsService coststatisticsService;


    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getCostList", method = {RequestMethod.GET})
    public ApiResult getCostList(HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
//        coststatistics.setOwner(tokenModel.getUserId());
        return ApiResult.success(coststatisticsService.getCostList(coststatistics));
    }

    @RequestMapping(value = "/insertCoststatistics", method = {RequestMethod.POST})
    public ApiResult insertCoststatistics(HttpServletRequest request) throws Exception {
        Coststatistics coststatistics = new Coststatistics();
        TokenModel tokenModel = tokenService.getToken(request);
//        coststatistics.setOwner(tokenModel.getUserId());
        return ApiResult.success(coststatisticsService.insertCoststatistics(coststatistics, tokenModel));
    }
}
