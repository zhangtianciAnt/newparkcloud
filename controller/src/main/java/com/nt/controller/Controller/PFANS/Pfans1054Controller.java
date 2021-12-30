package com.nt.controller.Controller.PFANS;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS1000.ExpenditureForecast;
import com.nt.dao_Pfans.PFANS1000.RevenueForecast;
import com.nt.dao_Pfans.PFANS1000.Vo.ExpenditureForecastVo;
import com.nt.dao_Pfans.PFANS1000.Vo.RevenueForecastVo;
import com.nt.service_pfans.PFANS1000.ExpenditureForecastService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("expenditureForecast")
public class Pfans1054Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ExpenditureForecastService expenditureForecastService;

    @RequestMapping(value = "/getInfo",method={RequestMethod.POST})
    public ApiResult getInfo(@RequestBody ExpenditureForecast forecast, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        List<ExpenditureForecast> expenditureForecastList =  expenditureForecastService.getInfo(forecast);
        return ApiResult.success(expenditureForecastList);
    }

    @RequestMapping(value = "/saveInfo",method={RequestMethod.POST})
    public ApiResult saveInfo(@RequestBody ExpenditureForecastVo expenditureVo, HttpServletRequest request) throws Exception {

        TokenModel tokenModel = tokenService.getToken(request);
        expenditureForecastService.saveInfo(expenditureVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getThemeOutDepth",method={RequestMethod.POST})
    public ApiResult getThemeOutDepth(@RequestBody ExpenditureForecast forecast, HttpServletRequest request) throws Exception {

        TokenModel tokenModel = tokenService.getToken(request);
        List<ExpenditureForecast> revenueForecastList =  expenditureForecastService.getThemeOutDepth(forecast);
        return ApiResult.success(revenueForecastList);
    }

    @RequestMapping(value = "/getPoortDepth",method={RequestMethod.POST})
    public ApiResult getPoortDepth(@RequestBody ExpenditureForecast forecast, HttpServletRequest request) throws Exception {

        TokenModel tokenModel = tokenService.getToken(request);
        List<ExpenditureForecast> revenueForecastList =  expenditureForecastService.getPoortDepth(forecast);
        return ApiResult.success(revenueForecastList);
    }
}
