package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.service_pfans.PFANS2000.OvertimeService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/Overtime")
public class Pfans2011Controller {
    //查找
    @Autowired
    private OvertimeService overtimeService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getOvertime(HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(overtimeService.getOvertime());
    }

    @RequestMapping(value="/insert",method = {RequestMethod.POST})
    public ApiResult insertOvertime(@RequestBody Overtime overtime, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        overtimeService.insertOvertime(overtime,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateOvertime(@RequestBody Overtime overtime, HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        overtimeService.updateOvertime(overtime,tokenModel);
        return ApiResult.success();
    }

}
